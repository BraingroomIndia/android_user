package com.braingroom.user.viewmodel;


import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.request.LoginReq;
import com.braingroom.user.model.response.LoginResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CheckoutActivity;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.HomeActivity;
import com.braingroom.user.view.activity.LoginActivity;
import com.braingroom.user.view.activity.SignupActivity;
import com.braingroom.user.view.activity.ThirdPartyViewActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONObject;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lombok.Setter;

public class LoginViewmodel extends ViewModel {

    public final ObservableField<String> email = new ObservableField<>("");
    public final ObservableField<String> password = new ObservableField<>("");
    public final Action onLoginClicked, onFbLoginClicked, onGoogleLoginClicked, onForgotPassClicked, onSignupClicked;

    @Setter
    JSONObject facebookUser;
    @Setter
    GoogleSignInAccount googleAccDetails;

    MessageHelper messageHelper;
    Navigator navigator;

    @Setter
    LoginActivity.UIHandler uiHandler;

    private final String referralCode;

    public final ObservableBoolean isOTP = new ObservableBoolean(false);


    public LoginViewmodel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, final String referralCode) {
        this.messageHelper = messageHelper;
        this.navigator = navigator;

        this.referralCode = referralCode;
        onLoginClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (!isEmailValid(email.get())) {
                    messageHelper.show("Enter a valid email");
                    return;
                }
                if (!isPasswordValid(password.get())) {
                    messageHelper.show("Enter a valid password");
                    return;
                }
                messageHelper.showProgressDialog("Logging in", "Sit back while we connect you...");
                emailLogin();
            }
        };

        onFbLoginClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHandler.fbLogin();
            }
        };
        onGoogleLoginClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHandler.googleLogin();
            }
        };

        onForgotPassClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHandler.showForgotPassDialog();
            }
        };

        onSignupClicked = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                data.putString("referralCode", referralCode);
                navigator.navigateActivity(SignupActivity.class, data);
            }
        };

    }

    public void emailLogin() {
        Observable<LoginResp> myObserb = apiService.login(email.get(), password.get(), pref.getString(Constants.FCM_TOKEN, ""));
        myObserb.subscribe(new Consumer<LoginResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull LoginResp loginResp) throws Exception {
                if (loginResp.getResCode().equals("1") && loginResp.getData().size() > 0) {
                    loginResp.getData().get(0).setEmailId(email.get());
                    loginResp.getData().get(0).setPassword(password.get());
                    messageHelper.dismissActiveProgress();
                    if ("".equals(loginResp.getData().get(0).getMobile()) || loginResp.getData().get(0).getReferralCode() == null) {
                        uiHandler.showEmailDialog(loginResp);
                        return;
                    } else if (loginResp.getData().get(0).getIsVerified() == 0) {
                        LoginResp.Snippet data = loginResp.getData().get(0);
                        SignUpResp.Snippet snippet = new SignUpResp.Snippet(data.getUuid(), data.getId(), data.getLoginType(), data.getEmailId(), data.getMobile(), data.getPassword());
                        uiHandler.changeToOTPFragment(snippet);
                        return;
                    } else {
                        editor.putBoolean(Constants.LOGGED_IN, true);
                        editor.putString(Constants.UUID, loginResp.getData().get(0).getUuid());
                        editor.putString(Constants.PROFILE_PIC, loginResp.getData().get(0).getProfilePic());
                        editor.putString(Constants.CITY_ID, loginResp.getData().get(0).getCityId());
                        editor.putString(Constants.NAME, loginResp.getData().get(0).getName());
                        editor.putString(Constants.BG_ID, loginResp.getData().get(0).getId());
                        editor.commit();

                        navigator.finishActivity(new Intent());
                    }

                } else

                {
                    String fcmToken = pref.getString(Constants.FCM_TOKEN, "");
                    editor.clear();
                    editor.putString(Constants.FCM_TOKEN, fcmToken);
                    editor.commit();
                    messageHelper.show(loginResp.getResMsg());
                    return;

                }
            }
        }, new Consumer<Throwable>()

        {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                messageHelper.show("some error occurred");
            }
        });
    }

    public void socialLogin(final String type) {
        String name = "", email = "", picture = null, socialId = "", fcmToken;
        if (type.equals("facebook")) {
            name = facebookUser.optString("name");
            picture = facebookUser.optJSONObject("picture").optJSONObject("data").optString("url");
            email = facebookUser.optString("email");
            socialId = facebookUser.optString("id");
        }
        if (type.equals("google")) {
            name = googleAccDetails.getDisplayName();
            if (googleAccDetails.getPhotoUrl() != null)
                picture = googleAccDetails.getPhotoUrl().toString();
            else picture = "";
            email = googleAccDetails.getEmail();
            socialId = googleAccDetails.getId();
        }
        editor.putString(Constants.NAME, name);
        editor.putString(Constants.PROFILE_PIC, picture);
        editor.putString(Constants.EMAIL, email);
        editor.commit();
        fcmToken = pref.getString(Constants.FCM_TOKEN, "");

        final String emailId = email;


        apiService.socialLogin(name, picture, email, socialId, fcmToken, "", referralCode).subscribe(new Consumer<LoginResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull LoginResp loginResp) throws Exception {


                if (loginResp.getResCode().equals("1") && loginResp.getData().size() > 0) {
                    if ("".equals(loginResp.getData().get(0).getMobile())) {
                        uiHandler.showEmailDialog(loginResp);
                        return;
                    } else if (loginResp.getData().get(0).getIsVerified() == 0) {

                        LoginResp.Snippet data = loginResp.getData().get(0);
                        SignUpResp.Snippet snippet = new SignUpResp.Snippet(data.getUuid(), data.getId(), data.getLoginType(), data.getEmailId(), data.getMobile(), data.getPassword());
                        uiHandler.changeToOTPFragment(snippet);
                        return;
                    } else {
                        editor.putBoolean(Constants.LOGGED_IN, true);
                        editor.putString(Constants.UUID, loginResp.getData().get(0).getUuid());
                        editor.putString(Constants.BG_ID, loginResp.getData().get(0).getId());
                        editor.commit();


                        navigator.finishActivity(new Intent());
                    }

                } else {
                    logOut();
                    messageHelper.show(loginResp.getResMsg());
                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                logOut();
                messageHelper.show("some error occurred");
            }
        });
    }

    public void forgotPassword(String email) {
        LoginReq.Snippet snippet = new LoginReq.Snippet();
        snippet.setEmail(email);

        apiService.forgotPassword(email).subscribe(new Consumer<LoginResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull LoginResp loginResp) throws Exception {
                if (loginResp.getResCode().equals("1")) {
                    messageHelper.show(loginResp.getResMsg());
                } else {
                    messageHelper.show(loginResp.getResMsg());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                messageHelper.show("some error occurred");
            }
        });
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 1;
    }


}

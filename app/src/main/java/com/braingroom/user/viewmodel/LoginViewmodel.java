package com.braingroom.user.viewmodel;


import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.braingroom.user.model.request.LoginReq;
import com.braingroom.user.model.response.LoginResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.LoginActivity;
import com.braingroom.user.view.activity.SignupActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import org.json.JSONObject;

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
                    LoginResp.Snippet data = loginResp.getData().get(0);
                    messageHelper.dismissActiveProgress();
                    if ("".equals(loginResp.getData().get(0).getMobile()) || loginResp.getData().get(0).getReferralCode() == null) {
                        uiHandler.showEmailDialog(loginResp);
                        return;
                    } else if (loginResp.getData().get(0).getIsVerified() == 0) {

                        SignUpResp.Snippet snippet = new SignUpResp.Snippet(data.getUuid(), data.getId(), data.getLoginType(), data.getEmailId(), data.getMobile(), data.getPassword());
                        uiHandler.changeToOTPFragment(snippet);
                        return;
                    } else {
                        //login(data.getName(), data.getEmailId(), data.getProfilePic(), data.getId(), data.getUuid());
                        editor.putBoolean(Constants.LOGGED_IN, true);
                        editor.putString(Constants.NAME, data.getName());
                        editor.putString(Constants.EMAIL, data.getEmailId());
                        editor.putString(Constants.PROFILE_PIC, data.getProfilePic());
                        editor.putString(Constants.BG_ID, data.getId());
                        editor.putString(Constants.UUID, data.getUuid());
                        editor.commit();
                        navigator.finishActivity(new Intent());
                        return;
                    }

                } else

                {
                    logOut();
                    messageHelper.show(loginResp.getResMsg());
                    return;

                }
            }
        }, new Consumer<Throwable>()

        {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
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

        fcmToken = pref.getString(Constants.FCM_TOKEN, "");

        editor.putString(Constants.NAME, email);
        editor.putString(Constants.EMAIL, name);
        editor.putString(Constants.PROFILE_PIC, picture);


        apiService.socialLogin(name, picture, email, socialId, fcmToken, "").subscribe(new Consumer<LoginResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull LoginResp loginResp) throws Exception {


                if (loginResp.getResCode().equals("1") && loginResp.getData().size() > 0) {
                    LoginResp.Snippet data = loginResp.getData().get(0);
                    if (TextUtils.isEmpty(loginResp.getData().get(0).getMobile()) || TextUtils.isEmpty(loginResp.getData().get(0).getEmailId())) {
                        //login(userName, emailId, profilePic, data.getId(), data.getUuid());
                        editor.putString(Constants.BG_ID, data.getId());
                        editor.putString(Constants.UUID, data.getUuid());
                        editor.commit();
                        uiHandler.showEmailDialog(loginResp);
                        return;
                    } else if (loginResp.getData().get(0).getIsVerified() == 0) {
                        SignUpResp.Snippet snippet = new SignUpResp.Snippet(data.getUuid(), data.getId(), data.getLoginType(), data.getEmailId(), data.getMobile(), data.getPassword());
                        //login(userName, emailId, profilePic, data.getId(), data.getUuid());
                        editor.putString(Constants.BG_ID, data.getId());
                        editor.putString(Constants.UUID, data.getUuid());
                        editor.commit();
                        uiHandler.changeToOTPFragment(snippet);
                        return;
                    } else {
                        //login(userName, emailId, profilePic, data.getId(), data.getUuid());
                        editor.putBoolean(Constants.LOGGED_IN, true);
                        editor.putString(Constants.BG_ID, data.getId());
                        editor.putString(Constants.UUID, data.getUuid());
                        editor.commit();
                        navigator.finishActivity(new Intent());
                        return;
                    }

                } else {
                    logOut();
                    messageHelper.show(loginResp.getResMsg());
                    return;
                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                logOut();
                throwable.printStackTrace();
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

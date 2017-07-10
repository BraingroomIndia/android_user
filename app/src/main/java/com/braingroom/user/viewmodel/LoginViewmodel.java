package com.braingroom.user.viewmodel;


import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.request.LoginReq;
import com.braingroom.user.model.response.LoginResp;
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

    String parentActivity;
    String classId;
    String origin;
    String thirdPartyUserId;
    private final String referralCode;
    Serializable classData;
    String catalogueId;


    public LoginViewmodel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, String parentActivity,
                          Serializable data, String classId,String catalogueId, String origin, String thirdPartyUserId, final String referralCode) {
        this.parentActivity = parentActivity;
        this.classData = data;
        this.thirdPartyUserId = thirdPartyUserId;
        this.messageHelper = messageHelper;
        this.navigator = navigator;
        this.classId = classId;
        this.origin = origin;
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
                    messageHelper.dismissActiveProgress();
                    if ("".equals(loginResp.getData().get(0).getMobile()) || loginResp.getData().get(0).getReferralCode() == null) {
                        uiHandler.showEmailDialog(loginResp);
                    } else {
                        editor.putBoolean(Constants.LOGGED_IN, true);
                        editor.putString(Constants.UUID, loginResp.getData().get(0).getUuid());
                        editor.putString(Constants.PROFILE_PIC, loginResp.getData().get(0).getProfilePic());
                        editor.putString(Constants.CITY_ID, loginResp.getData().get(0).getCityId());
                        editor.putString(Constants.NAME, loginResp.getData().get(0).getName());
                        editor.putString(Constants.BG_ID, loginResp.getData().get(0).getId());
                        editor.commit();
                        if (CheckoutActivity.class.getSimpleName().equals(parentActivity)) {
                            Bundle data = new Bundle();
                            data.putSerializable("classData", classData);
                            navigator.navigateActivity(CheckoutActivity.class, data);
                        } else if (ConnectHomeActivity.class.getSimpleName().equals(parentActivity)) {
                            navigator.navigateActivity(ConnectHomeActivity.class, null);
                        } else if (ClassDetailActivity.class.getSimpleName().equals(parentActivity)) {
                            Bundle data = new Bundle();
                            data.putString("id", classId);
                            data.putString("catalogueId",catalogueId);
                            data.putString("origin", origin);
                            navigator.navigateActivity(ClassDetailActivity.class, data);
                        } else if (ThirdPartyViewActivity.class.getSimpleName().equals(parentActivity)) {
                            Bundle data = new Bundle();
                            data.putString("userId", thirdPartyUserId);
                            navigator.navigateActivity(ThirdPartyViewActivity.class, data);
                        } else {
                            navigator.navigateActivity(HomeActivity.class, null);
                        }
                        navigator.finishActivity();
                    }
                } else

                {
                    String fcmToken = pref.getString(Constants.FCM_TOKEN, "");
                    editor.clear();
                    editor.putString(Constants.FCM_TOKEN, fcmToken);
                    editor.commit();
                    messageHelper.show(loginResp.getResMsg());

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


        apiService.socialLogin(name, picture, email, socialId, fcmToken, "", referralCode).subscribe(new Consumer<LoginResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull LoginResp loginResp) throws Exception {


                if (loginResp.getResCode().equals("1") && loginResp.getData().size() > 0) {
                    if ("".equals(loginResp.getData().get(0).getMobile())) {
                        uiHandler.showEmailDialog(loginResp);
                    } else {
                        editor.putBoolean(Constants.LOGGED_IN, true);
                        editor.putString(Constants.UUID, loginResp.getData().get(0).getUuid());
                        editor.putString(Constants.BG_ID, loginResp.getData().get(0).getId());
                        editor.commit();

                        if (HomeActivity.class.getSimpleName().equals(parentActivity)) {
                            navigator.navigateActivity(HomeActivity.class, null);
                        } else {
                            Bundle data = new Bundle();
                            data.putSerializable("classData", classData);
                            navigator.navigateActivity(CheckoutActivity.class, data);
                        }
                        if (CheckoutActivity.class.getSimpleName().equals(parentActivity)) {
                            Bundle data = new Bundle();
                            data.putSerializable("classData", classData);
                            navigator.navigateActivity(CheckoutActivity.class, data);
                        } else if (ConnectHomeActivity.class.getSimpleName().equals(parentActivity)) {
                            navigator.navigateActivity(ConnectHomeActivity.class, null);
                        } else if (ClassDetailActivity.class.getSimpleName().equals(parentActivity)) {
                            Bundle data = new Bundle();
                            data.putString("id", classId);
                            data.putString("catalogueId", catalogueId);
                            data.putString("origin", ClassListViewModel1.ORIGIN_HOME);
                            navigator.navigateActivity(ClassDetailActivity.class, data);
                        } else if (ThirdPartyViewActivity.class.getSimpleName().equals(parentActivity)) {
                            Bundle data = new Bundle();
                            data.putString("userId", thirdPartyUserId);
                            navigator.navigateActivity(ThirdPartyViewActivity.class, data);
                        } else {
                            navigator.navigateActivity(HomeActivity.class, null);
                        }


                        navigator.finishActivity();
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

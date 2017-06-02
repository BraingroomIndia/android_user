package com.braingroom.user.viewmodel;


import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.braingroom.user.model.request.LoginReq;
import com.braingroom.user.model.response.LoginResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CheckoutActivity;
import com.braingroom.user.view.activity.HomeActivity;
import com.braingroom.user.view.activity.LoginActivity;
import com.braingroom.user.view.activity.SearchActivity;
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

    String classId;

    public LoginViewmodel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @Nullable String classId) {
        this.messageHelper = messageHelper;
        this.navigator = navigator;
        this.classId = classId;
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
                navigator.navigateActivity(SignupActivity.class, null);
            }
        };

    }

    public void emailLogin() {
        Observable<LoginResp> myObserb = apiService.login(email.get(), password.get(), pref.getString(Constants.FCM_TOKEN, ""));
        myObserb.subscribe(new Consumer<LoginResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull LoginResp loginResp) throws Exception {
                if (loginResp.getResCode().equals("1") && loginResp.getData().size() > 0) {
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
                        if (classId != null) {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString(classId,"classId");
                            intent.putExtras(bundle);
                            navigator.finishActivity(intent);
                        }
                        navigator.navigateActivity(HomeActivity.class, null);
                        navigator.finishActivity();
                    }
                } else {
                    editor.clear();
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


        apiService.socialLogin(name, picture, email, socialId, fcmToken, "").subscribe(new Consumer<LoginResp>() {
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
                        navigator.navigateActivity(HomeActivity.class, null);
                        navigator.finishActivity();
                    }
                } else {
                    editor.clear();
                    editor.commit();
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
        return password.length() > 4;
    }


}

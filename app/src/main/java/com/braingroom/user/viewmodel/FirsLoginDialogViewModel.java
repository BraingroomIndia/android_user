package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.FirstSocialLoginResp;
import com.braingroom.user.model.response.LoginResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.LoginActivity;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lombok.Setter;

/**
 * Created by godara on 24/05/17.
 */

public class FirsLoginDialogViewModel extends CustomDialogViewModel {
    public final DataItemViewModel emailVm, mobileVm, referralVm;
    public final Action onContinue;
    public final String userId;
    public String emailId;
    @Setter
    LoginActivity.UIHandler uiHandler;

    public FirsLoginDialogViewModel(@NonNull final LoginResp loginResp, @NonNull final MessageHelper messageHelper,
                                    @NonNull final Navigator navigator, @Nullable final String referralCode) {
        emailVm = new DataItemViewModel("");
        mobileVm = new DataItemViewModel("");
        referralVm = new DataItemViewModel("");
        this.userId = loginResp.getData().get(0).getId();
        this.emailId = loginResp.getData().get(0).getEmailId();

        if (loginResp.getData().get(0).getReferralCode().equals("1") || !isEmpty(referralCode))
            referralVm.s_1.set(null);

        if (isEmpty(loginResp.getData().get(0).getMobile()))
            mobileVm.s_1.set(null);

        if (isEmpty(emailId))
            emailVm.s_1.set(null);

        onContinue = new Action() {
            @Override
            public void run() throws Exception {
                if (TextUtils.isEmpty(emailId)) {
                    emailId = emailVm.s_1.get();
                    if (!isValidEmail(emailId)) {
                        messageHelper.show("Enter a vaild email Id");
                    }
                }
                if (TextUtils.isEmpty(loginResp.getData().get(0).getMobile()) && !isValidPhoneNo(mobileVm.s_1.get())) {
                    messageHelper.show("Enter a valid mobile number");
                    return;
                }

                apiService.firstSocialLogin(userId, emailId, mobileVm.s_1.get() != null ? mobileVm.s_1.get() : loginResp.getData().get(0).getMobile(), referralVm.s_1.get() != null ? referralVm.s_1.get() : referralCode).
                        subscribe(new Consumer<FirstSocialLoginResp>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull FirstSocialLoginResp resp) throws Exception {
                                if (resp.getData().isEmpty()) {
                                    logOut();
                                    messageHelper.show(resp.getResMsg());
                                    dismissDialog();
                                } else {
                                    pref.edit().putString(Constants.referralCode, "").apply();
                                    LoginResp.Snippet data = loginResp.getData().get(0);
                                    if (mobileVm.s_1.get() != null) {
                                        SignUpResp.Snippet snippet = new SignUpResp.Snippet(data.getUuid(), data.getId(), data.getLoginType(), data.getEmailId(), mobileVm.s_1.get(), data.getPassword());
                                        uiHandler.changeToOTPFragment(snippet);
                                        if (!"direct".equals(data.getLoginType())) {
                                            //login(data.getName(),data.getEmailId(),data.getProfilePic(),data.getId(),data.getUuid());
                                            editor.putBoolean(Constants.LOGGED_IN, true);
                                            editor.putString(Constants.BG_ID, data.getId());
                                            editor.putString(Constants.UUID, data.getUuid());
                                            editor.commit();
                                        }
                                        dismissDialog();
                                    } else {
                                        // login(data.getName(),data.getEmailId(),data.getProfilePic(),data.getId(),data.getUuid());
                                        editor.putBoolean(Constants.LOGGED_IN, true);
                                        editor.putString(Constants.NAME, data.getName());
                                        editor.putString(Constants.EMAIL, data.getEmailId());
                                        editor.putString(Constants.PROFILE_PIC, data.getProfilePic());
                                        editor.putString(Constants.BG_ID, data.getId());
                                        editor.putString(Constants.UUID, data.getUuid());
                                        editor.commit();
                                        navigator.finishActivity(new Intent());
                                    }

                                }

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                logOut();
                                messageHelper.show("some Error occurred");

                            }
                        });

            }
        };
    }

    private static boolean isValidEmail(String target) {
        return target != null && target.contains("@");
    }


}

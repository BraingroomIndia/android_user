package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.braingroom.user.model.response.BaseResp;
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
    public final DataItemViewModel mobileVm, referralVm;
    public final Action onContinue;
    public final String userId;
    @Setter
    LoginActivity.UIHandler uiHandler;

    public FirsLoginDialogViewModel(@NonNull final LoginResp loginResp, @NonNull final MessageHelper messageHelper,
                                    @NonNull final Navigator navigator) {
        mobileVm = new DataItemViewModel("");

        referralVm = new DataItemViewModel("");
        this.userId = loginResp.getData().get(0).getId();

        if (loginResp.getData().get(0).getReferralCode().equals("1"))
            referralVm.s_1.set(null);

        if (loginResp.getData().get(0).getMobile() != null && !loginResp.getData().get(0).getMobile().equals(""))
            mobileVm.s_1.set(null);


        onContinue = new Action() {
            @Override
            public void run() throws Exception {
                if (!isValidPhoneNo(mobileVm.s_1.get())) {
                    messageHelper.show("Enter a valid mobile number");
                    return;
                }

                apiService.firstSocialLogin(userId, mobileVm.s_1.get(), referralVm.s_1.get()).subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                        if (baseResp.getResCode().equals("0")) {
                            logOut();
                            messageHelper.show(baseResp.getResMsg());
                        } else {
                            LoginResp.Snippet data =loginResp.getData().get(0);
                            if (mobileVm.s_1.get() != null) {
                                SignUpResp.Snippet snippet = new SignUpResp.Snippet(data.getUuid(), data.getId(), data.getLoginType(), data.getEmailId(), mobileVm.s_1.get(), data.getPassword());
                                uiHandler.changeToOTPFragment(snippet);
                                if (!data.getLoginType().equals("direct")){
                                    //login(data.getName(),data.getEmailId(),data.getProfilePic(),data.getId(),data.getUuid());
                                    editor.putBoolean(Constants.LOGGED_IN, true);
                                    editor.putString(Constants.NAME, data.getName());
                                    editor.putString(Constants.EMAIL, data.getEmailId());
                                    editor.putString(Constants.PROFILE_PIC,data.getProfilePic());
                                    editor.putString(Constants.BG_ID, data.getId());
                                    editor.putString(Constants.UUID, data.getUuid());
                                    editor.commit();
                                }
                                dismissDialog();
                            }
                            else {
                               // login(data.getName(),data.getEmailId(),data.getProfilePic(),data.getId(),data.getUuid());
                                editor.putBoolean(Constants.LOGGED_IN, true);
                                editor.putString(Constants.NAME, data.getName());
                                editor.putString(Constants.EMAIL, data.getEmailId());
                                editor.putString(Constants.PROFILE_PIC,data.getProfilePic());
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


}

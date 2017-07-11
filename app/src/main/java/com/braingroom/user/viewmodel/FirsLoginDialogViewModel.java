package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.LoginResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CheckoutActivity;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.HomeActivity;

import java.io.Serializable;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by godara on 24/05/17.
 */

public class FirsLoginDialogViewModel extends CustomDialogViewModel {
    public final DataItemViewModel mobileVm, referralVm;
    public final Action onContinue;
    public final String userId;

    public FirsLoginDialogViewModel(@NonNull final LoginResp loginResp, @NonNull final MessageHelper messageHelper,
                                    @NonNull final Navigator navigator, final String parentActivity, final String classId,
                                    final Serializable classData) {
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
                            String fcmToken = pref.getString(Constants.FCM_TOKEN,"");
                            editor.clear();
                            editor.putString(Constants.FCM_TOKEN,fcmToken);
                            editor.commit();
                            messageHelper.show(baseResp.getResMsg());
                        } else {
                            editor.putBoolean(Constants.LOGGED_IN, true);
                            editor.putString(Constants.UUID, loginResp.getData().get(0).getUuid());
                            editor.putString(Constants.BG_ID, loginResp.getData().get(0).getId());
                            editor.commit();
                            navigator.navigateActivity(HomeActivity.class, null);
                            if (CheckoutActivity.class.getSimpleName().equals(parentActivity)) {
                                Bundle data = new Bundle();
                                data.putSerializable("classData", classData);
                                navigator.navigateActivity(CheckoutActivity.class, data);
                            } else if (ConnectHomeActivity.class.getSimpleName().equals(parentActivity)) {
                                navigator.navigateActivity(ConnectHomeActivity.class, null);
                            } else if (ClassDetailActivity.class.getSimpleName().equals(parentActivity)) {
                                Bundle data =new Bundle();
                                data.putString("id", classId);
                                data.putString("origin", ClassListViewModel1.ORIGIN_HOME);
                                navigator.navigateActivity(ClassDetailActivity.class,data);
                            } else {
                                navigator.navigateActivity(HomeActivity.class, null);
                            }
                            navigator.finishActivity();
                            navigator.finishActivity();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        String fcmToken = pref.getString(Constants.FCM_TOKEN,"");
                        editor.clear();
                        editor.putString(Constants.FCM_TOKEN,fcmToken);
                        editor.commit();
                        messageHelper.show("some Error occurred");

                    }
                });

            }
        };
    }




}

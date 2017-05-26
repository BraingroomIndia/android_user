package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.GuestUserResp;
import com.braingroom.user.model.response.LoginResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.HomeActivity;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by godara on 24/05/17.
 */

public class FirstSocialLoginDialogViewModel extends CustomDialogViewModel {
    public final DataItemViewModel mobileVm, referralVm;
    public final Action onContinue;
    public final String userId;

    public FirstSocialLoginDialogViewModel(@NonNull final LoginResp loginResp, @NonNull final MessageHelper messageHelper,
                                           @NonNull final Navigator navigator) {
        mobileVm = new DataItemViewModel("");

        referralVm = new DataItemViewModel("");
        this.userId = loginResp.getData().get(0).getId();

        if (loginResp.getData().get(0).getReferralCode().equals("1"))
            referralVm.s_1.set(null);

        if (loginResp.getData().get(0).getMobile() != null)
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
                            editor.clear();
                            messageHelper.show(baseResp.getResMsg());
                        } else {
                            editor.putBoolean(Constants.LOGGED_IN, true);
                            editor.putString(Constants.UUID, loginResp.getData().get(0).getUuid());
                            editor.putString(Constants.BG_ID, loginResp.getData().get(0).getId());
                            editor.commit();
                            navigator.navigateActivity(HomeActivity.class, null);
                            navigator.finishActivity();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        editor.clear();
                        messageHelper.show("some Error occurred");

                    }
                });

            }
        };
    }

    private boolean isValidPhoneNo(CharSequence phoneNo) {
        if (!TextUtils.isEmpty(phoneNo)) {
            return android.util.Patterns.PHONE.matcher(phoneNo).matches();
        }
        return false;
    }


}

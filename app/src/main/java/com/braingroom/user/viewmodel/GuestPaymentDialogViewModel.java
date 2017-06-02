package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.response.GuestUserResp;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CheckoutActivity;
import com.braingroom.user.view.activity.LoginActivity;

import org.apache.commons.validator.routines.EmailValidator;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class GuestPaymentDialogViewModel extends CustomDialogViewModel {

    public final DataItemViewModel emailVm, mobileVm, nameVm;
    public final Action onClickLogin, onClickGuestPay;

    public GuestPaymentDialogViewModel(final ClassData classData, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, final CheckoutViewModel.UiHelper uiHelper, String classId) {
        emailVm = new DataItemViewModel("");
        mobileVm = new DataItemViewModel("");
        nameVm = new DataItemViewModel("");
        onClickLogin = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                data.putString("backStackActivity", CheckoutActivity.class.getSimpleName());
                data.putSerializable("classData", classData);
                navigator.navigateActivity(LoginActivity.class, data);
                navigator.finishActivity();
            }
        };

        onClickGuestPay = new Action() {
            @Override
            public void run() throws Exception {
                if ("".equals(nameVm.s_1.get())) {
                    messageHelper.show("Name cannot be empty");
                    return;
                }
                if (!EmailValidator.getInstance(false).isValid(emailVm.s_1.get())) {
                    messageHelper.show("Enter a valid email");
                    return;
                }
                if (!isValidPhoneNo(mobileVm.s_1.get())) {
                    messageHelper.show("Enter a valid mobile number");
                    return;
                }

                apiService.addGuestUser(nameVm.s_1.get(), emailVm.s_1.get(), mobileVm.s_1.get()).subscribe(new Consumer<GuestUserResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull GuestUserResp guestUserResp) throws Exception {
                        if ("1".equals(guestUserResp.getResCode())) {
                            uiHelper.initiateGuestPayment(guestUserResp.getData().get(0).getUserId());
                        } else {
                            messageHelper.show(guestUserResp.getResMsg());
                        }
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

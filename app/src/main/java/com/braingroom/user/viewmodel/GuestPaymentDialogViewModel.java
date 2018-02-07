package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.response.GuestUserResp;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.LoginActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

//import org.apache.commons.validator.routines.EmailValidator;

public class GuestPaymentDialogViewModel extends CustomDialogViewModel {

    public final DataItemViewModel emailVm, mobileVm, nameVm;
    public final Action onClickLogin, onClickGuestPay;

    public GuestPaymentDialogViewModel(final ClassData classData, @NonNull final MessageHelper messageHelper,
                                       @NonNull final Navigator navigator, final CheckoutViewModel.UiHelper uiHelper, String classId) {
        emailVm = new DataItemViewModel("");
        mobileVm = new DataItemViewModel("");
        nameVm = new DataItemViewModel("");
        onClickLogin = new Action() {
            @Override
            public void run() throws Exception {
                dismissDialog();
                navigator.navigateActivityForResult(LoginActivity.class, null, REQ_CODE_LOGIN);

            }
        };

        onClickGuestPay = new Action() {
            @Override
            public void run() throws Exception {
                if (isEmpty(nameVm.s_1.get())) {
                    messageHelper.show("Name cannot be empty");
                    return;
                }
                if (isEmpty(emailVm.s_1.get())) {//(!EmailValidator.getInstance(false).isValid(emailVm.s_1.get())) {
                    messageHelper.show("Enter a valid email");
                    return;
                }
                if (isEmpty(mobileVm.s_1.get())) {
                    messageHelper.show("Enter a valid mobile number");
                    return;
                }

                apiService.addGuestUser(nameVm.s_1.get(), emailVm.s_1.get(), mobileVm.s_1.get()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<GuestUserResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull GuestUserResp guestUserResp) throws Exception {
                        dismissDialog();
                        if (!isEmpty(guestUserResp.getData())) {
                            uiHelper.onGuestLoginSuccess(guestUserResp.getData().get(0).getUserId());

                        } else {
                            messageHelper.showDismissInfo("Error", guestUserResp.getResMsg());
                        }
                    }
                });
            }
        };
    }
}

package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.view.MessageHelper;

import io.reactivex.functions.Action;

public class GiftingDetailsDialogViewModel extends CustomDialogViewModel {

    public final DataItemViewModel emailVm, personalMsg, nameVm;
    public final Action onClickCollectDetails;

    public GiftingDetailsDialogViewModel(@NonNull final MessageHelper messageHelper, final CheckoutViewModel.UiHelper uiHelper) {
        emailVm = new DataItemViewModel("");
        personalMsg = new DataItemViewModel("");
        nameVm = new DataItemViewModel("");
        onClickCollectDetails = new Action() {
            @Override
            public void run() throws Exception {
                if (!emailVm.s_1.get().equals("")) {
                    messageHelper.show("Please Enter a email Id");
                    return;
                }
                uiHelper.onCollectGiftDetail(nameVm.s_1.get(), emailVm.s_1.get(), personalMsg.s_1.get());
                dismissDialog();
            }
        };

    }
}

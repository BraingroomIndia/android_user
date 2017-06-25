package com.braingroom.user.viewmodel;

import io.reactivex.functions.Action;

public class GiftingDetailsDialogViewModel extends CustomDialogViewModel {

    public final DataItemViewModel emailVm, personalMsg, nameVm;
    public final Action onClickCollectDetails;

    public GiftingDetailsDialogViewModel(final CheckoutViewModel.UiHelper uiHelper) {
        emailVm = new DataItemViewModel("");
        personalMsg = new DataItemViewModel("");
        nameVm = new DataItemViewModel("");
        onClickCollectDetails = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.onCollectGiftDetail(nameVm.s_1.get(), emailVm.s_1.get(), personalMsg.s_1.get());
                dismissDialog();
            }
        };

    }
}

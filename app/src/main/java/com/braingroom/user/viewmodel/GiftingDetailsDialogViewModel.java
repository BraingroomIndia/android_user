package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;
import android.text.TextUtils;

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
            }
        };

    }
}

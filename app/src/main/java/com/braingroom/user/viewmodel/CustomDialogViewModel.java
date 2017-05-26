package com.braingroom.user.viewmodel;

import com.afollestad.materialdialogs.MaterialDialog;

public class CustomDialogViewModel extends ViewModel {

    MaterialDialog dialog;

    public void setDialogInstance(MaterialDialog dialog) {
        this.dialog = dialog;
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) dialog.dismiss();
    }
}

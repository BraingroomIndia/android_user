package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.model.request.ContactAdmin;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/*
  Created by godara on 06/06/17.*/


public class ContactAdminDialogViewModel extends CustomDialogViewModel {
    public final DataItemViewModel nameVm, mobileVm, emailVm, dateVm;
    public final ObservableField<String> message;
    public final Action onSubmit, onDissmis;

    public ContactAdminDialogViewModel(@NonNull final MessageHelper messageHelper,
                                       @NonNull final Navigator navigator, final String classId) {
        onDissmis = new Action() {
            @Override
            public void run() throws Exception {
                dismissDialog();
            }
        };

        nameVm = new DataItemViewModel("");
        mobileVm = new DataItemViewModel("");
        emailVm = new DataItemViewModel("");
        dateVm = new DataItemViewModel("");
        message = new ObservableField<>("");
        onSubmit = new Action() {
            @Override
            public void run() throws Exception {
                if (nameVm.s_1.get().equals("")) {
                    messageHelper.show("Please enter your name");
                    return;
                } else if (!isValidPhoneNo(mobileVm.s_1.get())) {
                    messageHelper.show("Please enter a valid phone Number");
                    return;
                } else if (false) { //if (!EmailValidator.getInstance(false).isValid(emailVm.s_1.get())){
                    messageHelper.show("Please enter a valid email id");
                    return;
                } else if (message.get().equals("")) {
                    messageHelper.show("Please write a message");
                    return;
                }
                ContactAdmin.Snippet snippet = new ContactAdmin.Snippet();
                snippet.setName(nameVm.s_1.get());
                snippet.setMobile(mobileVm.s_1.get());
                snippet.setEmail(emailVm.s_1.get());
                snippet.setDatetime(dateVm.s_1.get());
                snippet.setMessage(message.get());
                apiService.contactAdmin(new ContactAdmin(snippet)).subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                        messageHelper.show(baseResp.getResMsg());
                        dismissDialog();

                    }
                });

            }
        };


    }
}

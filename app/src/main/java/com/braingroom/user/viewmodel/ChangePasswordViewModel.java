package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.model.request.ChangePasswordReq;
import com.braingroom.user.model.response.ChangePasswordResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.LoginActivity;
import com.braingroom.user.view.activity.SignupActivity;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by godara on 18/05/17.
 */

public class ChangePasswordViewModel extends ViewModel {
    public final ObservableField<String> oldPassword = new ObservableField<>("");
    public final ObservableField<String> newPassword = new ObservableField<>("");
    public final ObservableField<String> confirmPassword = new ObservableField<>("");

    public final Action onChangePasswordClicked;

    MessageHelper messageHelper;
    Navigator navigator;

    public ChangePasswordViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        this.messageHelper = messageHelper;
        this.navigator = navigator;
        onChangePasswordClicked = new Action() {
            @Override
            public void run() throws Exception {

                if (!isPasswordValid(newPassword.get())) {
                    messageHelper.show("Enter a valid password");
                } else if (!newPassword.get().equals(confirmPassword.get())) {
                    messageHelper.show("Password doesn't match");
                } else {
                    messageHelper.showProgressDialog("Wait", "Changing password...");
                    changePassword();
                }

            }
        };


    }

    public void changePassword() {
        ChangePasswordReq.Snippet snippet = new ChangePasswordReq.Snippet();
        snippet.setOldPassword(oldPassword.get());
        snippet.setUuid(pref.getString(Constants.UUID, ""));
        snippet.setNewPassword(newPassword.get());
        Observable<ChangePasswordResp> respObservable = apiService.changePassword(snippet);
                respObservable.subscribe(new Consumer<ChangePasswordResp>() {

            @Override
            public void accept(@io.reactivex.annotations.NonNull ChangePasswordResp changePasswordResp) throws Exception {
                if (changePasswordResp.getData()!=null) {
                    messageHelper.dismissActiveProgress();
                    logOut();
                    navigator.navigateActivity(LoginActivity.class, null);
                } else {
                    messageHelper.dismissActiveProgress();
                    messageHelper.show("Unable to change password");

                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                messageHelper.show("some error occurred");
            }
        });
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

}

package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.response.UploadResp;
import com.braingroom.user.utils.FileUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

public class ImageUploadViewModel extends ViewModel {


    public final ObservableInt placeHolder = new ObservableInt();
    public final ObservableField<String> remoteAddress = new ObservableField<>("");
    public final Action onUploadClicked;
    MessageHelper messageHelper;

    public ImageUploadViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, int placeholder, String remoteAddress) {
        this.messageHelper = messageHelper;
        this.placeHolder.set(placeholder);
        this.remoteAddress.set(remoteAddress);

        onUploadClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.launchImageChooserActivity(REQ_CODE_CHOOSE_IMAGE);
            }
        };

    }


    @Override
    public void handleActivityResult(final int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQ_CODE_CHOOSE_IMAGE)
                && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            messageHelper.show("uploading...");
            apiService.uploadImage(FileUtils.getPath(UserApplication.getInstance(), fileUri), UserApplication.getInstance().getContentResolver().getType(fileUri))
                    .subscribe(new Consumer<UploadResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull UploadResp resp) throws Exception {
                            if (resp.getResCode().equals("1")) {
                                messageHelper.show("image upload success");
                                remoteAddress.set(resp.getData().get(0).getImgPath());
                            } else {
                                messageHelper.show("image upload FAIlURE");
                            }

                        }

                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            messageHelper.show("image upload FAIlURE");
                        }
                    });

        }
    }

}

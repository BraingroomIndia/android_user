package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.response.UploadPostApiResp;
import com.braingroom.user.model.response.UploadResp;
import com.braingroom.user.utils.FileUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

public class PostApiImageUploadViewModel extends ViewModel {


    public final ObservableField<Integer> placeHolder = new ObservableField<>();
    public final ObservableField<String> remoteAddress = new ObservableField<>("");
    public final Action onUploadClicked;
    MessageHelper messageHelper;
    private String TAG = getClass().getCanonicalName();

    public PostApiImageUploadViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, int placeholder, String remoteAddress) {
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
            Log.d(TAG, "fileuri: "+fileUri);
            messageHelper.show("uploading...");
            apiService.uploadPostApiImage(
                    FileUtils.getPath(UserApplication.getInstance(), fileUri)
                    , UserApplication.getInstance().getContentResolver().getType(fileUri)
                    , "tips_tricks"
            )
                    .subscribe(new Consumer<UploadPostApiResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull UploadPostApiResp resp) throws Exception {
                            if (resp.getResCode().equals("1")) {
                                messageHelper.show("image upload success");
                                remoteAddress.set(resp.getData().get(0).getUrl());
                                Log.d(TAG, "getImgPath: "+resp.getData());
                                Log.d(TAG, "getImgPath: "+resp.getData().get(0).getUrl());
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

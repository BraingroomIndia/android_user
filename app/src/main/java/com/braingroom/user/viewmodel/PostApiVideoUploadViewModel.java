package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.response.UploadPostApiResp;
import com.braingroom.user.utils.FileUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class PostApiVideoUploadViewModel extends ViewModel {


    public final ObservableInt placeHolder = new ObservableInt();
    public final ObservableField<String> remoteAddress = new ObservableField<>("");
    public final ObservableField<String> thumbUrl = new ObservableField<>(null);
    public final Action onUploadClicked;
    MessageHelper messageHelper;
    private String TAG = getClass().getCanonicalName();

    public PostApiVideoUploadViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, int placeholder, String remoteAddress) {
        this.messageHelper = messageHelper;
        this.placeHolder.set(placeholder);
        this.remoteAddress.set(remoteAddress);

        onUploadClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.launchVideoChooserActivity(REQ_CODE_CHOOSE_VIDEO);
            }
        };

    }


    public void uploadVideo(Uri fileUri, String postType) {
        messageHelper.showProgressDialog("Upload", "Please wait while we upload the file to server");
        String filePath = FileUtils.getPath(fileUri);
        String fileType = FileUtils.getMimeType(fileUri);
        if (filePath == null || fileType == null) {
            messageHelper.showDismissInfo("", "Sorry we are unable to upload the file");
            return;
        }
        apiService.uploadPostApiVideo(filePath, fileType, postType)
                .subscribe(new Consumer<UploadPostApiResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull UploadPostApiResp resp) throws Exception {
                        if (resp.getData() != null && !resp.getData().isEmpty()) {
                            messageHelper.show("video upload success");
                            remoteAddress.set(resp.getData().get(0).getUrl());
                            thumbUrl.set(resp.getData().get(0).getThumb());
                        } else {
                            messageHelper.show("video upload FAIlURE");
                        }

                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        messageHelper.show("video upload FAIlURE");
                        Log.d("Video upload", "accept: ");
                        throwable.printStackTrace();
                    }
                });

    }


}



package com.braingroom.user.viewmodel;

import android.content.ContentResolver;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.response.UploadPostApiResp;
import com.braingroom.user.utils.FileUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

public class PostApiImageUploadViewModel extends ViewModel {


    public final ObservableInt placeHolder = new ObservableInt();
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

    public void imageUpload(Uri fileUri, String postType) {
        messageHelper.showProgressDialog("Upload", "Please wait while we upload the file to server");
        String filePath = FileUtils.getPath(fileUri);
        String fileType = FileUtils.getMimeType(fileUri);
        if (filePath == null || fileType == null) {
            messageHelper.show("Sorry we are unable to upload the file");
            Timber.tag(TAG).d("\nimageUpload: File Path" + filePath + "\nFile type" + fileType);
            return;
        }
        apiService.uploadPostApiImage(filePath, fileType, postType)
                .subscribe(new Consumer<UploadPostApiResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull UploadPostApiResp resp) throws Exception {
                        messageHelper.dismissActiveProgress();
                        if (resp.getResCode().equals("1")) {

                            messageHelper.show("image upload success");
                            remoteAddress.set(resp.getData().get(0).getUrl());
                            Timber.tag(TAG).d("getImgPath: " + resp.getData());
                            Timber.tag(TAG).d("getImgPath: " + resp.getData().get(0).getUrl());
                        } else {
                            messageHelper.show("image upload FAIlURE");
                        }

                    }

                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        messageHelper.dismissActiveProgress();
                        messageHelper.show("image upload FAIlURE");
                        Timber.tag(TAG).e(throwable, "Image upload");
                        throwable.printStackTrace();
                    }
                });


    }

    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = UserApplication.getInstance().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

}

package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.response.UploadResp;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.utils.FileUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;
import static com.braingroom.user.utils.Constants.defaultProfilePic;

public class ImageUploadViewModel extends ViewModel {


    public final ObservableInt placeHolder = new ObservableInt();
    public final ObservableField<String> remoteAddress = new ObservableField<>("");
    public final Action onUploadClicked;
    MessageHelper messageHelper;
    public final int[] profilePicResArray = new int[]{
            R.drawable.man, //Edited By Vikas Godara
            R.drawable.man_1,
            R.drawable.man_2,
            R.drawable.man_3, //Edited By Vikas Godara
            R.drawable.man_4,
            R.drawable.man_5,
            R.drawable.woman,
            R.drawable.woman_1,
            R.drawable.woman_2,
            R.drawable.woman_3,
            R.drawable.woman_4,
            R.drawable.woman_5,

    };

    public ImageUploadViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, int placeholder, String remoteAddress) {
        this.messageHelper = messageHelper;
        if (placeholder == R.drawable.avatar_male)
            this.placeHolder.set(profilePicResArray[CommonUtils.randInt(0, profilePicResArray.length)]);
        else
            this.placeHolder.set(placeholder);
        this.remoteAddress.set(!defaultProfilePic.equalsIgnoreCase(remoteAddress) ? remoteAddress : "");

        onUploadClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.launchImageChooserActivity(REQ_CODE_CHOOSE_IMAGE);
            }
        };

    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress.set(!defaultProfilePic.equalsIgnoreCase(remoteAddress) ? remoteAddress : "");
    }

    public ImageUploadViewModel(int placeholder, String remoteAddress) {
        if (placeholder == R.drawable.avatar_male)
            this.placeHolder.set(profilePicResArray[CommonUtils.randInt(0, profilePicResArray.length)]);
        else
            this.placeHolder.set(placeholder);
        this.remoteAddress.set(!defaultProfilePic.equalsIgnoreCase(remoteAddress) ? remoteAddress : "");

        onUploadClicked = null;

    }


    @Override
    public void handleActivityResult(final int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQ_CODE_CHOOSE_IMAGE)
                && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            String filePath = FileUtils.getPath(fileUri);
            String fileType = FileUtils.getMimeType(fileUri);
            if (filePath == null || fileType == null) {
                messageHelper.showDismissInfo("", "Unable to upload");
                return;
            }
            messageHelper.show("uploading...");
            apiService.uploadImage(filePath, fileType)
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

package com.braingroom.user.viewmodel.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ProfileDisplayActivity;
import com.braingroom.user.view.activity.ThirdPartyViewActivity;
import com.braingroom.user.viewmodel.ImageUploadViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.functions.Action;

public class LikedItemViewModel extends ViewModel {

    public final Action showthirdpartyProfile;
    public final String userImage, userName;
    public final int placeHolder = R.drawable.avatar_male;
    public final ImageUploadViewModel imageUploadViewModel;

    public LikedItemViewModel(String userImage, String userName, final String userId, @NonNull final Navigator navigator) {

        this.userImage = userImage;
        this.userName = userName;
        imageUploadViewModel = new ImageUploadViewModel(placeHolder, userImage);
        showthirdpartyProfile = new Action() {
            @Override
            public void run() throws Exception {
                Bundle bundleData = new Bundle();

                if (!userId.equalsIgnoreCase(BG_ID)) {
                    bundleData.putString("userId", userId);
                    navigator.navigateActivity(ThirdPartyViewActivity.class, bundleData);
                } else {
                    navigator.navigateActivity(ProfileDisplayActivity.class, null);
                }

            }
        };
    }

}

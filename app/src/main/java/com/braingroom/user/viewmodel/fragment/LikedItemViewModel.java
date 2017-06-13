package com.braingroom.user.viewmodel.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ThirdPartyViewActivity;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.functions.Action;

public class LikedItemViewModel extends ViewModel {

    public final Action showthirdpartyProfile;
    public final String userImage, userName;

    public LikedItemViewModel(String userImage, String userName, final String userId, @NonNull final Navigator navigator) {

        this.userImage = userImage;
        this.userName = userName;
        showthirdpartyProfile = new Action() {
            @Override
            public void run() throws Exception {
                Bundle bundleData = new Bundle();

                bundleData.putString("userId", userId);
                navigator.navigateActivity(ThirdPartyViewActivity.class, bundleData);

            }
        };
    }

}

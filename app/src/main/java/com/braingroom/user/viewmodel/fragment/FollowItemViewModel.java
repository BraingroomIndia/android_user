package com.braingroom.user.viewmodel.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ThirdPartyViewActivity;
import com.braingroom.user.viewmodel.FollowButtonViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.functions.Action;

public class FollowItemViewModel extends ViewModel {

    public final Action showthirdpartyProfile;
    public final String userImage, userName;
    public final int placeHolder = R.drawable.avatar_male;

    public final FollowButtonViewModel followButtonVm;

    public FollowItemViewModel(String userImage, String userName, final String userId, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator
            , @NonNull int followStatus) {
        followButtonVm = new FollowButtonViewModel(userId, messageHelper, navigator, followStatus == 0 ? FollowButtonViewModel.STATE_FOLLOW : FollowButtonViewModel.STATE_FOLLOWED);
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

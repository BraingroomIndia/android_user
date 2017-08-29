package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.FollowedUserViewModel;
import com.braingroom.user.viewmodel.fragment.FollowingUserViewModel;

/**
 * Created by godara on 26/08/17.
 */

public class FollowingUserFragment extends BaseFragment {
    public static FollowingUserFragment newInstance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID, userId);
        FollowingUserFragment fragment = new FollowingUserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new FollowingUserViewModel(((ConnectUiHelper) activity), activity.getNavigator(),getStringArguments(Constants.USER_ID));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_following_user;
    }
}

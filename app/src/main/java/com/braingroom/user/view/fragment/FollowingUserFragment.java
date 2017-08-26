package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.FollowedUserViewModel;

/**
 * Created by godara on 26/08/17.
 */

public class FollowingUserFragment extends BaseFragment {
    public static FollowingUserFragment newInstance() {
        return new FollowingUserFragment();

    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {

        return new FollowedUserViewModel(((ConnectUiHelper) activity), activity.getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_following_user;
    }
}

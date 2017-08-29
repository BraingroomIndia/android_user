package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.FollowedUserViewModel;

/**
 * Created by godara on 26/08/17.
 */

public class FollowedUserFragment extends BaseFragment {

    public static FollowedUserFragment newInstance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID, userId);
        FollowedUserFragment fragment = new FollowedUserFragment();
        fragment.setArguments(bundle);
        return fragment;

    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        String userId = getStringArguments(Constants.USER_ID);
        return new FollowedUserViewModel(((ConnectUiHelper) activity), activity.getMessageHelper(), activity.getNavigator()
                , activity.getHelperFactory(), userId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_followed_user;
    }
}

package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.FollowersUserViewModel;

/**
 * Created by godara on 26/08/17.
 */

public class FollowersUserFragment extends BaseFragment {
    public static FollowersUserFragment newInstance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.USER_ID, userId);
        FollowersUserFragment fragment = new FollowersUserFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new FollowersUserViewModel(((ConnectUiHelper) activity), activity.getMessageHelper()
                , activity.getNavigator(), activity.getHelperFactory(), getStringArguments(Constants.USER_ID));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_following_user;
    }
}

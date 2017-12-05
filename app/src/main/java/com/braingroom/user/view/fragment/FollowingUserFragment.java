package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.braingroom.user.R;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.viewmodel.ViewModel;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        String userId = getStringArguments(Constants.USER_ID);
        return new FollowingUserViewModel(((ConnectUiHelper) activity), activity.getMessageHelper(), activity.getNavigator()
                , activity.getHelperFactory(), userId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_followed_user;
    }
}

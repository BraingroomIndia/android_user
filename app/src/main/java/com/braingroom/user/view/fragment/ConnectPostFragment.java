package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ConnectPostViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class ConnectPostFragment extends BaseFragment {

    private static final String POST_TYPE = "POST_TYPE";

    public static ConnectPostFragment newInstance() {
        return new ConnectPostFragment();
    }

    public static Fragment newInstanceByPostType(String postType) {
        ConnectPostFragment mFragment = new ConnectPostFragment();
        Bundle args = new Bundle();
        args.putString(POST_TYPE,postType);
        mFragment.setArguments(args);
        return mFragment;
    }

    public interface UiHelper {
        void next();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        Bundle args = getArguments();
        String postType = null;
        if(args!=null){
            postType = args.getString(POST_TYPE);
        }
        return new ConnectPostViewModel(activity.getMessageHelper(), activity.getNavigator(), activity.getHelperFactory(), postType);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_post;
    }
}

package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ConnectPostViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class ConnectPostFragment extends BaseFragment {

    public static ConnectPostFragment newInstance() {
        return new ConnectPostFragment();
    }

    public interface UiHelper {
        void next();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ConnectPostViewModel(activity.getMessageHelper(), activity.getNavigator(), activity.getHelperFactory());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_post;
    }
}

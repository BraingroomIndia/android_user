package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class ConnectFilterFragment extends BaseFragment {

    public static ConnectFilterFragment newInstance() {
        return new ConnectFilterFragment();
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return ((ConnectHomeActivity) activity).getConnectFilterViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_connect_filter;
    }
}

package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.viewmodel.ClassDetailViewModel;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by godara on 22/06/17.
 */

public class ClassQueryFragment extends BaseFragment {
    public static ClassQueryFragment newInstance() {
        return new ClassQueryFragment();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return ((ClassDetailActivity) activity).getViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class_query;
    }
}

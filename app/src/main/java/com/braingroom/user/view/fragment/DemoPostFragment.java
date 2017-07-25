package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ClassDetailDemoPostViewModel;

/**
 * Created by godara on 24/07/17.
 */

public class DemoPostFragment extends BaseFragment {
    public static DemoPostFragment newInstance(ConnectFilterData filterData){
        Bundle bundle =new Bundle();
        bundle.putSerializable("connectFilterData",filterData);
        DemoPostFragment fragment = new DemoPostFragment();
        fragment.setArguments(bundle);
        return fragment;

    }
    @NonNull
    @Override
    protected ViewModel createViewModel() {
        ConnectFilterData connectFilterData = (ConnectFilterData) getSerializableArguments("connectFilterData");
        return new ClassDetailDemoPostViewModel(activity.getNavigator(),connectFilterData);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.demo_post_fragment;
    }
}

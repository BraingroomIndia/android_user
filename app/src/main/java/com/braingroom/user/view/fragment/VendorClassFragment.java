package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.VendorClassViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class VendorClassFragment extends BaseFragment {

    public static VendorClassFragment newInstance() {
        return new VendorClassFragment();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new VendorClassViewModel(activity.getNavigator(), activity.getIntentString("id"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vendor_class_list;
    }
}

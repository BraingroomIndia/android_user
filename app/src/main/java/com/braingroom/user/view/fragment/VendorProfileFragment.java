package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.VendorProfileViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class VendorProfileFragment extends BaseFragment {

    public static VendorProfileFragment newInstance() {
        return new VendorProfileFragment();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new VendorProfileViewModel(activity.getNavigator(), activity.getIntentString("id"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vendor_profile;
    }
}

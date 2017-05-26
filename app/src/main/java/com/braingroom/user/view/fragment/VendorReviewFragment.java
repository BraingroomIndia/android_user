package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.VendorReviewsViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class VendorReviewFragment extends BaseFragment {

    public static VendorReviewFragment newInstance() {
        return new VendorReviewFragment();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new VendorReviewsViewModel(activity.getMessageHelper(), activity.getIntentString("id"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_vendor_review_list;
    }
}

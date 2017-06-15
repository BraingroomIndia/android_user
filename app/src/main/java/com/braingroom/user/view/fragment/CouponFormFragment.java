package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.CouponFormActivity;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class CouponFormFragment extends BaseFragment {

    public static CouponFormFragment newInstance(int i) {
        CouponFormFragment mFragment = new CouponFormFragment();
        Bundle args = new Bundle();
        args.putInt("index", i);
        mFragment.setArguments(args);
        return mFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getRoot().requestFocus();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        Bundle args = getArguments();
        int index = args.getInt("index");
        return ((CouponFormActivity) activity).getViewmodel(index);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_coupons_form;
    }
}

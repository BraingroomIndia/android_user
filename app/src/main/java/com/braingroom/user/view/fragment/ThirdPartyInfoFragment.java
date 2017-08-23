package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.ThirdPartyViewActivity;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class ThirdPartyInfoFragment extends BaseFragment {

    public static ThirdPartyInfoFragment newInstance() {
        return new ThirdPartyInfoFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getRoot().requestFocus();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return ((ThirdPartyViewActivity) activity).getViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_thirdparty_profile_1;
    }
}

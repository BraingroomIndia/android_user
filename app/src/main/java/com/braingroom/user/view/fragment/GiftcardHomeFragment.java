package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.GiftcardCouponActivity;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class GiftcardHomeFragment extends BaseFragment {

    public static GiftcardHomeFragment newInstance() {
        return new GiftcardHomeFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.getRoot().requestFocus();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return ((GiftcardCouponActivity) activity).getViewmodel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_giftcards_home;
    }
}

package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.FeaturedPostActivity;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by godara on 13/09/17.
 */

public class FeaturedPostFragment extends BaseFragment {

    public static FeaturedPostFragment newInstance(int i) {
        FeaturedPostFragment mFragment = new FeaturedPostFragment();
        Bundle args = new Bundle();
        args.putInt("index", i);
        mFragment.setArguments(args);
        return mFragment;
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        Bundle args = getArguments();
        int index = args.getInt("index");
        return ((FeaturedPostActivity) activity).getFeaturePostItemViewModel(index);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_featured_post;
    }
}

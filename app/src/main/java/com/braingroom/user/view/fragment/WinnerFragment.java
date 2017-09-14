package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.FeaturedPostActivity;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by godara on 14/09/17.
 */

public class WinnerFragment extends BaseFragment {

    public static WinnerFragment newInstance(int i) {
        WinnerFragment mFragment = new WinnerFragment();
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
        return ((FeaturedPostActivity) activity).getWinnersViewModel(index);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_winner;
    }
}

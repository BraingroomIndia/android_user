package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.SignupActivity;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by godara on 01/07/17.
 */

public class Signup3Fragment extends BaseFragment {

    public static Signup3Fragment newInstance() {
        return new Signup3Fragment();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return ((SignupActivity) activity).getViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signup_3;
    }
}

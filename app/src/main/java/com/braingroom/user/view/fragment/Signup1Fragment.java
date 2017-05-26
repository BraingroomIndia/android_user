package com.braingroom.user.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.model.request.SignUpReq;
import com.braingroom.user.view.activity.SignupActivity;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class Signup1Fragment extends BaseFragment {

    public static Signup1Fragment newInstance() {
        return new Signup1Fragment();
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return ((SignupActivity)activity).getViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signup_1;
    }
}

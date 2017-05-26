package com.braingroom.user.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.model.request.SignUpReq;
import com.braingroom.user.view.activity.SignupActivity;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by agrahari on 07/04/17.
 */

public class Signup2Fragment extends BaseFragment {


    public static Signup2Fragment newInstance() {
        return new Signup2Fragment();
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {

        return ((SignupActivity) activity).getViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_signup_2;
    }
}

package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.SignUpActivityCompetition;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by himan on 6/7/2017.
 */

public class SignUpCompetition2Fragment extends BaseFragment {
    public static SignUpCompetition2Fragment newInstance() {
        return new SignUpCompetition2Fragment();
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return ((SignUpActivityCompetition)activity).getViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fargment_signup_competition_2;
    }
}

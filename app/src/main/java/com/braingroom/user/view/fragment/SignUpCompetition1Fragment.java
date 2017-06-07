package com.braingroom.user.view.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.SignUpActivityCompetition;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by himan on 6/7/2017.
 */

public class SignUpCompetition1Fragment extends BaseFragment {
    public static SignUpCompetition1Fragment newInstance() {
        return new SignUpCompetition1Fragment();
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return ((SignUpActivityCompetition)activity).getViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fargment_signup_competition_1;
    }
}

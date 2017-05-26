package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ChangePasswordViewModel;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by godara on 18/05/17.
 */

public class ChangePasswordActivity extends BaseActivity {
    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ChangePasswordViewModel(getMessageHelper(),getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_change_password;
    }
}

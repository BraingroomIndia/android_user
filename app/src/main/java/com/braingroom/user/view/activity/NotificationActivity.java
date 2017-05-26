package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.NotificationViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class NotificationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new NotificationViewModel(getHelperFactory(), getMessageHelper(), getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notification;
    }
}

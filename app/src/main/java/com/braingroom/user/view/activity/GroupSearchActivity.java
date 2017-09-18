package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.GroupSearchViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class GroupSearchActivity extends BaseActivity {

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new GroupSearchViewModel(getMessageHelper(), getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_group_search;
    }
}

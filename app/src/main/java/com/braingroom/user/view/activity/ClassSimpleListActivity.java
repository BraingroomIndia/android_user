package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ClassSimpleListViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class ClassSimpleListActivity extends BaseActivity {

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setElevation(0);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ClassSimpleListViewModel(getMessageHelper(), getNavigator(),getIntentString("listType"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_simplelist;
    }
}

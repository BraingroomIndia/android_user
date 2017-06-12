package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.CatalogueHomeViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class CatalogueHomeActivity extends BaseActivity {

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new CatalogueHomeViewModel(getMessageHelper(), getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_catalogue_home;
    }
}

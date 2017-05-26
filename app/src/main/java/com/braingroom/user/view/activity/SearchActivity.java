package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.SearchViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class SearchActivity extends BaseActivity {

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new SearchViewModel(getMessageHelper(),getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }
}

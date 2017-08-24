package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.ConnectSectionListViewModel;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by godara on 11/08/17.
 */

public class ConnectSectionListActivity extends BaseActivity {
    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ConnectSectionListViewModel(getNavigator());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_section_list;
    }


}

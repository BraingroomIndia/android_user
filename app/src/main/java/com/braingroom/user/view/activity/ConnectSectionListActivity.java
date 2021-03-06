package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.braingroom.user.R;

import com.braingroom.user.view.adapters.CustomGridLayoutManger;
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
        return new ConnectSectionListViewModel(getFirebaseAnalytics(), getGoogleTracker(), getNavigator(), new CustomGridLayoutManger(this, 5));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect_section_list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

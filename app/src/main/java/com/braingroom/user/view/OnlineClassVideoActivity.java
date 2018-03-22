package com.braingroom.user.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.BaseActivity;
import com.braingroom.user.viewmodel.OnlineClassVideoListViewModel;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by apple on 19/03/18.
 */

public class OnlineClassVideoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new OnlineClassVideoListViewModel(getNavigator(), getMessageHelper(), getIntentString("txn_id"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_online_class_list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

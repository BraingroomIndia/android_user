package com.braingroom.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.view.activity.BaseActivity;
import com.braingroom.user.viewmodel.OnlineClassVideoListViewModel;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by apple on 19/03/18.
 */

public class OnlineClassVideoActivity extends BaseActivity {
    public String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView className = (TextView) findViewById(R.id.my_online_classes);
        getSupportActionBar().setTitle("My Online Classes");
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        value = getIntentString("class_topic");
        className.setText(value);
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

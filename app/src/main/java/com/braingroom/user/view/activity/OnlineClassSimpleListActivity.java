package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.viewmodel.OnlineClassSimpleListViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class OnlineClassSimpleListActivity extends BaseActivity {

    @Override
    protected void onStart() {
        super.onStart();
        if (getActionBar() != null)
            getActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Online Classes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new OnlineClassSimpleListViewModel(getMessageHelper(), getNavigator(), getIntentString("listType"), null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_online_class_simple_list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}

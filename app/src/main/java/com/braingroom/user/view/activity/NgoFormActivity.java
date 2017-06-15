package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.NgoFormViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class NgoFormActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getIntentString("title"));
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        String giftcardId = getIntentString("giftcardId");
        return new NgoFormViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), giftcardId);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ngo_form;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ngo_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_apply) {
//            try {
//                viewmodel.submitClicked.run();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

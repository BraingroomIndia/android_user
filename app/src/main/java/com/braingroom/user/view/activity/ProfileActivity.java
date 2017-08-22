package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ProfileViewModel1;
import com.braingroom.user.viewmodel.ViewModel;

public class ProfileActivity extends BaseActivity {
    ActionBar actionBar;

    public interface UiHelper {
        void invalidateMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setElevation(0);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ProfileViewModel1(getMessageHelper(), getNavigator(), getHelperFactory(), new UiHelper() {
            @Override
            public void invalidateMenu() {
                invalidateOptionsMenu();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile_1;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_profile, menu);
        ((ProfileViewModel1) vm).handleMenuStates(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            ((ProfileViewModel1) vm).edit();
        }
        if (item.getItemId() == R.id.action_done) {
            ((ProfileViewModel1) vm).update();
            getNavigator().navigateActivity(ProfileActivity.class, null);
        }
        if (item.getItemId() == R.id.action_discard) {
            ((ProfileViewModel1) vm).revertData();
        }
        if (item.getItemId() == android.R.id.home) {
            getNavigator().navigateActivity(HomeActivity.class, null);
        }
        return true;
    }

}

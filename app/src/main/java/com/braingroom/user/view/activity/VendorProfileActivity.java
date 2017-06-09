package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.databinding.ActivityVendorProfileBinding;
import com.braingroom.user.view.fragment.VendorClassFragment;
import com.braingroom.user.view.fragment.VendorProfileFragment;
import com.braingroom.user.view.fragment.VendorReviewFragment;
import com.braingroom.user.viewmodel.ViewModel;

public class VendorProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ((ActivityVendorProfileBinding) binding).pager.setAdapter(new VendorPagerAdapter(getSupportFragmentManager()));
        ((ActivityVendorProfileBinding) binding).tabLayout.setupWithViewPager(((ActivityVendorProfileBinding) binding).pager);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ViewModel();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_vendor_profile;
    }

    public class VendorPagerAdapter extends FragmentStatePagerAdapter {
        public VendorPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) return VendorProfileFragment.newInstance();
            if (i == 1) return VendorClassFragment.newInstance();
            if (i == 2) return VendorReviewFragment.newInstance();
            return VendorClassFragment.newInstance();

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "Profile";
            if (position == 1) return "Classes";
            if (position == 2) return "Reviews";
            return "NO TAB";
        }
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

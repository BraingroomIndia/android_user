package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.braingroom.user.R;
import com.braingroom.user.view.fragment.ClassSimpleListFragment;
import com.braingroom.user.view.fragment.ConnectFeedFragment;
import com.braingroom.user.view.fragment.ThirdPartyInfoFragment;
import com.braingroom.user.viewmodel.ThirdPartyViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import lombok.Getter;


public class ThirdPartyViewActivity extends BaseActivity {

    private String TAG = getClass().getCanonicalName();

    ViewPager pager;
    public PagerAdapter pagerAdapter;

    @Getter
    ThirdPartyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Profile");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        viewModel = new ThirdPartyViewModel(getIntentString("userId"), getMessageHelper(), getNavigator());
        return viewModel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_third_party;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.thirdparty_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_find) {
//            getNavigator().navigateActivity(HomeActivity.class, null);
//            getNavigator().finishActivity();
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) return ThirdPartyInfoFragment.newInstance();
            if (i == 1) return ClassSimpleListFragment.newInstance();
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "General Info";
            if (position == 1) return "Classes";
            return "NO TAB";
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public ConnectFeedFragment getFragmentAt(int position) {
            return (ConnectFeedFragment) registeredFragments.get(position);
        }


    }


}
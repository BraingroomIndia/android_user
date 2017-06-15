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
import com.braingroom.user.view.fragment.ConnectFeedFragment;
import com.braingroom.user.view.fragment.CouponFormFragment;
import com.braingroom.user.viewmodel.CouponFormDataViewModel;
import com.braingroom.user.viewmodel.CouponFormViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.functions.Action;

public class CouponFormActivity extends BaseActivity {

    ViewPager pager;
    public PagerAdapter pagerAdapter;
    public CouponFormViewModel viewmodel;

    public CouponFormDataViewModel getViewmodel(int i) {
        return viewmodel.formDataList.get(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("Fill up Form");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager, true);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        viewmodel = new CouponFormViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), new Action() {
            @Override
            public void run() throws Exception {
                pagerAdapter.notifyDataSetChanged();
            }
        });
        return viewmodel;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_coupon_form;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return CouponFormFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return viewmodel.formDataList.size();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_coupon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_add) {
            try {
                viewmodel.addNewFormData();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        if (id == R.id.action_apply) {
            try {
                viewmodel.submitClicked.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

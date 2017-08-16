package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.braingroom.user.R;
import com.braingroom.user.view.fragment.BaseFragment;
import com.braingroom.user.view.fragment.CouponHomeFragment;
import com.braingroom.user.view.fragment.GiftcardHomeFragment;
import com.braingroom.user.viewmodel.GiftCouponViewModel;
import com.braingroom.user.viewmodel.ViewModel;

public class GiftcardCouponActivity extends BaseActivity {

    ViewPager pager;
    public PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setElevation(0);
//        getSupportActionBar().setTitle("Profile");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new GiftCouponViewModel(getMessageHelper(), getNavigator());
    }

    public GiftCouponViewModel getViewmodel() {
        return (GiftCouponViewModel) vm;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_giftcard_coupon;
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) return GiftcardHomeFragment.newInstance();
            if (i == 1) return CouponHomeFragment.newInstance();
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "Gift Cards";
            if (position == 1) return "Coupons";
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

        public BaseFragment getFragmentAt(int position) {
            return (BaseFragment) registeredFragments.get(position);
        }


    }
}

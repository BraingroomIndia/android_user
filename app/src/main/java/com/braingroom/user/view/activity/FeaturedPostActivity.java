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
import com.braingroom.user.utils.WrapContentHeightViewPager;
import com.braingroom.user.view.fragment.BaseFragment;
import com.braingroom.user.view.fragment.FeaturedPostFragment;
import com.braingroom.user.view.fragment.WinnerFragment;
import com.braingroom.user.viewmodel.FeaturedPostViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.FeaturedPostItemViewModel;
import com.braingroom.user.viewmodel.fragment.WinnersViewModel;

/**
 * Created by godara on 13/09/17.
 */

public class FeaturedPostActivity extends BaseActivity {


    FeaturedPostViewModel viewModel;

    WrapContentHeightViewPager postPager;
    PagerAdapter postPagerAdapter;
    TabLayout postTabLayout;
    private boolean setPostPagerAdapter = false;
    WrapContentHeightViewPager winnerPager;
    PagerAdapter winnerPagerAdapter;
    TabLayout winnerTabLayout;
    private boolean setWinnerPagerAdapter = false;

    public interface UiHelper {
        void setPostPagerAdapter();

        void setWinnerPagerAdapter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        postTabLayout = findViewById(R.id.post_tab_layout);
        postPager = findViewById(R.id.post_pager);
        winnerTabLayout = findViewById(R.id.winner_tab_layout);
        winnerPager = findViewById(R.id.winner_pager);
        if (setPostPagerAdapter) {
            postPagerAdapter = new PagerAdapter(getSupportFragmentManager(), FeaturedPostFragment.class);
            postPager.setAdapter(postPagerAdapter);
            postTabLayout.setupWithViewPager(postPager, true);
        }
        winnerTabLayout = findViewById(R.id.winner_tab_layout);
        winnerPager = findViewById(R.id.winner_pager);
        if (setWinnerPagerAdapter) {
            winnerPagerAdapter = new PagerAdapter(getSupportFragmentManager(), WinnerFragment.class);
            winnerPager.setAdapter(winnerPagerAdapter);
            winnerTabLayout.setupWithViewPager(winnerPager, true);
        }
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {

        viewModel = new FeaturedPostViewModel(getNavigator(), new UiHelper() {
            @Override
            public void setPostPagerAdapter() {
                if (postTabLayout != null && postPager != null) {
                    postPagerAdapter = new PagerAdapter(getSupportFragmentManager(), FeaturedPostFragment.class);
                    postPager.setAdapter(postPagerAdapter);
                    postTabLayout.setupWithViewPager(postPager, true);
                } else setPostPagerAdapter = true;
            }

            @Override
            public void setWinnerPagerAdapter() {
                if (winnerTabLayout != null && winnerPager != null) {
                    winnerPagerAdapter = new PagerAdapter(getSupportFragmentManager(), WinnerFragment.class);
                    winnerPager.setAdapter(winnerPagerAdapter);
                    winnerTabLayout.setupWithViewPager(winnerPager, true);
                } else setWinnerPagerAdapter = true;
            }
        });
        return viewModel;
    }

    public FeaturedPostItemViewModel getFeaturePostItemViewModel(int i) {
        return viewModel.featuredPostItemViewModelList.get(i);
    }

    public WinnersViewModel getWinnersViewModel(int i) {
        return viewModel.winnersViewModelList.get(i);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_featured_post;
    }


    private class PagerAdapter extends FragmentStatePagerAdapter {

        Class<?> fragment;

        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public PagerAdapter(FragmentManager fm, Class<?> fragment) {

            super(fm);
            this.fragment = fragment;
        }

        @Override
        public Fragment getItem(int i) {

            if (fragment == FeaturedPostFragment.class)
                return FeaturedPostFragment.newInstance(i);
            else return WinnerFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            if (fragment == FeaturedPostFragment.class)
                return viewModel.featuredPostItemViewModelList.size();
            else
                return viewModel.winnersViewModelList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BaseFragment fragment = (BaseFragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }
    }
}

package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.ViewGroup;
import android.widget.TextView;

import com.braingroom.user.R;
import com.braingroom.user.utils.WrapContentHeightViewPager;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.fragment.BaseFragment;
import com.braingroom.user.view.fragment.CommentFragment;
import com.braingroom.user.view.fragment.FeaturedPostFragment;
import com.braingroom.user.view.fragment.LikesFragment;
import com.braingroom.user.view.fragment.ReplyFragment;
import com.braingroom.user.view.fragment.WinnerFragment;
import com.braingroom.user.viewmodel.ConnectFeedItemViewModel;
import com.braingroom.user.viewmodel.FeaturedPostViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.FeaturedPostItemViewModel;
import com.braingroom.user.viewmodel.fragment.WinnersViewModel;

import java.util.List;

/**
 * Created by godara on 13/09/17.
 */

public class FeaturedPostActivity extends BaseActivity implements ConnectUiHelper {


    FeaturedPostViewModel viewModel;


    WrapContentHeightViewPager postPager;
    PagerAdapter postPagerAdapter;
    TabLayout postTabLayout;
    TextView postDate;
    TextView headingText;
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
        headingText = findViewById(R.id.post_heading);
        postDate = findViewById(R.id.post_date);
        postTabLayout = findViewById(R.id.post_tab_layout);
        postPager = findViewById(R.id.post_pager);
       /* winnerTabLayout = findViewById(R.id.winner_tab_layout);
        winnerPager = findViewById(R.id.winner_pager);*/
        if (setPostPagerAdapter) {
            postPagerAdapter = new PagerAdapter(getSupportFragmentManager(), FeaturedPostFragment.class);
            postPager.setAdapter(postPagerAdapter);
            postTabLayout.setupWithViewPager(postPager, true);
            headingText.setText("Post of the day");
            if (getFeaturePostItemViewModel(0) != null)
                postDate.setText(getFeaturePostItemViewModel(0).smallDate);
            postTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int position = tab.getPosition();
                    if (getFeaturePostItemViewModel(position) != null)
                        postDate.setText(getFeaturePostItemViewModel(position).smallDate);
                    headingText.setText("Post of the day");

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
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

        viewModel = new FeaturedPostViewModel(getNavigator(), getHelperFactory(), getMessageHelper(), new UiHelper() {
            @Override
            public void setPostPagerAdapter() {
                if (postTabLayout != null && postPager != null) {
                    postPagerAdapter = new PagerAdapter(getSupportFragmentManager(), FeaturedPostFragment.class);
                    postPager.setAdapter(postPagerAdapter);
                    postTabLayout.setupWithViewPager(postPager, true);
                    if (getFeaturePostItemViewModel(0) != null)
                        postDate.setText(getFeaturePostItemViewModel(0).smallDate);
                    postTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            int position = tab.getPosition();
                            if (getFeaturePostItemViewModel(position) != null)
                                postDate.setText(getFeaturePostItemViewModel(position).smallDate);
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
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
        }, this);
        return viewModel;
    }

    @Nullable
    public ConnectFeedItemViewModel getFeaturePostItemViewModel(int i) {
        try {
            return viewModel.connectFeedItemViewModelList.get(i);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Nullable
    public WinnersViewModel getWinnersViewModel(int i) {
        try {
            return viewModel.winnersViewModelList.get(i);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

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
                return viewModel.connectFeedItemViewModelList.size();
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


    //Connect ui Helper
    @Override
    public void openCommentsFragment(String postId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, CommentFragment.newInstance(postId)).addToBackStack(null).commit();
    }

    @Override
    public void openReplyFragment(String postId, String commentId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, ReplyFragment.newInstance(postId, commentId)).addToBackStack(null).commit();
    }

    @Override
    public void openLikesFragment(String postId, String commentId, String replyId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, LikesFragment.newInstance(postId, commentId, replyId)).addToBackStack(null).commit();

    }

    @Override
    public void openAcceptedUsersFragment(String postId) {

    }

    @Override
    public void setFilterData(String keyword, String categoryId, String segmentId, String myGroupId, String allGroupId, String instituteId, String authorId, List<String> location) {

    }

    @Override
    public void setSearchQuery(String searchQuery) {

    }

    @Override
    public void openConnectPost() {

    }

    @Override
    public void popFragment() {
        popBackstack();
    }

    @Override
    public void setCount(int notificationCount, int messageCount) {

    }

    @Override
    public void retry() {

    }

    @Override
    public void openFollowers() {

    }

    @Override
    public void openFilter() {

    }

    @Override
    public void openFollowing() {

    }
    //Connect Ui helper
}

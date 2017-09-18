package com.braingroom.user.view.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.fragment.BaseFragment;
import com.braingroom.user.view.fragment.ClassSimpleListFragment;
import com.braingroom.user.view.fragment.CommentFragment;
import com.braingroom.user.view.fragment.FollowingUserFragment;
import com.braingroom.user.view.fragment.FollowersUserFragment;
import com.braingroom.user.view.fragment.LikesFragment;
import com.braingroom.user.view.fragment.PostAcceptFragment;
import com.braingroom.user.view.fragment.ReplyFragment;
import com.braingroom.user.view.fragment.ThirdPartyInfoFragment;
import com.braingroom.user.viewmodel.ProfileViewModel1;
import com.braingroom.user.viewmodel.ThirdPartyViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.List;

import lombok.Getter;

import static com.braingroom.user.view.activity.ConnectHomeActivity.BUY_SELL;
import static com.braingroom.user.view.activity.ConnectHomeActivity.DISCUSS_DECIDE;
import static com.braingroom.user.view.activity.ConnectHomeActivity.FIND_PARTNERS;
import static com.braingroom.user.view.activity.ConnectHomeActivity.LEARNER_FORUM;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TIPS_TRICKS;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TUTORS_ARTICLE;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TUTORS_TALK;


public class ThirdPartyViewActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectUiHelper {

    private String TAG = getClass().getCanonicalName();
    public String userid;

    ViewPager pager;
    public PagerAdapter pagerAdapter;

    @Getter
    ThirdPartyViewModel viewModel;


    public BottomNavigationView mBottomNav;


    int[][] states = new int[][]{
            new int[]{android.R.attr.state_checked}, // enabled
            new int[]{-android.R.attr.state_checked}
    };
    ColorStateList greenList, blueList;
    @IdRes
    int selectedBottomNav = R.id.action_tips_tricks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().setElevation(0);
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
        userid = getIntentString("userId");
        viewModel = new ThirdPartyViewModel(userid, getMessageHelper(), getNavigator(), getHelperFactory(), this);
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
        if (id == R.id.action_messages) {

            if (!vm.getLoggedIn()) {
                Bundle data = new Bundle();
                data.putString("backStackActivity", ThirdPartyViewActivity.class.getSimpleName());
                data.putString("thirdPartyUserId", userid);
                getMessageHelper().showLoginRequireDialog("Only logged in users can send a message", data);
                return true;
            }

            String userName = ((ThirdPartyViewModel) vm).name.s_1.get();
            Bundle bundle = new Bundle();
            bundle.putString("sender_id", userid);
            bundle.putString("sender_name", userName);
            getNavigator().navigateActivity(MessagesThreadActivity.class, bundle);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    @Override
    public void openCommentsFragment(String postId) {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, CommentFragment.newInstance(postId)).addToBackStack(null).commit();
    }

    @Override
    public void openReplyFragment(String postId, String commentId) {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, ReplyFragment.newInstance(postId, commentId)).addToBackStack(null).commit();
    }

    @Override
    public void openLikesFragment(String postId, String commentId, String replyId) {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, LikesFragment.newInstance(postId, commentId, replyId)).addToBackStack(null).commit();
    }

    @Override
    public void openAcceptedUsersFragment(String postId) {
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, PostAcceptFragment.newInstance(postId)).addToBackStack(null).commit();

    }

    @Override
    public void openConnectPost() {
    }


    @Override
    public void setFilterData(String keyword, String categoryId, String segmentId, String myGroupId, String allGroupId, String instituteId, String authorId, List<String> location) {

    }

    @Override
    public void setSearchQuery(String searchQuery) {

    }


    @Override
    public void popFragment() {

    }

    @Override
    public void setCount(int notificationCount, int messageCount) {

    }

    @Override
    public void retry() {
    }

    @Override
    public void openFollowers() {
       /* if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, FollowersUserFragment.newInstance(userid)).addToBackStack(null).commit();
*/
    }

    @Override
    public void openFollowing() {
       /* if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, FollowingUserFragment.newInstance(userid)).addToBackStack(null).commit();

*/
    }

    @Override
    public void openFilter() {


    }


    public class PagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) return ThirdPartyInfoFragment.newInstance();
            if (i == 1) return ClassSimpleListFragment.newInstance(userid);
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

        public BaseFragment getFragmentAt(int position) {
            return (BaseFragment) registeredFragments.get(position);
        }


    }

    private void updateBottomNavigation(int itemId) {

        ConnectFilterData connectFilterData = new ConnectFilterData();
        connectFilterData.setAuthorId(userid);
        if (itemId == R.id.action_tips_tricks) {

            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(TIPS_TRICKS);
            ((ProfileViewModel1) vm).setFilterData(connectFilterData);
        }
        if (itemId == R.id.action_buy_sell) {
            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(BUY_SELL);
            ((ProfileViewModel1) vm).setFilterData(connectFilterData);
        }
        if (itemId == R.id.action_find_partners) {
            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(FIND_PARTNERS);
            ((ProfileViewModel1) vm).setFilterData(connectFilterData);
        }

        if (itemId == R.id.action_discuss_n_decide) {
            connectFilterData.setMajorCateg(TUTORS_TALK);
            connectFilterData.setMinorCateg(DISCUSS_DECIDE);
            ((ProfileViewModel1) vm).setFilterData(connectFilterData);
        }
        if (itemId == R.id.action_tutors_article) {

            connectFilterData.setMajorCateg(TUTORS_TALK);
            connectFilterData.setMinorCateg(TUTORS_ARTICLE);
            ((ProfileViewModel1) vm).setFilterData(connectFilterData);
        }
    }


}

package com.braingroom.user.view.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.fragment.BaseFragment;
import com.braingroom.user.view.fragment.CommentFragment;
import com.braingroom.user.view.fragment.ConnectPostFragment;
import com.braingroom.user.view.fragment.FollowedUserFragment;
import com.braingroom.user.view.fragment.FollowingUserFragment;
import com.braingroom.user.view.fragment.LikesFragment;
import com.braingroom.user.view.fragment.PostAcceptFragment;
import com.braingroom.user.view.fragment.ReplyFragment;
import com.braingroom.user.viewmodel.ConnectHomeViewModel;
import com.braingroom.user.viewmodel.ProfileViewModel;
import com.braingroom.user.viewmodel.ProfileViewModel1;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.List;

import static com.braingroom.user.view.activity.ConnectHomeActivity.BUY_SELL;
import static com.braingroom.user.view.activity.ConnectHomeActivity.DISCUSS_DECIDE;
import static com.braingroom.user.view.activity.ConnectHomeActivity.FIND_PARTNERS;
import static com.braingroom.user.view.activity.ConnectHomeActivity.LEARNER_FORUM;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TIPS_TRICKS;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TUTORS_ARTICLE;
import static com.braingroom.user.view.activity.ConnectHomeActivity.TUTORS_TALK;

public class ProfileDisplayActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectUiHelper {
    ActionBar actionBar;


    public BottomNavigationView mBottomNav;


    int[][] states = new int[][]{
            new int[]{android.R.attr.state_checked}, // enabled
            new int[]{-android.R.attr.state_checked}
    };
    ColorStateList greenList, blueList;
    @IdRes
    int selectedBottomNav = R.id.action_tips_tricks;
    BaseFragment mFragement;

    @Override
    protected void onStart() {
        super.onStart();
        if (getSupportActionBar() != null)
            getSupportActionBar().setElevation(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        vm = (ProfileViewModel1) vm;
        super.onCreate(savedInstanceState);
        greenList = new ColorStateList(states, new int[]{
                ContextCompat.getColor(this, R.color.bottomNavSelectedGreen),
                ContextCompat.getColor(this, R.color.bottomNavUnSelected)
        });
        blueList = new ColorStateList(states, new int[]{
                ContextCompat.getColor(this, R.color.bottomNavSelectedBlue),
                ContextCompat.getColor(this, R.color.bottomNavUnSelected)
        });
        mBottomNav = findViewById(R.id.bottom_navigation);
        mBottomNav.inflateMenu(R.menu.bottom_nav_connect);
        if (TIPS_TRICKS.equals(((ProfileViewModel1) vm).getConnectFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_tips_tricks;
        if (BUY_SELL.equals(((ProfileViewModel1) vm).getConnectFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_buy_sell;
        if (FIND_PARTNERS.equals(((ProfileViewModel1) vm).getConnectFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_find_partners;
        if (TUTORS_ARTICLE.equals(((ProfileViewModel1) vm).getConnectFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_tutors_article;
        if (DISCUSS_DECIDE.equals(((ProfileViewModel1) vm).getConnectFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_discuss_n_decide;
        mBottomNav.setSelectedItemId(selectedBottomNav);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                updateBottomNavigation(itemId);
                return true;
            }
        });

    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ProfileViewModel1(getMessageHelper(), getNavigator(), getHelperFactory(), this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_profile_1;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, FollowingUserFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void openFollower() {

    }

    @Override
    public void openFollowed() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, FollowedUserFragment.newInstance()).addToBackStack(null).commit();

    }

    @Override
    public void openFilter() {


    }

    private void updateBottomNavigation(int itemId) {

        ConnectFilterData connectFilterData = new ConnectFilterData();
        connectFilterData.setAuthorId(pref.getString(Constants.BG_ID, ""));
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_profile, menu);
//        ((ProfileViewModel1) vm).handleMenuStates(menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_edit) {
//            ((ProfileViewModel1) vm).edit();
//        }
//        if (item.getItemId() == R.id.action_done) {
//            ((ProfileViewModel1) vm).update();
//            getNavigator().navigateActivity(ProfileDisplayActivity.class, null);
//        }
//        if (item.getItemId() == R.id.action_discard) {
//            ((ProfileViewModel1) vm).revertData();
//        }
//        if (item.getItemId() == android.R.id.home) {
//            getNavigator().navigateActivity(HomeActivity.class, null);
//        }
//        return true;
//    }

}

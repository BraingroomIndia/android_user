package com.braingroom.user.view.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.fragment.CommentFragment;
import com.braingroom.user.view.fragment.ConnectFeedFragment;
import com.braingroom.user.view.fragment.ConnectPostFragment;
import com.braingroom.user.view.fragment.LikesFragment;
import com.braingroom.user.view.fragment.ReplyFragment;
import com.braingroom.user.viewmodel.ConnectHomeViewModel;
import com.braingroom.user.viewmodel.LocationFilterViewModel;
import com.braingroom.user.viewmodel.ViewModel;


public class ConnectHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectUiHelper {

    private String TAG = getClass().getCanonicalName();

    public interface UiHelper {
        void updateLocationFilter();

    }

    public UiHelper uiHelper;
    public BottomNavigationView mBottomNav;

    int[][] states = new int[][]{
            new int[]{android.R.attr.state_checked}, // enabled
            new int[]{-android.R.attr.state_checked}
    };
    ColorStateList greenList, blueList;

    @IdRes
    int learnerForumSelectedNav = R.id.action_tips_tricks;
    @IdRes
    int tutorTalkSelectedNav = R.id.action_tips_tricks;

    ViewPager pager;
    public ConnectPagerAdapter pagerAdapter;
    ConnectFilterData learnersFilter, tutorsFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        greenList = new ColorStateList(states, new int[]{
                ContextCompat.getColor(this, R.color.bottomNavSelectedGreen),
                ContextCompat.getColor(this, R.color.bottomNavUnSelected)
        });
        blueList = new ColorStateList(states, new int[]{
                ContextCompat.getColor(this, R.color.bottomNavSelectedBlue),
                ContextCompat.getColor(this, R.color.bottomNavUnSelected)
        });
        initFilters();
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNav.inflateMenu(R.menu.bottom_nav_connect);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ConnectPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
        final AppBarLayout mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mAppBar.setBackgroundResource(R.color.colorPrimary);
                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        fab.setVisibility(View.VISIBLE);
                        mBottomNav.getMenu().clear();
                        mBottomNav.inflateMenu(R.menu.bottom_nav_connect);
                        mBottomNav.setItemTextColor(greenList);
                        mBottomNav.setItemIconTintList(greenList);
                        mBottomNav.setSelectedItemId(learnerForumSelectedNav);
                        break;
                    case 1:
                        mAppBar.setBackgroundResource(R.color.colorAccent);
                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
                        fab.setVisibility(View.INVISIBLE);
                        mBottomNav.getMenu().clear();
                        mBottomNav.inflateMenu(R.menu.bottom_nav_connect_2);
                        mBottomNav.setItemTextColor(blueList);
                        mBottomNav.setItemIconTintList(blueList);
                        mBottomNav.setSelectedItemId(tutorTalkSelectedNav);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (pager.getCurrentItem() == 0) learnerForumSelectedNav = itemId;
                else tutorTalkSelectedNav = itemId;

                if (itemId == R.id.action_tips_tricks) {
                    learnersFilter.setMinorCateg("tips_tricks");
                }
                if (itemId == R.id.action_buy_sell) {
                    learnersFilter.setMinorCateg("group_post");
                }
                if (itemId == R.id.action_find_partners) {
                    learnersFilter.setMinorCateg("activity_request");
                }
                if (itemId == R.id.action_discuss_n_decide) {
                    // TODO: 25/05/17 change  the argument
                    tutorsFilter.setMinorCateg("user_post");
                }
                if (itemId == R.id.action_tutors_article) {
                    fab.setVisibility(View.INVISIBLE);
                    tutorsFilter.setMinorCateg("vendor_article");
                }
                else{
                    fab.setVisibility(View.VISIBLE);
                }
                updateFilter();
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initNavigationDrawer();
    }

    public void initNavigationDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        uiHelper = new UiHelper() {
            @Override
            public void updateLocationFilter() {
                setLocationData();
                updateFilter();
            }
        };
        return new ConnectHomeViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_connect;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_connect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_find) {
            getNavigator().navigateActivity(HomeActivity.class, null);
            getNavigator().finishActivity();
            return true;
        }
        if (id == R.id.action_location) {
            getHelperFactory().createDialogHelper().showCustomView(R.layout.dialog_location, new LocationFilterViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), uiHelper));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            getNavigator().navigateActivity(ProfileActivity.class, null);
        }
        if (id == R.id.nav_wishlist) {
            Bundle data = new Bundle();
            data.putString("listType", "wishlist");
            getNavigator().navigateActivity(ClassSimpleListActivity.class, data);
        }
        if (id == R.id.nav_booking_history) {
            Bundle data = new Bundle();
            data.putString("listType", "bookingHistory");
            getNavigator().navigateActivity(ClassSimpleListActivity.class, data);
        }
        if (id == R.id.nav_logout) {
            getMessageHelper().showAcceptableInfo("Log out?", "Are you sure you want to log out of the app", new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                    vm.logOut();
                    Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }
            });
        }
        if (id == R.id.nav_login) {
            getNavigator().navigateActivity(LoginActivity.class, null);
            finish();
        }
        if (id == R.id.nav_register) {
            getNavigator().navigateActivity(SignupActivity.class, null);
            finish();
        }
        if (id == R.id.nav_faq)
            //Edited By Vikas Godara
            getNavigator().navigateActivity(FAQActivity.class, null);
        if (id == R.id.nav_tnc)
            getNavigator().navigateActivity(TermsConditionActivity.class, null);
        if (id == R.id.nav_contact)
            getNavigator().navigateActivity(ContactUsActivity.class, null);

        //Edited By Vikas Godara
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class ConnectPagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

        public ConnectPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) return ConnectFeedFragment.newInstance("learners_forum");
            if (i == 1) return ConnectFeedFragment.newInstance("tutors_talk");
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "Learners Forum";
            if (position == 1) return "Tutor Talk";
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

    public void updateFilter() {
        pagerAdapter.getFragmentAt(pager.getCurrentItem()).getViewModel().initConnectItemObserver(pager.getCurrentItem() == 0 ? learnersFilter : tutorsFilter);
    }

    private void initFilters() {
        learnersFilter = new ConnectFilterData();
        learnersFilter.setMajorCateg("learners_forum");
        tutorsFilter = new ConnectFilterData();
        tutorsFilter.setMajorCateg("tutors_talk");
        setLocationData();
    }

    public void setLocationData() {
        learnersFilter.setCountryId(pref.getInt("connect_country_id", 0) == 0 ? "" : pref.getInt("connect_country_id", 0) + "");
        learnersFilter.setStateId(pref.getInt("connect_state_id", 0) == 0 ? "" : pref.getInt("connect_state_id", 0) + "");
        learnersFilter.setCityId(pref.getInt("connect_city_id", 0) == 0 ? "" : pref.getInt("connect_city_id", 0) + "");
        tutorsFilter.setCountryId(pref.getInt("connect_country_id", 0) == 0 ? "" : pref.getInt("connect_country_id", 0) + "");
        tutorsFilter.setStateId(pref.getInt("connect_state_id", 0) == 0 ? "" : pref.getInt("connect_state_id", 0) + "");
        tutorsFilter.setCityId(pref.getInt("connect_city_id", 0) == 0 ? "" : pref.getInt("connect_city_id", 0) + "");
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
    public void openConnectPost() {
        Log.d(TAG,"openConnectPost: "+tutorTalkSelectedNav+", "+learnerForumSelectedNav);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        String postType = getActivePostType();
        Fragment mFragement = postType==null ? ConnectPostFragment.newInstance() : ConnectPostFragment.newInstanceByPostType(postType) ;
        transaction.replace(R.id.comments_container, mFragement).addToBackStack(null).commit();
    }

    @Override
    public void popFragment() {
        popBackstack();
    }


    private String getActivePostType()
    {
        String postType = null;

        if(pager==null) return postType;

        int itemId = tutorTalkSelectedNav;
        if (pager.getCurrentItem() == 0) itemId = learnerForumSelectedNav;

        if (itemId == R.id.action_tips_tricks) {
            postType = "action_tips_tricks";
        }
        if (itemId == R.id.action_buy_sell) {
            postType = "action_buy_sell";
        }
        if (itemId == R.id.action_find_partners) {
            postType = "action_find_partners";
        }
        if (itemId == R.id.action_discuss_n_decide) {
            postType = "action_discuss_n_decide";
        }
        if (itemId == R.id.action_tutors_article) {
            postType = "action_tutors_article";
        }

        return postType;

    }


}

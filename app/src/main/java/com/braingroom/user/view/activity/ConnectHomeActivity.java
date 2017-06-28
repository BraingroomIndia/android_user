package com.braingroom.user.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
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
import com.braingroom.user.utils.BadgeDrawable;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.fragment.CommentFragment;
import com.braingroom.user.view.fragment.ConnectFeedFragment;
import com.braingroom.user.view.fragment.ConnectFilterFragment;
import com.braingroom.user.view.fragment.ConnectPostFragment;
import com.braingroom.user.view.fragment.DynamicSearchSelectListFragment;
import com.braingroom.user.view.fragment.LikesFragment;
import com.braingroom.user.view.fragment.PostAcceptFragment;
import com.braingroom.user.view.fragment.ReplyFragment;
import com.braingroom.user.view.fragment.SearchSelectListFragment;
import com.braingroom.user.viewmodel.ConnectFeedItemViewModel;
import com.braingroom.user.viewmodel.ConnectFilterViewModel;
import com.braingroom.user.viewmodel.ConnectHomeViewModel;
import com.braingroom.user.viewmodel.LocationFilterViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ConnectFeedViewModel;

import lombok.Getter;


public class ConnectHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectUiHelper {

    public static final String FRAGMENT_TITLE_CATEGORY = "Category";
    public static final String FRAGMENT_TITLE_SEGMENT = "Segments";
    public static final String FRAGMENT_TITLE_MY_GROUPS = "My Groups";
    public static final String FRAGMENT_TITLE_ALL_GROUPS = "All Groups";

    public static final String FRAGMENT_TITLE_COUNTRY = "country";
    public static final String FRAGMENT_TITLE_STATE = "state";
    public static final String FRAGMENT_TITLE_CITY = "city";
    public static final String FRAGMENT_TITLE_LOCALITY = "locality";

    public static final String FRAGMENT_TITLE_COLLEGE = "College";

    private MenuItem itemNotification;
    private MenuItem itemMessage;

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
    int learnerForumSelectedNav = R.id.action_all;
    @IdRes
    int tutorTalkSelectedNav = R.id.action_all;

    ViewPager pager;
    public ConnectPagerAdapter pagerAdapter;
    ConnectFilterData learnersFilter, tutorsFilter;

    @Getter
    ConnectFilterViewModel connectFilterViewModel;

    Fragment mFragement;

    String defMajorCateg;
    String defMinorCateg;

    private AppBarLayout mAppBar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        defMajorCateg = getIntentString("defMajorCateg") != null ? getIntentString("defMajorCateg") : "learners_forum";
        defMinorCateg = getIntentString("defMinorCateg") != null ? getIntentString("defMinorCateg") : "";
        greenList = new ColorStateList(states, new int[]{
                ContextCompat.getColor(this, R.color.bottomNavSelectedGreen),
                ContextCompat.getColor(this, R.color.bottomNavUnSelected)
        });
        blueList = new ColorStateList(states, new int[]{
                ContextCompat.getColor(this, R.color.bottomNavSelectedBlue),
                ContextCompat.getColor(this, R.color.bottomNavUnSelected)
        });
        connectFilterViewModel = new ConnectFilterViewModel(getMessageHelper(), getNavigator(), new FragmentHelper() {
            @Override
            public void show(String tag) {
                if (!tag.equals(FRAGMENT_TITLE_COLLEGE)) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.comments_container, SearchSelectListFragment.newInstance(tag)).addToBackStack(tag).commit();
                } else {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.comments_container, DynamicSearchSelectListFragment.newInstance(tag)).addToBackStack(tag).commit();
                }
            }

            @Override
            public void remove(String tag) {
                popBackstack(tag);
            }
        }, this);
        initFilters();
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNav.inflateMenu(R.menu.bottom_nav_connect);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ConnectPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);

        if ("tips_tricks".equals(defMinorCateg)) {
            learnerForumSelectedNav = R.id.action_tips_tricks;
        }
        if ("group_post".equals(defMinorCateg)) {
            learnerForumSelectedNav = R.id.action_buy_sell;
        }
        if ("activity_request".equals(defMinorCateg))
            learnerForumSelectedNav = R.id.action_find_partners;
        if ("user_post".equals(defMinorCateg)) tutorTalkSelectedNav = R.id.action_discuss_n_decide;
        if ("vendor_article".equals(defMinorCateg)) {
            tutorTalkSelectedNav = R.id.action_tutors_article;
        }

        if ("learners_forum".equals(defMajorCateg)) {
            pager.setCurrentItem(0);
            setPagerLearnerForum();
            mBottomNav.setSelectedItemId(learnerForumSelectedNav);
        } else {
            pager.setCurrentItem(1);
            setPageTutorTalk();
            mBottomNav.setSelectedItemId(tutorTalkSelectedNav);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        setPagerLearnerForum();
                        break;
                    case 1:
                        setPageTutorTalk();
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

                if (itemId==R.id.action_all){
                    learnersFilter.setMinorCateg("");
                    tutorsFilter.setMinorCateg("");
                }
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
                } else {
                    fab.setVisibility(View.VISIBLE);
                }
                updateFilter();
                return true;
            }
        });
    }

    private void unCheckAllMenuItems(@NonNull final Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                // Un check sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
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
        itemNotification = menu.findItem(R.id.action_notifications);
        itemMessage = menu.findItem(R.id.action_messages);
        setBadgeCount(itemNotification, this, ((ConnectHomeViewModel) vm).notificationCount);
        setBadgeCount(itemMessage, this, ((ConnectHomeViewModel) vm).messageCount);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_find) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            getNavigator().navigateActivity(intent);
            return true;
        }
        if (id == R.id.action_messages) {

            if (!vm.loggedIn.get()) {
                Bundle data = new Bundle();
                data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                getMessageHelper().showLoginRequireDialog("Only logged in users can send a message", data);
                return true;
            }
            getNavigator().navigateActivity(MessageActivity.class, null);
//            vm.retry();
            return true;
        }
        if (id == R.id.action_notifications) {
            if (!vm.loggedIn.get()) {
                Bundle data = new Bundle();
                data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                getMessageHelper().showLoginRequireDialog("Only logged in users can see notification", data);
                return true;
            }
            getNavigator().navigateActivity(NotificationActivity.class, null);
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
        if (id == R.id.nav_location)
            getHelperFactory().createDialogHelper().showCustomView(R.layout.dialog_location, new LocationFilterViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), uiHelper), false);

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
            if (i == 0) return ConnectFeedFragment.newInstance("learners_forum", defMinorCateg);
            if (i == 1) return ConnectFeedFragment.newInstance("tutors_talk", defMinorCateg);
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "Learners Forum";
            if (position == 1) return "Tutors Talk";
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
        pagerAdapter.getFragmentAt(pager.getCurrentItem()).getViewModel().reset(pager.getCurrentItem() == 0 ? learnersFilter : tutorsFilter);
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
    public void openAcceptedUsersFragment(String postId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, PostAcceptFragment.newInstance(postId)).addToBackStack(null).commit();

    }

    @Override
    public void openConnectPost() {
        Log.d(TAG, "openConnectPost: " + tutorTalkSelectedNav + ", " + learnerForumSelectedNav);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        String postType = getActivePostType();
        mFragement = postType == null ? ConnectPostFragment.newInstance() : ConnectPostFragment.newInstanceByPostType(postType);
        transaction.replace(R.id.comments_container, mFragement).addToBackStack(null).commit();
    }

    @Override
    public void popFragment() {
        popBackstack();
    }

    @Override
    public void setCount(int notificationCount, int messageCount) {
        setBadgeCount(itemNotification, ConnectHomeActivity.this, notificationCount);
        setBadgeCount(itemMessage, ConnectHomeActivity.this, messageCount);
    }


    @Override
    public void retry() {
        pagerAdapter.getFragmentAt(0).getViewModel().retry();
        pagerAdapter.getFragmentAt(1).getViewModel().retry();
    }

    @Override
    public void setSearchQuery(String searchQuery) {
        if (pager.getCurrentItem() == 0)
            learnersFilter.setSearchQuery(searchQuery);
        else
            tutorsFilter.setSearchQuery(searchQuery);
        updateFilter();

    }

    @Override
    public void setFilterData(String keyword, String categoryId, String segmentId, String myGroupId, String allGroupId) {
        learnersFilter.setSearchQuery(keyword);
        learnersFilter.setCategId(categoryId);
        learnersFilter.setSegId(segmentId);
        if ("".equals(myGroupId))
            tutorsFilter.setGroupId(allGroupId);
        else
            tutorsFilter.setGroupId(myGroupId);
        tutorsFilter.setSearchQuery(keyword);
        updateFilter();
    }

    @Override
    public void openFilter() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, ConnectFilterFragment.newInstance()).addToBackStack(null).commit();
    }

    private String getActivePostType() {
        String postType = null;

        if (pager == null) return postType;

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
        if (itemId == R.id.action_all && pager.getCurrentItem() != 0) {
            postType = "tutor_talks";
        }
        if (itemId == R.id.action_tutors_article) {
            postType = "tutor_talks";
        }

        return postType;

    }


    private void setPageTutorTalk() {
        connectFilterViewModel.isLearnerForum.set(false);
        mAppBar.setBackgroundResource(R.color.colorAccent);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        mBottomNav.getMenu().clear();
        mBottomNav.inflateMenu(R.menu.bottom_nav_connect_2);
        mBottomNav.setItemTextColor(blueList);
        mBottomNav.setItemIconTintList(blueList);
        mBottomNav.setSelectedItemId(tutorTalkSelectedNav);
        ((ConnectHomeViewModel) vm).searchQuery.set("");
    }

    private void setPagerLearnerForum() {
        connectFilterViewModel.isLearnerForum.set(true);
        mAppBar.setBackgroundResource(R.color.colorPrimary);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        fab.setVisibility(View.VISIBLE);
        mBottomNav.getMenu().clear();
        mBottomNav.inflateMenu(R.menu.bottom_nav_connect);
        mBottomNav.setItemTextColor(greenList);
        mBottomNav.setItemIconTintList(greenList);
        mBottomNav.setSelectedItemId(learnerForumSelectedNav);
        ((ConnectHomeViewModel) vm).searchQuery.set("");
    }

    public void setBadgeCount(MenuItem item, Context context, int count) {

        if (item != null && vm.loggedIn.get()) {
            BadgeDrawable badge;
            LayerDrawable icon = (LayerDrawable) item.getIcon();
            Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
            if (reuse != null && reuse instanceof BadgeDrawable) {
                badge = (BadgeDrawable) reuse;
            } else {
                badge = new BadgeDrawable(context);
            }

            badge.setCount(count);
            icon.mutate();
            icon.setDrawableByLayerId(R.id.ic_badge, badge);
        }
    }

    @Override
    public ViewModel getFragmentViewmodel(String title) {
        if (FRAGMENT_TITLE_ALL_GROUPS.equals(title))
            return connectFilterViewModel.allGroups;
        if (FRAGMENT_TITLE_MY_GROUPS.equals(title))
            return connectFilterViewModel.myGroups;
        if (FRAGMENT_TITLE_CATEGORY.equals(title))
            return connectFilterViewModel.categoryVm;
        if (FRAGMENT_TITLE_SEGMENT.equals(title))
            return connectFilterViewModel.segmentsVm;
        if (FRAGMENT_TITLE_COUNTRY.equals(title))
            return connectFilterViewModel.countryVm;
        if (FRAGMENT_TITLE_STATE.equals(title))
            return connectFilterViewModel.stateVm;
        if (FRAGMENT_TITLE_CITY.equals(title))
            return connectFilterViewModel.cityVm;
        if (FRAGMENT_TITLE_LOCALITY.equals(title))
            return connectFilterViewModel.localityVM;
        if (FRAGMENT_TITLE_COLLEGE.equals(title))
            return connectFilterViewModel.instituteVm;
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mFragement != null && !mFragement.isDetached())
            mFragement.onActivityResult(requestCode, resultCode, data);
    }


}

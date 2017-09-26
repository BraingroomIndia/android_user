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
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.utils.BadgeDrawable;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.fragment.BaseFragment;
import com.braingroom.user.view.fragment.CommentFragment;
import com.braingroom.user.view.fragment.ConnectFilterFragment;
import com.braingroom.user.view.fragment.ConnectPostFragment;
import com.braingroom.user.view.fragment.DynamicSearchSelectListFragment;
import com.braingroom.user.view.fragment.LikesFragment;
import com.braingroom.user.view.fragment.PostAcceptFragment;
import com.braingroom.user.view.fragment.ReplyFragment;
import com.braingroom.user.view.fragment.SearchSelectListFragment;
import com.braingroom.user.viewmodel.ConnectFilterViewModel;
import com.braingroom.user.viewmodel.ConnectHomeViewModel;
import com.braingroom.user.viewmodel.LocationFilterViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.DynamicSearchSelectListViewModel;

import java.util.List;

import lombok.Getter;


public class ConnectHomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, ConnectUiHelper {


    public static String LEARNER_FORUM = "learners_forum";
    public static String TIPS_TRICKS = "tips_tricks";
    public static String BUY_SELL = "group_post";
    public static String FIND_PARTNERS = "activity_request";

    //TUTOR TALKS
    public static String TUTORS_TALK = "tutors_talk";
    public static String TUTORS_ARTICLE = "vendor_article";
    public static String DISCUSS_DECIDE = "user_post";


    public static final String FRAGMENT_TITLE_CATEGORY = "Category";
    public static final String FRAGMENT_TITLE_SEGMENT = "Segments";
    public static final String FRAGMENT_TITLE_MY_GROUPS = "My Groups";
    public static final String FRAGMENT_TITLE_ALL_GROUPS = "All Groups";

    public static final String FRAGMENT_TITLE_COUNTRY = "country";
    public static final String FRAGMENT_TITLE_STATE = "state";
    public static final String FRAGMENT_TITLE_CITY = "city";
    public static final String FRAGMENT_TITLE_LOCALITY = "locality";

    public static final String FRAGMENT_TITLE_COLLEGE = DynamicSearchSelectListViewModel.FRAGMENT_TITLE_COLLEGE;
    public static final String FRAGMENT_TITLE_LEARNER = DynamicSearchSelectListViewModel.FRAGMENT_TITLE_LEARNER;
    public static final String FRAGMENT_TITLE_Vendor = DynamicSearchSelectListViewModel.FRAGMENT_TITLE_Vendor;

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
    int selectedBottomNav = R.id.action_tips_tricks;

    @Getter
    ConnectFilterViewModel connectFilterViewModel;

    BaseFragment mFragement;

    String defMajorCateg;
    String defMinorCateg;
    final ObservableField<Integer> updateFilter = new ObservableField<>(0);

    private AppBarLayout mAppBar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vm = (ConnectHomeViewModel) vm;
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
                if (!tag.equals(FRAGMENT_TITLE_COLLEGE) && !tag.equals(FRAGMENT_TITLE_LEARNER) && !tag.equals(FRAGMENT_TITLE_Vendor)) {
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
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        mBottomNav = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mBottomNav.inflateMenu(R.menu.bottom_nav_connect);
        if (TIPS_TRICKS.equals(((ConnectHomeViewModel) vm).getFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_tips_tricks;
        if (BUY_SELL.equals(((ConnectHomeViewModel) vm).getFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_buy_sell;
        if (FIND_PARTNERS.equals(((ConnectHomeViewModel) vm).getFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_find_partners;
        if (TUTORS_ARTICLE.equals(((ConnectHomeViewModel) vm).getFilterData().getMinorCateg()))
            selectedBottomNav = R.id.action_tutors_article;
        if (DISCUSS_DECIDE.equals(((ConnectHomeViewModel) vm).getFilterData().getMinorCateg()))
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


    @Override
    protected void onStart() {
        super.onStart();
        initNavigationDrawer();
    }

    public void initNavigationDrawer() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().clear();
        if (vm.getLoggedIn())
            navigationView.inflateMenu(R.menu.activity_connect_drawer_loggedin);
        else
            navigationView.inflateMenu(R.menu.activity_home_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        ConnectFilterData connectFilterData = (ConnectFilterData) getIntentSerializable(Constants.connectFilterData);
        if (connectFilterData == null) {
            connectFilterData = new ConnectFilterData();
            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(TIPS_TRICKS);
        }
        uiHelper = new UiHelper() {
            @Override
            public void updateLocationFilter() {
            }


        };
        return new ConnectHomeViewModel(getFirebaseAnalytics(), getGoogleTracker(), connectFilterData, getMessageHelper(), getNavigator(), getHelperFactory(), this);
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
        setBadgeCount(itemNotification, this, ViewModel.notificationCount);
        setBadgeCount(itemMessage, this, ViewModel.messageCount);
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

            if (!vm.getLoggedIn()) {
                Bundle data = new Bundle();
                data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                getMessageHelper().showLoginRequireDialog("Only logged in users can send a message", data);
                return true;
            } else {
                ViewModel.messageCount = 0;
                getNavigator().navigateActivity(MessageActivity.class, null);
            }
//            vm.retry();
            return true;
        }
        if (id == R.id.action_notifications) {
            if (!vm.getLoggedIn()) {
                Bundle data = new Bundle();
                data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                getMessageHelper().showLoginRequireDialog("Only logged in users can see notification", data);
                return true;
            } else {
                ViewModel.notificationCount = 0;
                getNavigator().navigateActivity(NotificationActivity.class, null);
                return true;
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_profile) {
            getNavigator().navigateActivity(ProfileDisplayActivity.class, null);
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
                    Intent i = new Intent(ConnectHomeActivity.this, ConnectHomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();

                }
            });
        }
        if (id == R.id.nav_login) {
            getNavigator().navigateActivityForResult(LoginActivity.class, null, ViewModel.REQ_CODE_LOGIN);
        }
        if (id == R.id.nav_register) {
            getNavigator().navigateActivity(SignupActivity.class, null);
            finish();
        }
        if (id == R.id.nav_change_pass)
            getNavigator().navigateActivity(ChangePasswordActivity.class, null);
        if (id == R.id.nav_faq)
            //Edited By Vikas Godara
            getNavigator().navigateActivity(FAQActivity.class, null);
        if (id == R.id.nav_tnc)
            getNavigator().navigateActivity(TermsConditionActivity.class, null);
        if (id == R.id.nav_contact)
            getNavigator().navigateActivity(ContactUsActivity.class, null);
        if (id == R.id.nav_location)
            getHelperFactory().createDialogHelper().showCustomView(R.layout.dialog_location, new LocationFilterViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), uiHelper), false);

        if (id == R.id.nav_catalogue) {
            getNavigator().navigateActivity(CatalogueHomeActivity.class, null);
//            finish();
        }
/*        if (id == R.id.nav_giftcard) {
            getNavigator().navigateActivity(GiftcardCouponActivity.class, null);
//            finish();
        }*/
        //Edited By Vikas Godara
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*public class ConnectPagerAdapter extends FragmentStatePagerAdapter {
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
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return "";
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


    }*/

   /* public void updateFilter() {
        //postPagerAdapter.getFragmentAt(postPager.getCurrentItem()).getViewModel().reset(postPager.getCurrentItem() == 0 ? learnersFilter : tutorsFilter);
    }*/


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
        vm.retry();
    }

    @Override
    public void onUserInteraction() {

        super.onUserInteraction();
    }

    @Override
    public void openFollowers() {

    }

    @Override
    public void openFollowing() {

    }


    @Override
    public void setSearchQuery(String searchQuery) {


    }

    @Override
    public void setFilterData(String keyword, String categoryId, String segmentId, String myGroupId, String allGroupId,
                              String instituteId, String authorId, List<String> location) {

        ((ConnectHomeViewModel) vm).getFilterData().setSearchQuery(keyword);
        ((ConnectHomeViewModel) vm).getFilterData().setCategId(categoryId);
        ((ConnectHomeViewModel) vm).getFilterData().setGroupId(allGroupId);
        ((ConnectHomeViewModel) vm).getFilterData().setSegId(segmentId);
        ((ConnectHomeViewModel) vm).getFilterData().setCountryId(location.get(0));
        ((ConnectHomeViewModel) vm).getFilterData().setStateId(location.get(1));
        ((ConnectHomeViewModel) vm).getFilterData().setCityId(location.get(2));
        ((ConnectHomeViewModel) vm).getFilterData().setLocalityId(location.get(3));
        ((ConnectHomeViewModel) vm).getFilterData().setInstituteId(instituteId);
        ((ConnectHomeViewModel) vm).getFilterData().setAuthorId(authorId);
        ((ConnectHomeViewModel) vm).rest();
       /* updateFilter();*/
    }

    @Override
    public void openFilter() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.bottom_in, R.anim.top_out);
        transaction.replace(R.id.comments_container, ConnectFilterFragment.newInstance()).addToBackStack(null).commit();
    }

    private String getActivePostType() {
        String postType = "";

//        if (postPager == null) return postType;

        int itemId = selectedBottomNav;
        //  if (postPager.getCurrentItem() == 0) itemId = selectedBottomNav;
        if (itemId == R.id.action_tips_tricks)
            postType = "action_tips_tricks";
        if (itemId == R.id.action_buy_sell)
            postType = "action_buy_sell";
        if (itemId == R.id.action_find_partners)
            postType = "action_find_partners";
        if (itemId == R.id.action_discuss_n_decide)
            postType = "action_discuss_n_decide";
       /* if (itemId == R.id.action_all && postPager.getCurrentItem() != 0) {
            postType = "tutor_talks";
        }
        if (itemId == R.id.action_tutors_article) {
            postType = "tutor_talks";
        }
        if (itemId == R.id.action_discuss_n_decide)
            postType = "tutor_talks";
*/
        return postType;

    }


/*    private void setPageTutorTalk() {
        connectFilterViewModel.isLearnerForum.set(false);
        connectFilterViewModel.reset();
        learnersFilter = new ConnectFilterData();
        learnersFilter.setMajorCateg("learners_forum");
        setLocationData();
        mAppBar.setBackgroundResource(R.color.colorAccent);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        mBottomNav.getMenu().clear();
        mBottomNav.inflateMenu(R.menu.bottom_nav_connect_2);
        mBottomNav.setItemTextColor(blueList);
        mBottomNav.setItemIconTintList(blueList);
        mBottomNav.setSelectedItemId(tutorTalkSelectedNav);
        ((ConnectHomeViewModel) vm).searchQuery.set("");
    }*/

/*    private void setPagerLearnerForum() {
        connectFilterViewModel.isLearnerForum.set(true);
        connectFilterViewModel.reset();
        tutorsFilter = new ConnectFilterData();
        tutorsFilter.setMajorCateg("tutors_talk");
        mAppBar.setBackgroundResource(R.color.colorPrimary);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        fab.setVisibility(View.VISIBLE);
        mBottomNav.getMenu().clear();
        mBottomNav.inflateMenu(R.menu.bottom_nav_connect);
        mBottomNav.setItemTextColor(greenList);
        mBottomNav.setItemIconTintList(greenList);
        mBottomNav.setSelectedItemId(selectedBottomNav);
        ((ConnectHomeViewModel) vm).searchQuery.set("");
    }*/

    public void setBadgeCount(MenuItem item, Context context, int count) {

        if (item != null && vm.getLoggedIn()) {
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
          /*  return connectFilterViewModel.myGroups;*/
            return null;
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
        if (FRAGMENT_TITLE_LEARNER.equals(title))
            return connectFilterViewModel.learnerVm;
        if (FRAGMENT_TITLE_Vendor.equals(title))
            return connectFilterViewModel.tutorVm;
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int id = mBottomNav.getSelectedItemId();
        updateBottomNavigation(id);
        // navigationView.getMenu().clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (vm.getLoggedIn())
            navigationView.getMenu().clear();
        if (mFragement != null && !mFragement.isDetached())
            mFragement.onActivityResult(requestCode, resultCode, data);

//        if (vm.getLoggedIn()) navigationView.inflateMenu(R.menu.activity_home_drawer_loggedin);
//        else navigationView.inflateMenu(R.menu.activity_home_drawer);

    }

    private void updateBottomNavigation(int itemId) {
        if (itemId == R.id.action_tips_tricks) {
            ConnectFilterData connectFilterData = new ConnectFilterData();
            selectedBottomNav = R.id.action_tips_tricks;
            connectFilterViewModel.isLearnerForum.set(true);
            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(TIPS_TRICKS);
            if (!connectFilterData.isEqual(((ConnectHomeViewModel) vm).getFilterData()))
                ((ConnectHomeViewModel) vm).setFilterData(connectFilterData);
        }
        if (itemId == R.id.action_buy_sell) {
            selectedBottomNav = R.id.action_buy_sell;
            connectFilterViewModel.isLearnerForum.set(true);
            ConnectFilterData connectFilterData = new ConnectFilterData();
            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(BUY_SELL);
            if (!connectFilterData.isEqual(((ConnectHomeViewModel) vm).getFilterData()))
                ((ConnectHomeViewModel) vm).setFilterData(connectFilterData);
        }
        if (itemId == R.id.action_find_partners) {
            selectedBottomNav = R.id.action_find_partners;
            connectFilterViewModel.isLearnerForum.set(true);
            ConnectFilterData connectFilterData = new ConnectFilterData();
            connectFilterData.setMajorCateg(LEARNER_FORUM);
            connectFilterData.setMinorCateg(FIND_PARTNERS);
            if (!connectFilterData.isEqual(((ConnectHomeViewModel) vm).getFilterData()))
                ((ConnectHomeViewModel) vm).setFilterData(connectFilterData);
        }

        if (itemId == R.id.action_discuss_n_decide) {
            selectedBottomNav = R.id.action_discuss_n_decide;
            connectFilterViewModel.isLearnerForum.set(false);
            ConnectFilterData connectFilterData = new ConnectFilterData();
            connectFilterData.setMajorCateg(TUTORS_TALK);
            connectFilterData.setMinorCateg(DISCUSS_DECIDE);
            if (!connectFilterData.isEqual(((ConnectHomeViewModel) vm).getFilterData()))
                ((ConnectHomeViewModel) vm).setFilterData(connectFilterData);
        }
        if (itemId == R.id.action_tutors_article) {
            fab.setVisibility(View.INVISIBLE);
            connectFilterViewModel.isLearnerForum.set(false);
            ConnectFilterData connectFilterData = new ConnectFilterData();
            connectFilterData.setMajorCateg(TUTORS_TALK);
            connectFilterData.setMinorCateg(TUTORS_ARTICLE);
            if (!connectFilterData.isEqual(((ConnectHomeViewModel) vm).getFilterData()))
                ((ConnectHomeViewModel) vm).setFilterData(connectFilterData);
        } else {
            fab.setVisibility(View.VISIBLE);
        }
    }


}

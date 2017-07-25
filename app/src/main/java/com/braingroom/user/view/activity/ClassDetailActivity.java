package com.braingroom.user.view.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.braingroom.user.R;
import com.braingroom.user.view.fragment.ClassQueryFragment;
import com.braingroom.user.view.fragment.ConnectFeedFragment;
import com.braingroom.user.view.fragment.DemoPostFragment;
import com.braingroom.user.view.fragment.QuoteFormFragment;
import com.braingroom.user.viewmodel.ClassDetailViewModel;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.ViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import retrofit2.http.POST;

import static com.braingroom.user.R.string.action_learners_forum;
import static com.braingroom.user.R.string.action_tips_tricks;
import static com.braingroom.user.R.string.action_tutors_talk;
import static com.rollbar.android.Rollbar.TAG;

public class ClassDetailActivity extends BaseActivity {

    YouTubePlayerFragment youTubePlayerFragment;
    YouTubePlayer youTubePlayer;
    MyPlaybackEventListener myPlaybackEventListener;
    UiHelper uiHelper;
    SupportMapFragment mapFragment;
    RxPermissions rxPermissions;

    ViewPager pager;
    PostPagerAdapter pagerAdapter;

    public interface UiHelper {
        void initYoutube();

        void invalidateMenu();

        void stopShimmer();

        void showQuoteForm();

        void postQueryForm();

        void next();

        void makeACall(String phoneNumber);


    }

    private class PostPagerAdapter extends FragmentStatePagerAdapter {
        SparseArray<Fragment> registeredFragments = new SparseArray<>();

         PostPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    ((ClassDetailViewModel) vm).connectFilterData.setMinorCateg("tips_tricks");
                   break;
                case 1:
                    ((ClassDetailViewModel) vm).connectFilterData.setMinorCateg("group_post");
                    break;
                case 2:
                    ((ClassDetailViewModel) vm).connectFilterData.setMinorCateg("group_post");
                    break;
                default:
                    ((ClassDetailViewModel) vm).connectFilterData.setMinorCateg("");
                    break;
            }
            return DemoPostFragment.newInstance(((ClassDetailViewModel) vm).connectFilterData);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) return getString(action_tips_tricks);
            if (position == 1) return getString(action_learners_forum);
            if (position == 2) return getString(action_tutors_talk);
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rxPermissions = new RxPermissions(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                ((ClassDetailViewModel) vm).setGoogleMap(googleMap);
            }
        });
        if (!ClassListViewModel1.ORIGIN_CATALOG.equals(getIntentString("origin"))){
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
            pager = (ViewPager) findViewById(R.id.pager);
            pagerAdapter = new PostPagerAdapter(getSupportFragmentManager());
            pager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(pager);
        }

//        shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_container);
//        shimmerFrameLayout.startShimmerAnimation();
//        ((ClassDetailViewModel) vm).setUiHelper(uiHelper);
    }


    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().setElevation(0);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        myPlaybackEventListener = new MyPlaybackEventListener();
        uiHelper = new UiHelper() {
            @Override
            public void initYoutube() {
            }

            @Override
            public void invalidateMenu() {
                invalidateOptionsMenu();
            }

            @Override
            public void stopShimmer() {
//                shimmerFrameLayout.stopShimmerAnimation();
            }

            @Override
            public void showQuoteForm() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, QuoteFormFragment.newInstance(getIntentString("catalogueId"), getIntentString("id"))).addToBackStack(null).commit();
            }

            @Override
            public void postQueryForm() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, ClassQueryFragment.newInstance()).addToBackStack(null).commit();
            }

            @Override
            public void makeACall(final String phoneNumber) {
                if (phoneNumber != null && rxPermissions != null)
                    rxPermissions.request(Manifest.permission.CALL_PHONE).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Boolean aBoolean) throws Exception {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            Log.d(TAG, "accept: " + phoneNumber);
                            getNavigator().navigateActivity(intent);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            //throwable.printStackTrace();
                            Log.d("makeACall", "OnError: " + throwable.toString() + "\t" + phoneNumber);
                        }
                    });
            }

            @Override
            public void next() {
                onBackPressed();
            }


        };
        return new ClassDetailViewModel(getHelperFactory(), uiHelper, getMessageHelper(), getNavigator(), getIntentString("id"),
                getIntentString("origin"), getIntentString("catalogueId"));
    }

    @Override
    protected int getLayoutId() {
        if (ClassListViewModel1.ORIGIN_CATALOG.equals(getIntentString("origin")))
            return R.layout.activity_class_detail_catalog;
        return R.layout.activity_class_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getLayoutId() != R.layout.activity_class_detail_catalog) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.activity_class_detail, menu);
            if (((ClassDetailViewModel) vm).isInWishlist)
                menu.findItem(R.id.action_wishlist).setIcon(R.drawable.ic_favorite_white_24dp);
            return true;
        }
        return false;

    }

    public ClassDetailViewModel getViewModel() {
        return (ClassDetailViewModel) vm;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_wishlist) {
            if (ViewModel.loggedIn.get()) {
                ((ClassDetailViewModel) vm).addToWishlist();
            } else {
                Bundle data = new Bundle();
                data.putString("id", getIntentString("id"));
                data.putString("backStackActivity", ClassDetailActivity.class.getSimpleName());
                getMessageHelper().showLoginRequireDialog("Please login to add to wishlist", data);
            }
            return true;
        }

        if (id == R.id.action_share) {
            ((ClassDetailViewModel) vm).share();
            return true;
        }
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ClassDetailViewModel) vm).releaseYoutube();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public final class MyPlaybackEventListener implements YouTubePlayer.PlaybackEventListener {

        @Override
        public void onPlaying() {
            // Called when playback starts, either due to user action or call to play().
            ;
        }

        @Override
        public void onPaused() {
            // Called when playback is paused, either due to user action or call to pause().

        }

        @Override
        public void onStopped() {
            // Called when playback stops for a reason other than being paused.

        }

        @Override
        public void onBuffering(boolean b) {
            // Called when buffering starts or ends.
        }

        @Override
        public void onSeekTo(int i) {
            // Called when a jump in playback position occurs, either
            // due to user scrubbing or call to seekRelativeMillis() or seekToMillis()
        }
    }

}

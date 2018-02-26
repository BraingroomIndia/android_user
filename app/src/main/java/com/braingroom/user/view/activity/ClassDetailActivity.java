package com.braingroom.user.view.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.transition.Fade;
import android.support.transition.Scene;
import android.support.transition.Transition;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.text.Layout;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.braingroom.user.R;
import com.braingroom.user.databinding.DemoClassColapsedSceneBinding;
import com.braingroom.user.databinding.DemoClassExpandedSceneBinding;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.WrapContentHeightViewPager;
import com.braingroom.user.view.fragment.ClassQueryFragment;
import com.braingroom.user.view.fragment.DemoPostFragment;
import com.braingroom.user.view.fragment.QuoteFormFragment;
import com.braingroom.user.view.fragment.ReviewFragment;
import com.braingroom.user.viewmodel.ClassDetailViewModel;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.ViewModel;
import com.braingroom.user.viewmodel.fragment.ClassDetailDemoPostViewModel;
import com.braingroom.user.viewmodel.fragment.ReviewAddViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import timber.log.Timber;

import static com.braingroom.user.R.string.action_learners_forum;
import static com.braingroom.user.R.string.action_tips_tricks;
import static com.braingroom.user.R.string.action_tutors_talk;
import static com.google.android.youtube.player.YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT;

public class ClassDetailActivity extends BaseActivity {

    YouTubePlayerFragment youTubePlayerFragment;
    YouTubePlayer youTubePlayer;
    MyPlaybackEventListener myPlaybackEventListener;
    UiHelper uiHelper;
    SupportMapFragment mapFragment;
    RxPermissions rxPermissions;

    //    WrapContentHeightViewPager pager;
 /*   PostPagerAdapter pagerAdapter;
*/
    Scene mColapsedScene;
    Scene mExpandedScene;
    RelativeLayout mSceneRoot;
    ImageView imageView;
    TextView catalogLocationList;

    public String classId;

    public interface UiHelper {
        void initYoutube();

        void invalidateMenu();

        void stopShimmer();

        void showQuoteForm();

        void postQueryForm();

        void addReview();

        void addReview(String userId);

        void backFromReview();

        void next();

        void makeACall(String phoneNumber);

        void expandDemoClass(String v);

        void compressDemoClass(String v);

        boolean isViewEllipsized();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        catalogLocationList = findViewById(R.id.catalog_location_list);
        if (catalogLocationList != null)
            catalogLocationList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Layout l = catalogLocationList.getLayout();
                    if (l != null && vm != null) {
                        try {
                            ((ClassDetailViewModel) vm).hideViewMore.set((l.getEllipsisCount(3) > 0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        youTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        youTubePlayerFragment.getView().setVisibility(View.INVISIBLE);

        rxPermissions = new RxPermissions(this);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null)
            mapFragment.getMapAsync(((ClassDetailViewModel) vm)::setGoogleMap);

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (getSupportActionBar() != null)
            getSupportActionBar().setElevation(0);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        classId = getIntentString("id");
        uiHelper = new UiHelper() {
            @Override
            public void initYoutube() {
                youTubePlayerFragment.getView().setVisibility(View.VISIBLE);
                youTubePlayerFragment.initialize("AIzaSyBsaNQgFsk2LbSmXydzNAhBdsQ4YkzAoh0", new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
                        //youTubePlayer.setPlaybackEventListener(myPlaybackEventListener);
                        youTubePlayer.setFullscreen(false);
                        youTubePlayer.setFullscreenControlFlags(FULLSCREEN_FLAG_CUSTOM_LAYOUT);
                        ((ClassDetailViewModel) vm).setYoutubePlayer(youTubePlayer);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Log.d("Youtube player error", "onInitializationFailure: ");
                    }
                });

            }

            @Override
            public void invalidateMenu() {
                invalidateOptionsMenu();
            }

            @Override
            public void stopShimmer() {
                mFirebaseAnalytics.setCurrentScreen(ClassDetailActivity.this, ((ClassDetailViewModel) vm).mClassData.getClassTopic(), null);

            }

            @Override
            public void showQuoteForm() {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.add(R.id.fragment_container, QuoteFormFragment.newInstance(getIntentString("catalogueId"), classId)).addToBackStack(null).commit();
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
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse("tel:" + phoneNumber));
                            Timber.tag(TAG).d("accept: " + phoneNumber);
                            getNavigator().navigateActivity(intent);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            //throwable.printStackTrace();
                            Timber.tag(TAG).e(throwable, "makeACall" + phoneNumber);
                        }
                    });
            }

            @Override
            public void addReview() {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, ReviewFragment.newInstance(Constants.classReview, classId, new ReviewAddViewModel.ReviewAddHelper() {
                    @Override
                    public void run() {
                        popBackstack();
                    }
                })).addToBackStack(null).commit();
            }

            @Override
            public void addReview(String userId) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, ReviewFragment.newInstance(Constants.classReview, classId, userId, new ReviewAddViewModel.ReviewAddHelper() {
                    @Override
                    public void run() {
                        popBackstack();
                    }
                })).addToBackStack(null).commit();
            }

            @Override
            public void backFromReview() {
                ClassDetailActivity.this.popBackstack();
            }

            @Override
            public void expandDemoClass(String s) {
                Transition mFadeTransition = new Fade();
                ClassDetailDemoPostViewModel demoPostVm = null;
                String title = null;


                if (ClassDetailViewModel.KNK.equals(s)) {
                    mSceneRoot = findViewById(R.id.scene_container1);
                    demoPostVm = new ClassDetailDemoPostViewModel(getNavigator(), ((ClassDetailViewModel) vm).connectFilterDataKNN);
                    title = ClassDetailViewModel.KNK;
                }
                if (ClassDetailViewModel.BNS.equals(s)) {
                    mSceneRoot = findViewById(R.id.scene_container2);
                    demoPostVm = new ClassDetailDemoPostViewModel(getNavigator(), ((ClassDetailViewModel) vm).connectFilterDataBNS);
                    title = ClassDetailViewModel.BNS;
                }

                if (ClassDetailViewModel.AP.equals(s)) {
                    mSceneRoot = findViewById(R.id.scene_container3);
                    title = ClassDetailViewModel.AP;
                    demoPostVm = new ClassDetailDemoPostViewModel(getNavigator(), ((ClassDetailViewModel) vm).connectFilterDataFP);
                }
                mColapsedScene = Scene.getSceneForLayout(mSceneRoot, R.layout.demo_class_colapsed_scene, ClassDetailActivity.this);
                mExpandedScene = Scene.getSceneForLayout(mSceneRoot, R.layout.demo_class_expanded_scene, ClassDetailActivity.this);
                TransitionManager.go(mExpandedScene, mFadeTransition);
                DemoClassExpandedSceneBinding expBinding = DemoClassExpandedSceneBinding.bind(mSceneRoot.getChildAt(0));
                expBinding.setTitle(title);
                expBinding.setCollapseAction(((ClassDetailViewModel) vm).collapseAction);
                expBinding.setVm(demoPostVm);

            }

            @Override
            public void compressDemoClass(String s) {
                Transition mFadeTransition = new Fade();
                String title = null;
                if (ClassDetailViewModel.KNK.equals(s)) {
                    mSceneRoot = findViewById(R.id.scene_container1);
                    title = ClassDetailViewModel.KNK;
                }
                if (ClassDetailViewModel.BNS.equals(s)) {
                    mSceneRoot = findViewById(R.id.scene_container2);
                    title = ClassDetailViewModel.BNS;
                }
                if (ClassDetailViewModel.AP.equals(s)) {
                    mSceneRoot = findViewById(R.id.scene_container3);
                    title = ClassDetailViewModel.AP;
                }

                mColapsedScene = Scene.getSceneForLayout(mSceneRoot, R.layout.demo_class_colapsed_scene, ClassDetailActivity.this);
                mExpandedScene = Scene.getSceneForLayout(mSceneRoot, R.layout.demo_class_expanded_scene, ClassDetailActivity.this);
                TransitionManager.go(mColapsedScene, mFadeTransition);
                DemoClassColapsedSceneBinding collapseBinding = DemoClassColapsedSceneBinding.bind(mSceneRoot.getChildAt(0));
                collapseBinding.setTitle(title);
                collapseBinding.setExpandAction(((ClassDetailViewModel) vm).expandAction);

            }

            @Override
            public boolean isViewEllipsized() {

                return ClassDetailActivity.this.isViewEllipsized(catalogLocationList);
            }


            @Override
            public void next() {
                onBackPressed();
            }


        };
        return new ClassDetailViewModel(getFirebaseAnalytics(), getGoogleTracker(), getHelperFactory(), uiHelper, getMessageHelper(), getNavigator(), classId,
                getIntentString("origin"), getIntentString("catalogueId"), getIntentString(Constants.promoCode), getIntentString(Constants.isIncentive), getIntentString(Constants.BG_ID));
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
            if (vm.getLoggedIn()) {
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

    public boolean isViewEllipsized(TextView view) {

        return view != null && view.getLayout() != null && (view.getLayout().getEllipsisCount(3) > 0);
    }


}

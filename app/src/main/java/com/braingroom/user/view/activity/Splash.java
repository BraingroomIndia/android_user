package com.braingroom.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.braingroom.user.R;
import com.braingroom.user.utils.SplashViewPager;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.google.firebase.messaging.FirebaseMessaging;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import me.relex.circleindicator.CircleIndicator;

import static com.braingroom.user.FCMInstanceIdService.TAG;

public class Splash extends AppCompatActivity {

    Bundle bundleReceived = new Bundle();
    Bundle bundleSend = new Bundle();
    String referralCode = "";
    String postId;
    String classId;
    String messageSenderId;
    String messageSenderName;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.find_background, R.drawable.connect_background, R.drawable.catalouge_background};
    private ArrayList<Integer> imageArray = new ArrayList<Integer>();
    private ArrayList<String> textArray = new ArrayList<>();
    final int SPLASH_DISPLAY_LENGTH = 2000;
    final int SPLASH_MENU_LENGTH = 9000;
    public boolean isClicked = false;
    private boolean isBackground = false;

    Handler handler;
    Runnable Update;


    /**
     * Duration of wait
     **/


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        init();
        final RelativeLayout splashScreen = (RelativeLayout) findViewById(R.id.splash);
        splashScreen.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                splashScreen.setVisibility(View.VISIBLE);

            }
        }, SPLASH_DISPLAY_LENGTH);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
//        ZohoSalesIQ.init(getApplication(), "vbaQbJT6pgp%2F3Bcyb2J5%2FIhGMQOrLMwCtSBDWvN719iFMGR6B8HQyg%2BYib4OymZbE8IA0L0udBo%3D", "689wH7lT2QpWpcVrcMcCOyr5GFEXO50qvrL9kW6ZUoJBV99ST2d97x9bQ72vOdCZvEyaq1slqV%2BhFd9wYVqD4%2FOv9G5EQVmggE5fHIGwHTu%2BOv301MhrYfOQ0d2CzZkt0qlz0ytPLErfXRYn5bu%2FGGbVJmRXRnWU");
        bundleSend.putBoolean("splash", true);
        bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {

            postId = bundleReceived.getString("post_id");
            classId = bundleReceived.getString("class_id");
            messageSenderId = bundleReceived.getString("sender_id");
            messageSenderName = bundleReceived.getString("sender_name");
        }
        if (postId != null) {
            bundleSend.putString("postId", postId);
            navigateActivity(PostDetailActivity.class, bundleSend);

        } else if (classId != null) {
            bundleSend.putString("id", classId);
            bundleSend.putString("origin", ClassListViewModel1.ORIGIN_HOME);
            navigateActivity(ClassDetailActivity.class, bundleSend);

        } else if (messageSenderId != null) {
            bundleSend.putString("sender_id", messageSenderId);
            bundleSend.putString("sender_name", messageSenderName);
            navigateActivity(MessagesThreadActivity.class, bundleSend);
        }

        Branch branch = Branch.getInstance();


        try {
            if (branch != null)
                branch.initSession(new Branch.BranchUniversalReferralInitListener() {
                    @Override
                    public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError branchError) {
                        //If not Launched by clicking Branch link
                        if (branchUniversalObject == null) {

                        }
                /* In case the clicked link has $android_deeplink_path the Branch will launch the MonsterViewer automatically since AutoDeeplinking feature is enabled.
                 * Launch Monster viewer activity if a link clicked without $android_deeplink_path
                 */
                        else if (!branchUniversalObject.getMetadata().containsKey("$android_deeplink_path")) {
                            HashMap<String, String> referringParams = branchUniversalObject.getMetadata();
                            if (referringParams.containsKey("referral")) {
                                referralCode = referringParams.get("referral");
                                bundleSend.putString("referralCode", referralCode);
                                isClicked = true;
                                if (!TextUtils.isEmpty(referralCode))
                                    navigateActivity(HomeActivity.class, bundleSend);
                                finish();
                            }
                        }
                    }
                }, null, this);
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d(TAG, "onCreate: " + e.toString());
        }
    }
        /*try {
            ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,false);
        } catch (Exception e){e.printStackTrace();}
*/
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/


    private void init() {

        for (int i = 0; i < XMEN.length; i++)
            imageArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SplashViewPager(Splash.this, imageArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        handler = new Handler();
        Update = new Runnable() {
            @Override
            public void run() {
                if (!isBackground) {
                    currentPage = mPager.getCurrentItem();
                    ++currentPage;
                    mPager.setCurrentItem(currentPage, true);
                    if (currentPage > 2)
                        currentPage = 0;
                    if (mPager.getCurrentItem() == 2) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isClicked && !isBackground) {
                                    navigateActivity(HomeActivity.class, bundleSend);
                                    finish();
                                }
                            }
                        }, SPLASH_MENU_LENGTH / 3);
                    }
                }
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.postDelayed(Update, SPLASH_DISPLAY_LENGTH);
            }
        }, SPLASH_MENU_LENGTH / 3, SPLASH_MENU_LENGTH / 3);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();
        isBackground = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackground = false;

    }

    public void goHome(View view) {
        isClicked = true;
        navigateActivity(HomeActivity.class, bundleSend);
        finish();
    }

    public void goConnect(View view) {
        isClicked = true;
        navigateActivity(ConnectHomeActivity.class, bundleSend);
        finish();
    }

    public void goCatalogue(View view) {
        isClicked = true;
        navigateActivity(CatalogueHomeActivity.class, bundleSend);
        finish();
    }


    public void navigateActivity(Class<?> destination, @Nullable Bundle bundle) {
        isClicked = true;
        Intent intent = new Intent(Splash.this, destination);
        intent.putExtra("classData", bundle);
        startActivity(intent);
        handler.removeCallbacks(Update);
        finish();
    }
}
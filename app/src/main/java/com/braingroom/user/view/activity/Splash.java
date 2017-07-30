package com.braingroom.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.braingroom.user.R;
import com.braingroom.user.utils.SplashViewPager;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.SplashViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.payu.magicretry.MainActivity;
/*
import com.zoho.commons.ChatComponent;
import com.zoho.livechat.android.MbedableComponent;
import com.zoho.salesiqembed.ZohoSalesIQ;
*/

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
    private static final Integer[] XMEN= {R.drawable.find_background,R.drawable.connect_background,R.drawable.catalouge_background};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    final int SPLASH_DISPLAY_LENGTH = 3000;
    public boolean isClicked = false;


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
        FirebaseMessaging.getInstance().subscribeToTopic("all");
      //  ZohoSalesIQ.init(getApplication(), "vbaQbJT6pgp%2F3Bcyb2J5%2FIhGMQOrLMwCtSBDWvN719iFMGR6B8HQyg%2BYib4OymZbE8IA0L0udBo%3D", "689wH7lT2QpWpcVrcMcCOyr5GFEXO50qvrL9kW6ZUoJBV99ST2d97x9bQ72vOdCZvEyaq1slqV%2BhFd9wYVqD4%2FOv9G5EQVmggE5fHIGwHTu%2BOv301MhrYfOQ0d2CzZkt0qlz0ytPLErfXRYn5bu%2FGGbVJmRXRnWU");

        init();



        bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            bundleSend.putBoolean("pushNotification", true);
            postId = bundleReceived.getString("post_id");
            classId = bundleReceived.getString("class_id");
            messageSenderId = bundleReceived.getString("sender_id");
            messageSenderName = bundleReceived.getString("sender_name");
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
                            }
                        }
                    }
                }, null, this);
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d(TAG, "onCreate: " + e.toString());
        }
/*        try {
            ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,false);
        } catch (Exception e){e.printStackTrace();}*/


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!isClicked)
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
                } else
                    navigateActivity(HomeActivity.class, bundleSend);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private void init() {
        for(int i=0;i<XMEN.length;i++)
            XMENArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SplashViewPager(Splash.this,XMENArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == XMEN.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },SPLASH_DISPLAY_LENGTH/3 , SPLASH_DISPLAY_LENGTH/3);
    }



    public void goHome(View view) {
        isClicked=true;
        navigateActivity(HomeActivity.class, null);
        finish();
    }

    public void goConnect(View view) {
        isClicked=true;
        navigateActivity(ConnectHomeActivity.class, null);
        finish();
    }
    public void goCatalogue(View view){
        isClicked= true;
        navigateActivity(CatalogueHomeActivity.class,null);
        finish();
    }



    public void navigateActivity(Class<?> destination, @Nullable Bundle bundle) {
        Intent intent = new Intent(Splash.this, destination);
        intent.putExtra("classData", bundle);
        startActivity(intent);
        finish();
    }
}
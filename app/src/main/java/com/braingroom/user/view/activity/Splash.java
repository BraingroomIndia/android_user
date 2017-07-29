package com.braingroom.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.zoho.commons.ChatComponent;
import com.zoho.livechat.android.MbedableComponent;
import com.zoho.salesiqembed.ZohoSalesIQ;

import java.util.HashMap;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

import static com.braingroom.user.FCMInstanceIdService.TAG;

public class Splash extends AppCompatActivity {

    Bundle bundleReceived = new Bundle();
    Bundle bundleSend = new Bundle();
    String referralCode = "";
    String postId;
    String classId;
    String messageSenderId;
    String messageSenderName;

    /**
     * Duration of wait
     **/


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        ZohoSalesIQ.init(getApplication(), "vbaQbJT6pgp%2F3Bcyb2J5%2FIhGMQOrLMwCtSBDWvN719iFMGR6B8HQyg%2BYib4OymZbE8IA0L0udBo%3D", "689wH7lT2QpWpcVrcMcCOyr5GFEXO50qvrL9kW6ZUoJBV99ST2d97x9bQ72vOdCZvEyaq1slqV%2BhFd9wYVqD4%2FOv9G5EQVmggE5fHIGwHTu%2BOv301MhrYfOQ0d2CzZkt0qlz0ytPLErfXRYn5bu%2FGGbVJmRXRnWU");

        FirebaseMessaging.getInstance().subscribeToTopic("all");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
            bundleSend.putBoolean("pushNotification", true);
            postId = bundleReceived.getString("post_id");
            classId = bundleReceived.getString("class_id");
            messageSenderId = bundleReceived.getString("sender_id");
            messageSenderName = bundleReceived.getString("sender_name");
        }

        Branch branch = Branch.getInstance();


        final int SPLASH_DISPLAY_LENGTH = 4000;
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
        try {
            ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
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


    public void goHome(View view) {
        navigateActivity(HomeActivity.class, null);
    }

    public void goConnect(View view) {
        navigateActivity(ConnectHomeActivity.class, null);
    }



    public void navigateActivity(Class<?> destination, @Nullable Bundle bundle) {
        Intent intent = new Intent(Splash.this, destination);
        intent.putExtra("classData", bundle);
        startActivity(intent);
        finish();
    }
}
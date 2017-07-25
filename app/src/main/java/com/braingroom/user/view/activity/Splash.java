package com.braingroom.user.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ClassListViewModel1;

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
        setContentView(R.layout.activity_splash);
        bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {
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
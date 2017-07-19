package com.braingroom.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.generated.callback.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.HashMap;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

import static com.braingroom.user.FCMInstanceIdService.TAG;

public class Splash extends AppCompatActivity {

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

        Branch branch = Branch.getInstance();


        final int SPLASH_DISPLAY_LENGTH = 3000;
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
                                String name = referringParams.get("referral");
                                if (!TextUtils.isEmpty(name)) {
                                    Log.d(TAG, "getReferralCode: " + name);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("referralCode", name);
                                    navigateActivity(LoginActivity.class, bundle);
                                    finish();

                                }
                            }
                        }
                    }
                }, this.getIntent().getData(), this);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: " + e.toString());
        }

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                navigateActivity(HomeActivity.class, null);
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
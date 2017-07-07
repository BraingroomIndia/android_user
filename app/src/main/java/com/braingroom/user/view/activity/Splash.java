package com.braingroom.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.braingroom.user.R;

import java.util.HashMap;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;

import static com.braingroom.user.FCMInstanceIdService.TAG;

public class Splash extends Activity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 0;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);

        Branch branch = Branch.getInstance();

        if (branch != null)
            branch.initSession(new Branch.BranchUniversalReferralInitListener() {
                @Override
                public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError branchError) {
                    //If not Launched by clicking Branch link
                    if (branchUniversalObject == null) {
                        Log.d(TAG, "onInitFinished: branchUniversalObject is null");
                    }
                /* In case the clicked link has $android_deeplink_path the Branch will launch the MonsterViewer automatically since AutoDeeplinking feature is enabled.
                 * Launch Monster viewer activity if a link clicked without $android_deeplink_path
                 */
                    else if (!branchUniversalObject.getMetadata().containsKey("$android_deeplink_path")) {
                        getReferralCode(branchUniversalObject);
                    }
                    if (linkProperties != null)
                        Log.d(TAG, "onInitFinished: " + linkProperties.toString());
                }
            }, null, null);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash.this, HomeActivity.class);
                Splash.this.startActivity(mainIntent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public void getReferralCode(BranchUniversalObject monster) {
        String referralCode = null;
        Intent intent = new Intent(Splash.this, HomeActivity.class);
        if (monster != null) {
            HashMap<String, String> referringParams = monster.getMetadata();
            if (referringParams.containsKey("referral")) {
                String name = referringParams.get("referral");
                if (!TextUtils.isEmpty(name)) {
                    referralCode = name;
                    Log.d(TAG, "getReferralCode: " + referralCode);
                    Bundle bundle = new Bundle();
                    bundle.putString("referralCode", referralCode);
                    intent = new Intent(Splash.this, LoginActivity.class);
                    intent.putExtra("classData", bundle);
                    startActivity(intent);

                }
            }
        }
        finish();

    }
}
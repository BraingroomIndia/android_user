package com.braingroom.user.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import io.branch.referral.Defines;
import io.branch.referral.InstallListener;
import io.branch.referral.PrefHelper;

/**
 * Created by godara on 06/07/17.
 */

public class FirstInstall extends BroadcastReceiver {
    private static String installID_ = "bnc_no_value";

    private static boolean isWaitingForReferrer;

    public FirstInstall() {
    }

    public static void startInstallReferrerTime(long delay) {
        isWaitingForReferrer = true;

    }

    public void onReceive(Context context, Intent intent) {
        InstallListener listener = new InstallListener();
        listener.onReceive(context, intent);
        Log.d("firstInstall", "onReceive: ");
        String rawReferrerString = intent.getStringExtra("referrer");
        if (rawReferrerString != null) {
            try {
                rawReferrerString = URLDecoder.decode(rawReferrerString, "UTF-8");
                HashMap e = new HashMap();
                String[] referralParams = rawReferrerString.split("&");
                String[] prefHelper = referralParams;
                int var7 = referralParams.length;

                for (int var8 = 0; var8 < var7; ++var8) {
                    String referrerParam = prefHelper[var8];
                    String[] keyValue = referrerParam.split("=");
                    if (keyValue.length > 1) {

                        e.put(URLDecoder.decode(keyValue[0], "UTF-8"), URLDecoder.decode(keyValue[1], "UTF-8"));
                    }
                }
                Log.d("referrerParam", "onReceive: \n" +e.toString());

                PrefHelper var13 = PrefHelper.getInstance(context);
                if (isWaitingForReferrer) {
                    if (e.containsKey(Defines.Jsonkey.LinkClickID.getKey())) {
                        installID_ = (String) e.get(Defines.Jsonkey.LinkClickID.getKey());
                        var13.setLinkClickIdentifier(installID_);
                    }

                    if (e.containsKey(Defines.Jsonkey.IsFullAppConv.getKey()) && e.containsKey(Defines.Jsonkey.ReferringLink.getKey())) {
                        var13.setIsFullAppConversion(Boolean.parseBoolean((String) e.get(Defines.Jsonkey.IsFullAppConv.getKey())));
                        var13.setAppLink((String) e.get(Defines.Jsonkey.ReferringLink.getKey()));
                    }
                }

                if (e.containsKey(Defines.Jsonkey.GoogleSearchInstallReferrer.getKey())) {
                    var13.setGoogleSearchInstallIdentifier((String) e.get(Defines.Jsonkey.GoogleSearchInstallReferrer.getKey()));
                    var13.setGooglePlayReferrer(rawReferrerString);
                }

            } catch (UnsupportedEncodingException var11) {
                var11.printStackTrace();
            } catch (IllegalArgumentException var12) {
                var12.printStackTrace();
                Log.w("BranchSDK", "Illegal characters in url encoded string");
            }
        }

    }

}

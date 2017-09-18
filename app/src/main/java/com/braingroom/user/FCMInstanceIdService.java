package com.braingroom.user;

import android.content.SharedPreferences;
import android.util.Log;

import com.braingroom.user.utils.Constants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Promolta-H on 05-02-2017.
 */

public class FCMInstanceIdService extends FirebaseInstanceIdService {

    public static final String TAG = "FCMInstanceIdService";

    @Inject
    @Named("defaultPrefEditor")
    SharedPreferences.Editor editor;

    public FCMInstanceIdService() {
        UserApplication.getInstance().getMAppComponent().inject(this);
    }


    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        editor.putBoolean(Constants.NEW_FCM, true);
        editor.putString(Constants.FCM_TOKEN, refreshedToken).commit();
    }

}
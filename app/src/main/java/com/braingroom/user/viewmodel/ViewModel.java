package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.DataflowService;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.utils.Constants;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.functions.Consumer;

public class ViewModel {

    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;
    public static final int REQ_CODE_CHOOSE_IMAGE = 1;
    public static final int REQ_CODE_CHOOSE_FILTER = 2;
    public static final int REQ_CODE_CHOOSE_VIDEO = 3;
    public static final int REQ_CODE_LOGIN = 4;
    public boolean apiSuccessful = false;
    public ConnectivityViewModel connectivityViewmodel;


    @Inject
    public DataflowService apiService;


    @Inject
    Gson gson;

    @Inject
    @Named("defaultPref")

    public SharedPreferences pref;

    @Inject
    @Named("defaultPrefEditor")
    protected
    SharedPreferences.Editor editor;

    //public final ObservableBoolean loggedInNonStatic = new ObservableBoolean(false);

    public static int notificationCount = 0;
    public static int messageCount = 0;

    /*for non reactive adapter recycler views*/
    public
    @NonNull
    List<ViewModel> nonReactiveItems = new ArrayList<>();

    public ObservableField<Integer> callAgain = new ObservableField<>(0);

    public ViewModel() {
        UserApplication.getInstance().getMAppComponent().inject(this);
        setLoggedIn();
        if (pref.getBoolean(Constants.NEW_FCM, false)) {
            apiService.registerUserDevice().subscribe(new Consumer<BaseResp>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull BaseResp resp) throws Exception {
                    if (resp.getResCode()!=null)
                        editor.putBoolean(Constants.NEW_FCM,false);

                }
            });
        }
    }

    public void logOut() {

        String fcmToken = pref.getString(Constants.FCM_TOKEN, "");
        Log.d("Firebase", "fcm Token: " + fcmToken);
        apiService.logout().subscribe();
        editor.clear();
        editor.putString(Constants.FCM_TOKEN, fcmToken);
        editor.commit();
        LoginManager.getInstance().logOut();


    }

    public void login(String name, String email, String profilePic, String userId, String uuid) {
        editor.putBoolean(Constants.LOGGED_IN, true);
        editor.putString(Constants.NAME, name);
        editor.putString(Constants.EMAIL, email);
        editor.putString(Constants.PROFILE_PIC, profilePic);
        editor.putString(Constants.BG_ID, userId);
        editor.putString(Constants.UUID, uuid);
        editor.commit();
        setLoggedIn();

    }


    public void init() {
    }

    public void paginate() {
    }


    public void handleActivityResult(final int requestCode, int resultCode, Intent data) {
    }

    public void retry() {
        callAgain.set(callAgain.get() + 1);
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public boolean isValidPhoneNo(CharSequence phoneNo) {
        if (!TextUtils.isEmpty(phoneNo)) {
            return android.util.Patterns.PHONE.matcher(phoneNo).matches();
        }
        return false;
    }
    public void setLoggedIn(){
        UserApplication.getInstance().loggedIn=pref.getBoolean(Constants.LOGGED_IN, false);
    }
    public boolean getLoggedIn(){
       return UserApplication.getInstance().loggedIn;
    }
}

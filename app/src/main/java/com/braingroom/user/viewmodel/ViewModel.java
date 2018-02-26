package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.BuildConfig;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.DataflowService;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.Navigator;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import lombok.Setter;
import timber.log.Timber;

import static com.braingroom.user.utils.CommonUtils.sendCustomEvent;

public class ViewModel extends BaseObservable {

    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0;
    public static final int REQ_CODE_CHOOSE_IMAGE = 1;
    public static final int REQ_CODE_CHOOSE_FILTER = 2;
    public static final int REQ_CODE_CHOOSE_VIDEO = 3;

    public static final int REQ_CODE_LOGIN = 4;
    public static final int REQ_CODE_STRIPE = 6;
    public static final int REQ_CODE_PLAY_VIDEO = 5;
    public boolean apiSuccessful = false;
    public ConnectivityViewModel connectivityViewmodel;

    @Setter
    protected Tracker mTracker;

    public final String rupeesSymbol = "&#x20b9;";

    public String BG_ID;

    public static boolean versionChecked = false;

    @Setter
    protected FirebaseAnalytics mFirebaseAnalytics;

    @Inject
    public DataflowService apiService;

    public final String TAG;


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
        BG_ID = pref.getString(Constants.BG_ID, "");
        setLoggedIn();

        TAG = this.getClass().getSimpleName();

    }

    public void logOut() {

        String fcmToken = pref.getString(Constants.FCM_TOKEN, "");
        String referralCode = pref.getString(Constants.referralCode, "");
        Timber.tag(TAG).d("Firebase", "fcm Token: " + fcmToken);
        apiService.logout().observeOn(AndroidSchedulers.mainThread()).subscribe();
        editor.putString(Constants.BG_ID, "");
        editor.putBoolean(Constants.LOGGED_IN, false);
        editor.putString(Constants.EMAIL, "");
        editor.putString(Constants.NAME, "");
        editor.putString(Constants.UUID, "");
        editor.putString(Constants.PROFILE_PIC, "");
        editor.commit();
        editor.apply();
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
        BG_ID = pref.getString(Constants.BG_ID, "");
    }

    public void retry() {
        callAgain.set(callAgain.get() + 1);
    }

    public void onPause() {
    }

    public void onResume() {
    }

    public void onUserInteraction() {

    }


    boolean isValidPhoneNo(CharSequence phoneNo) {
        return !isEmpty(phoneNo) && android.util.Patterns.PHONE.matcher(phoneNo).matches();
    }

    public void setLoggedIn() {
        UserApplication.getInstance().loggedIn = pref.getBoolean(Constants.LOGGED_IN, false);
    }

    public boolean getLoggedIn() {
        return pref.getBoolean(Constants.LOGGED_IN, false);
    }

    public boolean isEmpty(String data) {
        return data == null || data.equals("") || data.trim().equals("");
    }

    public boolean isEmpty(List<?> data) {
        return data == null || data.isEmpty() || data.get(0) == null;
    }

    void setScreenName(String screenName) {
        if (!BuildConfig.DEBUG) {
            mTracker.setScreenName(screenName);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }

    }

    public void setCustomEvent(String category, String actionId, String label, boolean interaction) {
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(actionId)
                .setLabel(label)
                .setNonInteraction(!interaction)
                .build());
    }

    public boolean isEmpty(Object data) {
        return data == null;
    }

}

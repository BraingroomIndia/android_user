package com.braingroom.user;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.braingroom.user.utils.AppComponent;
import com.braingroom.user.utils.AppModule;
import com.braingroom.user.utils.BindingAdapters;
import com.braingroom.user.utils.BindingUtils;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.DaggerAppComponent;
import com.braingroom.user.utils.ProductionTree;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
/*import com.squareup.leakcanary.RefWatcher;*/

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import io.branch.referral.Branch;
import io.branch.referral.BranchApp;
import io.branch.referral.PrefHelper;
import io.fabric.sdk.android.Fabric;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;
import timber.log.Timber;

import static com.google.android.gms.ads.identifier.AdvertisingIdClient.getAdvertisingIdInfo;


public class UserApplication extends BranchApp {


    private static final String TAG = UserApplication.class.getSimpleName();

    public static String BASE_URL = "https://www.braingroom.com/apis/";
    public static String DEV_BASE_URL = "https://dev.braingroom.com/apis/";

    private static UserApplication sInstance;

    @Getter
    PublishSubject<Boolean> internetStatusBus;

    @Getter
    PublishSubject<Boolean> newNotificationBus;

    @Getter
    PublishSubject<String> otpArrived;

    @Getter
    public TypedArray classPlaceholder;

    public boolean loggedIn = false;

    public static int versionCode = BuildConfig.VERSION_CODE;
//

    public static boolean locationSettingPopup;
    @Getter
    private AppComponent mAppComponent;
    private Map<String, Typeface> fontCache = new HashMap<>();

    private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        return getInstance().mRefWatcher;
    }

    // Google Analytics
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;
    public static String DeviceFingerPrintID;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new ProductionTree(this));
        }
        Branch.getAutoInstance(this);
        DeviceFingerPrintID = PrefHelper.getInstance(this).getDeviceFingerPrintID();

        try {
            versionCode = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sAnalytics = GoogleAnalytics.getInstance(this);
      /*  if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        mRefWatcher = LeakCanary.install(this);*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        Stetho.initializeWithDefaults(this);
//        if (BuildConfig.DEBUG) {
//            Timber.plant(new Timber.DebugTree());
//        } else {
//            Timber.plant(new CrashReportingTree());
//        }
        sInstance = this;
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this, BASE_URL)).build();
        BindingUtils.setDefaultBinder(BindingAdapters.defaultBinder);
        internetStatusBus = PublishSubject.create();
        newNotificationBus = PublishSubject.create();
        otpArrived = PublishSubject.create();
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        if (sTracker == null) {
            sTracker = sAnalytics.newTracker(R.xml.global_tracker);
            sTracker.enableAutoActivityTracking(true);
            sTracker.enableExceptionReporting(true);
        }

        return sTracker;
    }

    //fragment_featured_post

    public static UserApplication getInstance() {
        return sInstance;
    }

    public Typeface getFont(String key) {
        return fontCache.get(key);
    }

    public void putFontCache(String key, String name) {
        if (fontCache.get(key) == null) {
            fontCache.put(key, Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/" + name));
        } else {
            Timber.tag(TAG).d("font already in cache " + name);
        }
    }

    public void setRegularTypeface(TextView textView) {
        if (getFont(Constants.FONT_REGULAR) == null)
            putFontCache(Constants.FONT_REGULAR, Constants.FONT_REGULAR);
        textView.setTypeface(getFont(Constants.FONT_REGULAR));
    }

    public void setBoldTypeface(TextView textView) {
        if (getFont(Constants.FONT_BOLD) == null)
            putFontCache(Constants.FONT_BOLD, Constants.FONT_BOLD);
        textView.setTypeface(getFont(Constants.FONT_BOLD));
    }

//    private static class CrashReportingTree extends Timber.Tree {
//        @Override
//        protected void log(int priority, String tag, String message, Throwable t) {
//            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
//                return;
//            }
//            if (t != null) {
//                if (priority == Log.ERROR) {
//                    FirebaseCrash.report(t);
//                }
//            }
//        }
//    }

}

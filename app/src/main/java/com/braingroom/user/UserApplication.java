package com.braingroom.user;

import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.multidex.MultiDex;
import android.widget.TextView;

import com.braingroom.user.utils.AppComponent;
import com.braingroom.user.utils.AppModule;
import com.braingroom.user.utils.BindingAdapters;
import com.braingroom.user.utils.BindingUtils;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.DaggerAppComponent;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;
/*import com.squareup.leakcanary.RefWatcher;*/

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;
import timber.log.Timber;


public class UserApplication extends Application {


    private static final String TAG = UserApplication.class.getSimpleName();

    public static String BASE_URL = "https://www.braingroom.com/apis/";
    //Edited By Vikas Godara
    public static String DEV_BASE_URL = "https://dev.braingroom.com/apis/";
    //Edited By Vikas Godara

    private static UserApplication sInstance;

    @Getter
    PublishSubject<Boolean> internetStatusBus;

    @Getter
    public TypedArray classPlaceholder;

    @Getter
    private AppComponent mAppComponent;
    private Map<String, Typeface> fontCache = new HashMap<>();
   /* private RefWatcher mRefWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        return getInstance().mRefWatcher;
    }
*/

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Fabric.with(this, new Crashlytics());
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        mRefWatcher = LeakCanary.install(this);

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

    }

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

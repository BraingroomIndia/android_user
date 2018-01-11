package com.braingroom.user.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.DataflowService;
import com.braingroom.user.model.QRCode.ClassBooking;
import com.braingroom.user.model.QRCode.ClassDetail;
import com.braingroom.user.model.QRCode.ClassListing;
import com.braingroom.user.model.QRCode.ConnectListing;
import com.braingroom.user.model.QRCode.PostDetail;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.SplashViewPager;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.FilterViewModel;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.relex.circleindicator.CircleIndicator;

import static com.braingroom.user.FCMInstanceIdService.TAG;

public class Index extends AppCompatActivity {

    Bundle bundleReceived = new Bundle();
    Bundle bundleSend = new Bundle();
    String referralCode = "";
    private ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.find_background, R.drawable.connect_background, R.drawable.catalouge_background};
    private ArrayList<Integer> imageArray = new ArrayList<>();
    private ArrayList<String> textArray = new ArrayList<>();
    final int SPLASH_DISPLAY_LENGTH = 3000;
    final int SPLASH_MENU_LENGTH = 9000;
    public boolean isClicked = false;
    private boolean isBackground = false;

    Handler handler;
    Runnable Update;
    @Inject
    @Named("defaultPref")
    public SharedPreferences pref;
    @Inject
    Gson gson;
    @Inject
    public DataflowService apiService;
    private MessageHelper messageHelper;
    public Toast toast;
    MaterialDialog progressDialog;
    RxPermissions rxPermissions;
    boolean firstRun = true;


    @Override
    public void onCreate(Bundle icicle) {
        UserApplication.getInstance().getMAppComponent().inject(this);
        super.onCreate(icicle);
        bundleReceived = getIntent().getExtras();
        isBackground = false;
        rxPermissions = new RxPermissions(Index.this);
        apiService.checkGeoDetail();
        apiService.forceUpdate().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean != null && aBoolean) {
                    showAcceptableInfo(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            openPlayStore();
                        }
                    });
                }
            }
        });


        setContentView(R.layout.activity_splash);
        init();
        FirebaseMessaging.getInstance().subscribeToTopic("all");


    }


    private void init() {

        imageArray.addAll(Arrays.asList(XMEN));

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SplashViewPager(Index.this, imageArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        handler = new Handler();
        Update = new Runnable() {
            @Override
            public void run() {
                if (!isBackground) {
                    currentPage = mPager.getCurrentItem();
                    ++currentPage;
                    if (currentPage > 2)
                        currentPage = 0;
                    mPager.setCurrentItem(currentPage, true);

                }
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.postDelayed(Update, SPLASH_DISPLAY_LENGTH);
            }
        }, SPLASH_MENU_LENGTH / 3, SPLASH_MENU_LENGTH / 3);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isBackground = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackground = false;
        isClicked = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        firstRun = false;
    }

    public void goHome(View view) {
        navigateActivity(HomeActivity.class, bundleSend);
    }

    public void goConnect(View view) {
        if (pref.getBoolean(Constants.HIDE_CONNECT_SECTIONS, false))
            navigateActivity(ConnectHomeActivity.class, bundleSend);
        else
            navigateActivity(ConnectSectionListActivity.class, bundleSend);
    }

    public void goCatalogue(View view) {
        navigateActivity(CatalogueHomeActivity.class, bundleSend);
    }

    public void openQrCode(final View view) {
        rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean != null && aBoolean) {
                    isBackground = false;
                    navigateActivity(QRCodeReaderActivity.class, null);
                } else askAgain(view);
            }
        });


    }

    public void askAgain(final View view) {
        rxPermissions.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    rxPermissions.request(Manifest.permission.CAMERA).subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean aBoolean) throws Exception {
                            if (aBoolean != null && aBoolean) {
                                isBackground = false;
                                navigateActivity(QRCodeReaderActivity.class, null);
                            } else openQrCode(view);
                        }
                    });
                } else openPermissionSettings();
            }
        });

    }


    public void navigateActivity(Class<?> destination, @Nullable Bundle bundle) {
        if (isClicked || isBackground)
            return;
        isClicked = true;
        Intent intent = new Intent(Index.this, destination);
        intent.putExtra("classData", bundle);
        startActivity(intent);
    }


    public void forceUpdate() {
        apiService.forceUpdate().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@NonNull Boolean aBoolean) throws Exception {
                if (aBoolean != null && aBoolean) {
                    showAcceptableInfo(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            openPlayStore();
                        }
                    });
                }
            }
        });

    }

    public void showAcceptableInfo(@NonNull MaterialDialog.SingleButtonCallback positiveCallback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(Index.this);
        builder.title("Update").
                content("Please Update the app").
                theme(Theme.LIGHT).
                positiveText("Update").autoDismiss(false).cancelable(false).
                onNegative(positiveCallback).
                onNeutral(positiveCallback).
                onPositive(positiveCallback);
        builder.show();
    }

    public void openPlayStore() {
        isClicked = true;
        final String appPackageName = UserApplication.getInstance().getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();

    }

    @NonNull
    public MessageHelper getMessageHelper() {
        if (messageHelper == null)
            messageHelper = new MessageHelper() {
                @Override
                public void show(String message) {
                    if (toast != null) {
                        toast.cancel();
                    }
                    dismissActiveProgress();
                    toast = Toast.makeText(Index.this, message, Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public void showLoginRequireDialog(String content, final Bundle data) {

                }

                @Override
                public void showDismissInfo(String title, String content) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Index.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.positiveText("Dismiss").show();
                }

                @Override
                public void showDismissInfo(@Nullable String title, @android.support.annotation.NonNull Spanned content) {

                }

                @Override
                public void showDismissInfo(@Nullable String title, @NonNull String buttonText, @NonNull String content) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Index.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.positiveText(buttonText).show();
                }

                @Override
                public void showAcceptableInfo(@Nullable String title, @NonNull String content, @NonNull MaterialDialog.SingleButtonCallback positiveCallback) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Index.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.onPositive(positiveCallback);
                    builder.positiveText("OK").show();

                }

                @Override
                public void showAcceptableInfo(@Nullable String title, @android.support.annotation.NonNull Spanned content, @android.support.annotation.NonNull MaterialDialog.SingleButtonCallback positiveCallback) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Index.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.onPositive(positiveCallback);
                    builder.positiveText("OK").show();
                }

                @Override
                public void showAcceptableInfo(@Nullable String title, @NonNull String content, String postiveText, @NonNull MaterialDialog.SingleButtonCallback positiveCallback) {
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Index.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.onPositive(positiveCallback);
                    builder.positiveText(postiveText).show();
                }

                @Override
                public void showAcceptDismissInfo(@Nullable String title, @android.support.annotation.NonNull String content, @android.support.annotation.NonNull MaterialDialog.SingleButtonCallback positiveCallback) {

                }


                @Override
                public void showProgressDialog(@Nullable String title, @NonNull String content) {
                    progressDialog = new MaterialDialog.Builder(Index.this)
                            .title(title != null ? title : "")
                            .content(content)
                            .progress(true, 0)
//                .canceledOnTouchOutside(false)
                            .show();

                }


                @Override
                public void dismissActiveProgress() {
                    if (progressDialog != null && progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            };
        return messageHelper;
    }


    public void openPermissionSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + this.getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }
}




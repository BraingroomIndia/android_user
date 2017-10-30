package com.braingroom.user.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
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
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import me.relex.circleindicator.CircleIndicator;

import static com.braingroom.user.FCMInstanceIdService.TAG;

public class Splash extends AppCompatActivity {

    Bundle bundleReceived = new Bundle();
    Bundle bundleSend = new Bundle();
    String referralCode = "";
    String postId;
    String classId;
    String messageSenderId;
    String messageSenderName;
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

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        isBackground = false;
        rxPermissions = new RxPermissions(Splash.this);
        UserApplication.getInstance().getMAppComponent().inject(this);
        apiService.forceUpdate().subscribe(new Consumer<Boolean>() {
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
        final ConstraintLayout splashScreen = (ConstraintLayout) findViewById(R.id.splash);
        splashScreen.setVisibility(View.INVISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                splashScreen.setVisibility(View.VISIBLE);
            }
        }, SPLASH_DISPLAY_LENGTH);
        FirebaseMessaging.getInstance().subscribeToTopic("all");
//        ZohoSalesIQ.init(getApplication(), "vbaQbJT6pgp%2F3Bcyb2J5%2FIhGMQOrLMwCtSBDWvN719iFMGR6B8HQyg%2BYib4OymZbE8IA0L0udBo%3D", "689wH7lT2QpWpcVrcMcCOyr5GFEXO50qvrL9kW6ZUoJBV99ST2d97x9bQ72vOdCZvEyaq1slqV%2BhFd9wYVqD4%2FOv9G5EQVmggE5fHIGwHTu%2BOv301MhrYfOQ0d2CzZkt0qlz0ytPLErfXRYn5bu%2FGGbVJmRXRnWU");
        bundleSend.putBoolean("splash", true);
        bundleReceived = getIntent().getExtras();
        if (bundleReceived != null) {

            postId = bundleReceived.getString("post_id");
            classId = bundleReceived.getString("class_id");
            messageSenderId = bundleReceived.getString("sender_id");
            messageSenderName = bundleReceived.getString("sender_name");
        }
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
        }

        Branch branch = Branch.getInstance();
        try {
            if (branch != null)
                branch.initSession(new Branch.BranchUniversalReferralInitListener() {
                    @Override
                    public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError branchError) {
                        //If not Launched by clicking Branch link
                        if (branchUniversalObject == null) {
                            return;
                        }
                /* In case the clicked link has $android_deeplink_path the Branch will launch the MonsterViewer automatically since AutoDeeplinking feature is enabled.
                 * Launch Monster viewer activity if a link clicked without $android_deeplink_path
                 */
                        else if (!branchUniversalObject.getMetadata().containsKey("$android_deeplink_path")) {
                            HashMap<String, String> referringParams = branchUniversalObject.getMetadata();
                            if (referringParams.containsKey("referral")) {
                                referralCode = referringParams.get("referral");
                                bundleSend.putString("referralCode", referralCode);
                                isClicked = true;
                                if (!TextUtils.isEmpty(referralCode))
                                    navigateActivity(HomeActivity.class, bundleSend);
                            } else if (referringParams.containsKey("qrcode")) {
                                qrCodeData(referringParams.get("qrcode"));
                            }
                        }
                    }
                }, this.getIntent().getData(), this);
        } catch (Exception e) {
            //e.printStackTrace();
            Log.d(TAG, "onCreate: " + e.toString());
        }
    }
        /*try {
            ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,false);
        } catch (Exception e){e.printStackTrace();}
*/
        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/


    private void init() {

        for (int i = 0; i < XMEN.length; i++)
            imageArray.add(XMEN[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SplashViewPager(Splash.this, imageArray));
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
                if (aBoolean != null && aBoolean)
                    navigateActivity(QRCodeReaderActivity.class, bundleSend);
                else openQrCode(view);
            }
        });

    }


    public void navigateActivity(Class<?> destination, @Nullable Bundle bundle) {
        if (isClicked || isBackground)
            return;
        isClicked = true;
        Intent intent = new Intent(Splash.this, destination);
        intent.putExtra("classData", bundle);
        startActivity(intent);
    }

    private void qrCodeData(final String json) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Bundle bundle = new Bundle();
                if (json == null) {
                    return;
                }

                if (json.contains("class_listing")) {

                    ClassListing data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ClassListing.class);
                    FilterData filterData = gson.fromJson(gson.toJson(data.reqData), FilterData.class);
                    bundle.putSerializable(Constants.classFilterData, filterData);

                    if (filterData.getCatalog() != null && !filterData.getCatalog().isEmpty())
                        bundle.putString(Constants.origin, FilterViewModel.ORIGIN_CATALOG);
                    else
                        bundle.putString(Constants.origin, FilterViewModel.ORIGIN_HOME);
                    try {
                        HashMap<String, Integer> categoryFilterMap = new HashMap<>();
                        HashMap<String, Integer> segmentsFilterMap = new HashMap<>();
                        HashMap<String, String> cityFilterMap = new HashMap<>();
                        HashMap<String, String> localityFilterMap = new HashMap<>();
                        HashMap<String, Integer> communityFilterMap = new HashMap<>();
                        HashMap<String, Integer> classTypeFilterMap = new HashMap<>();
                        HashMap<String, Integer> classScheduleFilterMap = new HashMap<>();
                        HashMap<String, String> vendorListFilterMap = new HashMap<>();

                      /*  if (!isEmpty(filterData.getCategoryId()))
                            categoryFilterMap.put("", Integer.valueOf(filterData.getCategoryId()));
                        if (!isEmpty(filterData.getSegmentId()))
                            segmentsFilterMap.put("", Integer.valueOf(filterData.getSegmentId()));
                        if (!isEmpty(filterData.getCommunityId()))
                            communityFilterMap.put("", Integer.valueOf(filterData.getCommunityId()));
                        if (!isEmpty(filterData.getClassType()))
                            classTypeFilterMap.put("", Integer.valueOf(filterData.getClassType()));
                        if (!isEmpty(filterData.getClassSchedule()))
                            classScheduleFilterMap.put("", Integer.valueOf(filterData.getClassSchedule()));
                        if (!isEmpty(filterData.getClassProvider()))
                            vendorListFilterMap.put("", filterData.getClassProvider());
                        if (!isEmpty(filterData.getCity()))
                            cityFilterMap.put("", filterData.getCity());*/
                        bundle.putSerializable(Constants.classFilterData, filterData);
                        bundle.putSerializable(Constants.categoryFilterMap, categoryFilterMap);
                        bundle.putSerializable(Constants.segmentsFilterMap, segmentsFilterMap);
                        bundle.putSerializable(Constants.cityFilterMap, cityFilterMap);
                        bundle.putSerializable(Constants.localityFilterMap, localityFilterMap);
                        bundle.putSerializable(Constants.communityFilterMap, communityFilterMap);
                        bundle.putSerializable(Constants.classTypeFilterMap, classTypeFilterMap);
                        bundle.putSerializable(Constants.classScheduleFilterMap, classScheduleFilterMap);
                        bundle.putSerializable(Constants.vendorListFilterMap, vendorListFilterMap);
                        navigateActivity(ClassListActivity.class, bundle);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (json.contains("connect_listing")) {
                    ConnectListing data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ConnectListing.class);
                    ConnectFilterData connectFilterData = gson.fromJson(gson.toJson(data.reqData), ConnectFilterData.class);
                    bundle.putSerializable(Constants.connectFilterData, connectFilterData);
                    navigateActivity(ConnectHomeActivity.class, bundle);
                } else if (json.contains("class_detail")) {
                    ClassDetail data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ClassDetail.class);
                    bundle.putString("id", data.reqData.getId());
                    bundle.putString(Constants.origin, ClassListViewModel1.ORIGIN_HOME);
                    bundle.putString(Constants.promoCode,data.reqData.getPromoCode());
                    navigateActivity(ClassDetailActivity.class, bundle);
                } else if (json.contains("post_detail")) {
                    PostDetail data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), PostDetail.class);
                    bundle.putString("postId", data.reqData.getPostId());
                    navigateActivity(PostDetailActivity.class, bundle);
                } else if (json.contains("class_booking")) {
                    final ClassBooking data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ClassBooking.class);
                    apiService.getClassDetail(data.reqData.id, 0).onErrorReturn(new Function<Throwable, ClassData>() {
                        @Override
                        public ClassData apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            return new ClassData();
                        }
                    }).subscribe(new Consumer<ClassData>() {
                        @Override
                        public void accept(@NonNull ClassData classData) throws Exception {
                            if (classData != null) {
                                bundle.putSerializable("classData", classData);
                                bundle.putSerializable("checkoutType", "class");
                                bundle.putString(Constants.promoCode, data.reqData.promoCode);
                                navigateActivity(CheckoutActivity.class, bundle);
                            }
                        }
                    });
                }

            }
        });
    }



    public void forceUpdate(){
        apiService.forceUpdate().subscribe(new Consumer<Boolean>() {
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
        MaterialDialog.Builder builder = new MaterialDialog.Builder(Splash.this);
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
                    toast = Toast.makeText(Splash.this, message, Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public void showLoginRequireDialog(String content, final Bundle data) {

                }

                @Override
                public void showDismissInfo(String title, String content) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Splash.this);
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
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Splash.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.positiveText(buttonText).show();
                }

                @Override
                public void showAcceptableInfo(@Nullable String title, @NonNull String content, @NonNull MaterialDialog.SingleButtonCallback positiveCallback) {
                    dismissActiveProgress();
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Splash.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.onPositive(positiveCallback);
                    builder.positiveText("OK").show();

                }

                @Override
                public void showAcceptableInfo(@Nullable String title, @NonNull String content, String postiveText, @NonNull MaterialDialog.SingleButtonCallback positiveCallback) {
                    MaterialDialog.Builder builder = new MaterialDialog.Builder(Splash.this);
                    if (title != null) builder.title(title);
                    builder.content(content);
                    builder.onPositive(positiveCallback);
                    builder.positiveText(postiveText).show();
                }

                @Override
                public void showAcceptDismissInfo() {
                }

                @Override
                public void showProgressDialog(@Nullable String title, @NonNull String content) {
                    progressDialog = new MaterialDialog.Builder(Splash.this)
                            .title(title)
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


}




package com.braingroom.user.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

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
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.viewmodel.ClassDetailViewModel;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.FilterViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;
import static com.braingroom.user.FCMInstanceIdService.TAG;
import static com.braingroom.user.utils.CommonUtils.sendCustomEvent;

/*
 * Created by godara on 17/11/17.
 */

public class Splash extends AppCompatActivity {

    Bundle bundleReceived;
    Branch branch;
    @Inject
    @Named("defaultPrefEditor")
    protected
    SharedPreferences.Editor editor;
    @Inject
    Gson gson;
    @Inject
    public DataflowService apiService;
    @Inject
    @Named("defaultPref")
    public SharedPreferences pref;

    public final String TAG = Splash.class.getSimpleName();

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        UserApplication.getInstance().getMAppComponent().inject(this);
        Log.d(TAG, "onCreate: Called  ");
        branch = Branch.getInstance();
        apiService.checkGeoDetail();
        if (getIntent().getExtras() != null)
            bundleReceived = getIntent().getExtras().getBundle(Constants.pushNotification);
        branchData();
    }


    public void navigateActivity(Class<?> destination, @Nullable Bundle bundle) {
        try {
            Log.d(this.getClass().getSimpleName(), "FCM token: " + pref.getString(Constants.FCM_TOKEN, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Splash.this, destination);
        intent.putExtra("classData", bundle);
        startActivity(intent);
        finish();
    }

    private void pushNotification() {
        String postId = null;
        String classId = null;
        String messageSenderId = null;
        String messageSenderName = null;
        Bundle bundle = new Bundle();
        //if onMessageReceived called
        if (bundleReceived != null) {
            bundle.putBoolean(Constants.pushNotification, true);
            HashMap<String, String> data = gson.fromJson(bundleReceived.getString(Constants.pushNotification), new TypeToken<HashMap<String, String>>() {
            }.getType());
            Log.d(TAG, "hashMap data" + data.toString());
            bundle.putString("notification_id", data.get("notification_id"));
            postId = data.get("post_id");
            classId = data.get("class_id");
            messageSenderId = data.get("sender_id");
            messageSenderName = data.get("sender_name");
            // if onMessageReceived not called
        } else if (getIntent().getExtras() != null) {
            postId = getIntent().getExtras().getString("post_id");
            classId = getIntent().getExtras().getString("class_id");
            messageSenderId = getIntent().getExtras().getString("sender_id");
            messageSenderName = getIntent().getExtras().getString("sender_name");
        } else {
            navigateActivity(Index.class, null);
            return;
        }
        if (postId != null) {
            bundle.putString("postId", postId);
            navigateActivity(PostDetailActivity.class, bundle);
        } else if (classId != null) {
            bundle.putString("id", classId);
            bundle.putString(Constants.origin, ClassListViewModel1.ORIGIN_HOME);
            navigateActivity(ClassDetailActivity.class, bundle);
        } else if (messageSenderId != null && messageSenderName != null) {
            bundle.putString("sender_id", messageSenderId);
            bundle.putString("sender_name", messageSenderName);
            if ("0".equalsIgnoreCase(messageSenderId))
                //If message is from admin open Inbox
                navigateActivity(MessageActivity.class, bundle);
            else
                // For every one else open Chat thread
                navigateActivity(MessagesThreadActivity.class, bundle);

        } else {
            navigateActivity(Index.class, null);
        }


    }

    private void navigateToIndex() {
        navigateActivity(Index.class, null);
    }

    private void qrCodeData(final String json) {

        final Bundle bundle = new Bundle();
        if (json == null) {
            return;
        }
        if (json.contains("class_listing")) {
            final ClassListing data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ClassListing.class);
            apiService.getFilterData(data.reqData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<FilterData>() {
                @Override
                public void accept(FilterData filterData) throws Exception {
                    bundle.putSerializable(Constants.classFilterData, filterData);
                    bundle.putString(Constants.promoCode, data.promoCode);
                    bundle.putSerializable(Constants.origin, FilterViewModel.ORIGIN_HOME);
                    bundle.putString(Constants.isIncentive, data.isIncentive);
                    navigateActivity(ClassListActivity.class, bundle);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    navigateToIndex();
                    throwable.printStackTrace();
                }
            });

        } else if (json.contains("connect_listing")) {
            try {
                ConnectListing data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ConnectListing.class);
                ConnectFilterData connectFilterData = gson.fromJson(gson.toJson(data.reqData), ConnectFilterData.class);
                bundle.putSerializable(Constants.connectFilterData, connectFilterData);
                navigateActivity(ConnectHomeActivity.class, bundle);
            } catch (Exception e) {
                navigateToIndex();
                e.printStackTrace();
            }

        } else if (json.contains("class_detail")) {

            try {
                ClassDetail data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ClassDetail.class);
                bundle.putString("id", data.reqData.getId());
                bundle.putString(Constants.origin, ClassListViewModel1.ORIGIN_HOME);
                bundle.putString(Constants.promoCode, data.reqData.getPromoCode());
                navigateActivity(ClassDetailActivity.class, bundle);
            } catch (Exception e) {
                e.printStackTrace();
                navigateToIndex();
            }
        } else if (json.contains("post_detail")) {

            try {
                PostDetail data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), PostDetail.class);
                bundle.putString("postId", data.reqData.getPostId());
                navigateActivity(PostDetailActivity.class, bundle);
            } catch (Exception e) {
                e.printStackTrace();
                navigateToIndex();
            }
        } else if (json.contains("class_booking")) {
            final ClassBooking data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ClassBooking.class);
            apiService.getClassDetail(data.reqData.id, 0).onErrorReturn(new Function<Throwable, ClassData>() {
                @Override
                public ClassData apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                    return new ClassData();
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Consumer<ClassData>() {
                @Override
                public void accept(@NonNull ClassData classData) throws Exception {
                    if (classData != null) {
                        bundle.putSerializable("classData", classData);
                        bundle.putSerializable("checkoutType", "class");
                        bundle.putString(Constants.promoCode, data.reqData.promoCode);
                        navigateActivity(CheckoutActivity.class, bundle);
                    }
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                    navigateToIndex();
                }
            });
        } else navigateToIndex();

    }


    private void branchData() {
        try {
            if (branch != null)
                branch.initSession(new Branch.BranchUniversalReferralInitListener() {
                    @Override
                    public void onInitFinished(BranchUniversalObject branchUniversalObject, LinkProperties linkProperties, BranchError branchError) {
                        //If not Launched by clicking Branch link
                        if (branchUniversalObject == null)
                            pushNotification();
                        /* In case the clicked link has $android_deeplink_path the Branch will launch the MonsterViewer automatically since AutoDeeplinking feature is enabled.
                         Launch Monster viewer activity if a link clicked without $android_deeplink_path
                        */
                        else if (!branchUniversalObject.getMetadata().containsKey("$android_deeplink_path")) {
                            HashMap<String, String> referringParams = branchUniversalObject.getMetadata();
                            if (referringParams.containsKey("referral")) {
                                String referralCode = referringParams.get("referral");
                                Bundle bundle = new Bundle();
                                bundle.putString("referralCode", referralCode);
                                if (!isEmpty(referralCode)) {
                                    editor.putString(Constants.referralCode, referralCode).apply();
                                    // Splash>-HomePage->Login
                                    navigateActivity(HomeActivity.class, bundle);
                                }
                            } else if (referringParams.containsKey("qrcode"))
                                // Process qr code data
                                qrCodeData(referringParams.get("qrcode"));

                                //redirect to Index Page
                            else navigateActivity(Index.class, null);
                        }
                        // redirect to Index Page
                        else navigateActivity(Index.class, null);
                    }
                }, this.getIntent().getData(), this);
        } catch (Exception e) {
            Log.d(TAG, "Branch error: " + e.getLocalizedMessage());
            navigateToIndex();
            e.printStackTrace();

        }
    }
}

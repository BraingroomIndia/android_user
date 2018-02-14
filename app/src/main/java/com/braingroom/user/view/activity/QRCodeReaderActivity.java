package com.braingroom.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.braingroom.user.BuildConfig;
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
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.FilterViewModel;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import info.androidhive.barcode.BarcodeReader;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

import static com.braingroom.user.FCMInstanceIdService.TAG;

/*
 * Created by godara on 16/10/17.
 */

public class QRCodeReaderActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private Navigator navigator;
    private static final String TAG = QRCodeReaderActivity.class.getSimpleName();
    private BarcodeReader barcodeReader;
    @Inject
    Gson gson;

    @Inject
    OkHttpClient client;
    String barCodeUrl;
    @Inject
    public DataflowService apiService;

    public String baseUrl = "";
    public String branchKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UserApplication.getInstance().getMAppComponent().inject(this);
        setContentView(R.layout.activity_qrcode_reader);
        baseUrl = getString(R.string.branch_base_url);
        branchKey = getString(R.string.branch_key);
        // getting barcode instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_fragment);

        /*
         * Providing beep sound. The sound file has to be placed in
         * `assets` folder
         */
        // barcodeReader.setBeepSoundFile("shutter.mp3");

        /*
         * Pausing / resuming barcode reader. This will be useful when you want to
         * do some foreground user interaction while leaving the barcode
         * reader in background
         * */
        // barcodeReader.pauseScanning();
        // barcodeReader.resumeScanning();
    }

    @Override
    public void onScanned(final Barcode barcode) {
        Timber.tag(TAG).d("onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();
        Bundle bundle = new Bundle();
        if (barCodeUrl != null && barCodeUrl.contains(baseUrl))
            return;
        if (barcode.displayValue == null) {
            return;
        }

        if (barcode.displayValue.contains(baseUrl)) {
            barCodeUrl = barcode.displayValue;
            qrCodeData(getBranchUrlData(barCodeUrl));
        }


    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Timber.tag(TAG).d("onScannedMultiple: " + barcodes.size());
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {

    }

    @Override
    public void onCameraPermissionDenied() {

    }


    public Navigator getNavigator() {
        if (navigator == null)
            navigator = new Navigator() {
                @Override
                public void navigateActivity(Class<?> destination, @Nullable Bundle bundle) {
                    Intent intent = new Intent(QRCodeReaderActivity.this, destination);
                    intent.putExtra("classData", bundle);
                    startActivity(intent);
                    finishActivity();
                }

                @Override
                public void navigateActivityForResult(Class<? extends AppCompatActivity> destination, @Nullable Bundle bundle, int reqCode) {
                    Intent intent = new Intent(QRCodeReaderActivity.this, destination);
                    intent.putExtra("classData", bundle);
                    startActivityForResult(intent, reqCode);

                }

                @Override
                public void navigateActivity(Intent intent) {
                    startActivity(intent);
                }

                @Override
                public void finishActivity() {
                    finish();
                }

                @Override
                public void finishActivity(Intent resultData) {
                    setResult(Activity.RESULT_OK, resultData);
                    finish();
                }

                @Override
                public void openStandaloneYoutube(String videoId) {

                }

                @Override
                public void openStandaloneVideo(String videoId) {

                }


                @Override
                public void showMenuPopup(@MenuRes int layout, View v, android.support.v7.widget.PopupMenu.OnMenuItemClickListener clickListner) {

                }

                @Override
                public void forceUpdate() {

                }


                @Override
                public void launchImageChooserActivity(final int reqCode) {


                }

                @Override
                public void launchVideoChooserActivity(final int reqCode) {


                }

                @Override
                public void launchPlaceSearchIntent(int reqCode) {

                }

                @Override
                public void hideKeyBoard(View view) {

                }
            }

                    ;
        return navigator;
    }


    public String getBranchUrlData(String url) {
        if (TextUtils.isEmpty(url))
            return null;
        JSONObject jsonObject = doGetRequest("https://api.branch.io/v1/url?url=" + url + "&branch_key=" + branchKey);
        try {
            jsonObject = new JSONObject(jsonObject.getString("data"));
            jsonObject = new JSONObject(jsonObject.getString("qrcode"));
            return jsonObject.toString(1);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject doGetRequest(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //noinspection ConstantConditions
        if (response != null && response.body() != null) {
            try {
                //noinspection ConstantConditions
                return new JSONObject(response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        } else return null;
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

                    final ClassListing data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ClassListing.class);
                    apiService.getFilterData(data.reqData).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.tag(TAG).e(throwable, json);
                        }
                    }).subscribe(new Consumer<FilterData>() {
                        @Override
                        public void accept(FilterData filterData) throws Exception {
                            bundle.putSerializable(Constants.classFilterData, filterData);
                            bundle.putString(Constants.promoCode, data.promoCode);
                            bundle.putSerializable(Constants.origin, FilterViewModel.ORIGIN_HOME);
                            getNavigator().navigateActivity(ClassListActivity.class, bundle);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.tag(TAG).e(throwable, json);
                        }
                    });

                } else if (json.contains("connect_listing")) {
                    ConnectListing data = gson.fromJson(json, ConnectListing.class);
                    ConnectFilterData connectFilterData = gson.fromJson(gson.toJson(data.reqData), ConnectFilterData.class);
                    bundle.putSerializable(Constants.connectFilterData, connectFilterData);
                    getNavigator().navigateActivity(ConnectHomeActivity.class, bundle);
                } else if (json.contains("class_detail")) {
                    ClassDetail data = gson.fromJson(json, ClassDetail.class);
                    bundle.putString("id", data.reqData.getId());
                    bundle.putString(Constants.origin, ClassListViewModel1.ORIGIN_HOME);
                    bundle.putString(Constants.promoCode, data.reqData.getPromoCode());
                    getNavigator().navigateActivity(ClassDetailActivity.class, bundle);
                } else if (json.contains("post_detail")) {
                    PostDetail data = gson.fromJson(json, PostDetail.class);
                    bundle.putString("postId", data.reqData.getPostId());
                    getNavigator().navigateActivity(PostDetailActivity.class, bundle);
                } else if (json.contains("class_booking")) {
                    final ClassBooking data = gson.fromJson(json.substring(0, json.lastIndexOf("}") + 1), ClassBooking.class);
                    apiService.getClassDetail(data.reqData.id, 0).onErrorReturn(new Function<Throwable, ClassData>() {
                        @Override
                        public ClassData apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            return new ClassData();
                        }
                    }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ClassData>() {
                        @Override
                        public void accept(@NonNull ClassData classData) throws Exception {
                            if (classData != null) {
                                bundle.putSerializable("classData", classData);
                                bundle.putSerializable("checkoutType", "class");
                                bundle.putString(Constants.promoCode, data.reqData.promoCode);
                                getNavigator().navigateActivity(CheckoutActivity.class, bundle);
                            }
                        }
                    });
                }

            }
        });
    }

    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();

    }
}


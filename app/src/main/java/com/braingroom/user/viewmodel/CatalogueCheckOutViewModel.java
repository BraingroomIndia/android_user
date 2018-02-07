package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.model.dto.PayUCheckoutData;
import com.braingroom.user.model.request.GetBookingDetailsReq;
import com.braingroom.user.model.request.RazorSuccessReq;
import com.braingroom.user.model.response.QuoteDetailsResp;
import com.braingroom.user.model.response.RazorSuccessResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CatalogueCheckOutActivity;
import com.braingroom.user.view.activity.PaySuccessActivity;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by godara on 06/09/17.
 */

public class CatalogueCheckOutViewModel extends ViewModel {

    //Essentials
    private final Navigator navigator;
    private final MessageHelper messageHelper;
    private final String quoteId;

    //Display Info
    public final ObservableField<String> className = new ObservableField<>("");
    public final ObservableField<String> classImage = new ObservableField<>("");
    public final ObservableField<String> classType = new ObservableField<>("");
    public final ObservableField<String> organizationName = new ObservableField<>("");
    public final ObservableField<String> location = new ObservableField<>("");
    public final ObservableField<String> classDate = new ObservableField<>("");
    public final ObservableField<String> unitPriceMessage = new ObservableField<>("");
    public final ObservableField<String> totalPriceMessage = new ObservableField<>("");
    public final ObservableField<String> amountTobePayed = new ObservableField<>("");
    public final ObservableBoolean payTotalPrice = new ObservableBoolean(true);
    public final ObservableBoolean hideUnitPrice = new ObservableBoolean(false);


    //onClick Action
    public final Action onPaymentOptionClicked, onPaymentClicked;

    //Internal info
    private String userId;
    private String classId;
    private String catalogId;
    private int totalPrice;
    private int unitPrice;
    private int paymentAmount;
    private PayUCheckoutData mChekcoutData;

    //Ui helper
    private final CatalogueCheckOutActivity.UiHelper uiHelper;


    public CatalogueCheckOutViewModel(@NonNull FirebaseAnalytics mFirebaseAnalytics, @NonNull Tracker mTracker, @NonNull final Navigator navigator, @NonNull final MessageHelper messageHelper, @NonNull final CatalogueCheckOutActivity.UiHelper uiHelper, @NonNull String quoteId) {
        messageHelper.showProgressDialog("Wait", "loading");
        this.mFirebaseAnalytics = mFirebaseAnalytics;
        this.mTracker = mTracker;
        this.navigator = navigator;
        this.messageHelper = messageHelper;
        this.uiHelper = uiHelper;
        this.quoteId = quoteId;
        onPaymentOptionClicked = new Action() {
            @Override
            public void run() throws Exception {
                paymentAmount = (payTotalPrice.get() ? totalPrice : unitPrice);
                amountTobePayed.set("" + paymentAmount);

            }
        };
        onPaymentClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (paymentAmount == 0) {
                    messageHelper.showDismissInfo("Wait", "Wait while we load the info");
                } else {
                    GetBookingDetailsReq.Snippet snippet = new GetBookingDetailsReq.Snippet();
                    snippet.setLocalityId(-1 + "");
                    snippet.setAmount(paymentAmount + "");
                    snippet.setClassId(classId);
                    snippet.setLevels("[{\"" + classId + "\":\"1\"}]");
                    snippet.setUserId(userId);
                    snippet.setIsGuest(0);
                    apiService.getBookingDetails(new GetBookingDetailsReq(snippet)).doOnError(new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            messageHelper.showAcceptableInfo("Error", "Oops something went wrong", new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                }
                            });
                        }
                    }).subscribe(new Consumer<PayUCheckoutData>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull PayUCheckoutData payUCheckoutData) throws Exception {
                            mChekcoutData = payUCheckoutData;
                            startRazorpayPayment(mChekcoutData.getAmount(), mChekcoutData.getPhone(), mChekcoutData.getEmail());
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                            throwable.printStackTrace();
                            messageHelper.showAcceptableInfo("Error", "Oops something went wrong", new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                }
                            });
                        }
                    });
                }
            }
        };
        if (isEmpty(quoteId)) {
            navigator.finishActivity();
            return;
        }

        apiService.getQuoteDetails(quoteId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<QuoteDetailsResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull QuoteDetailsResp resp) throws Exception {
                messageHelper.dismissActiveProgress();
                QuoteDetailsResp.Snippet data;
                if (isEmpty(resp))
                    navigator.finishActivity();
                else if (isEmpty(resp.getData()))
                    navigator.finishActivity();
                else {

                    data = resp.getData().get(0);
                    className.set(data.getClassName());
                    classImage.set(data.getClassImage());
                    classType.set(data.getClassMode());
                    classDate.set(data.getDate());
                    organizationName.set(data.getOrganization());
                    location.set(data.getLocation());
                    unitPrice = (data.getUnitPrice());
                    unitPriceMessage.set(data.getUnitMessage());
                    totalPrice = (data.getTotalPrice());
                    totalPriceMessage.set(data.getTotalMessage());
                    userId = data.getUserId();
                    classId = data.getClassId();
                    catalogId = data.getCatalogId();
                    onPaymentOptionClicked.run();
                    hideUnitPrice.set(unitPrice == 0);
                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                messageHelper.showAcceptableInfo("Error", "Oops something went wrong", new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                        navigator.finishActivity();
                    }
                });
            }
        });


    }

    public void handleRazorpaySuccess(String razorpayTxnid) {
        final Bundle data = new Bundle();
        data.putString(Constants.name, mChekcoutData.getFirstname());
        data.putString(Constants.className, className.get());
        data.putString(Constants.amount, mChekcoutData.getAmount() + "");
        data.putString(Constants.BGTransactionId, mChekcoutData.getBGtransactionid());
        RazorSuccessReq.Snippet snippet = new RazorSuccessReq.Snippet();
        snippet.setAmount("" + paymentAmount);
        snippet.setClassId(mChekcoutData.getClassId());
        snippet.setUserId(mChekcoutData.getUdf1());
        snippet.setLocalityId("-1");
        snippet.setRazorPayTxnid(razorpayTxnid);
        snippet.setBgTxnid(mChekcoutData.getBGtransactionid());
        snippet.setBookingType(Constants.catalogBooking);
        snippet.setUserEmail(mChekcoutData.getEmail());
        snippet.setUserMobile(mChekcoutData.getPhone());
        snippet.setUserId(mChekcoutData.getUdf1());
        snippet.setIsGuest(0);
        snippet.setCouponCode("");
        snippet.setCouponAmount("");
        snippet.setPromoCode("");
        snippet.setPromoAmount("");
        snippet.setTickets("{\"tickets\":[{\"level_id\":\"1\",\"tickets\":\"1\"}]}");

        apiService.postRazorpaySuccess(new RazorSuccessReq(snippet)).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                data.putBoolean(Constants.success, false);
                navigator.navigateActivity(PaySuccessActivity.class, data);
            }
        }).subscribe(new Consumer<RazorSuccessResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull RazorSuccessResp resp) throws Exception {
                if (isEmpty(resp)) {
                    data.putBoolean(Constants.success, false);
                } else if (isEmpty(resp.getData())) {
                    data.putBoolean(Constants.success, false);
                } else if (isEmpty(resp.getData().get(0))) {
                    data.putBoolean(Constants.success, false);
                } else
                    data.putBoolean(Constants.success, true);
                navigator.navigateActivity(PaySuccessActivity.class, data);
                if (data.getBoolean(Constants.success))
                    navigator.finishActivity();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                data.putBoolean(Constants.success, false);
                navigator.navigateActivity(PaySuccessActivity.class, data);
            }
        });
    }

    public void startRazorpayPayment(int amount, String mobile, String email) throws JSONException {
        JSONObject options = new JSONObject();
        options.put("name", className.get());
        options.put("description", "By: Braingroom.com");
        //You can omit the image option to fetch the image from dashboard
        options.put("image", "https://www.braingroom.com/homepage/img/logo.jpg");
        options.put("currency", "INR");
        options.put("amount", "" + amount * 100);

        JSONObject preFill = new JSONObject();
        preFill.put("email", email);
        preFill.put("contact", mobile);
        options.put("prefill", preFill);
        uiHelper.startRazorpayPayment(options);
    }
}

package com.braingroom.user.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.widget.RadioGroup;

import com.braingroom.user.R;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.viewmodel.CatalogueCheckOutViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

/**
 * Created by godara on 06/09/17.
 */

public class CatalogueCheckOutActivity extends BaseActivity implements PaymentResultListener {

    public interface UiHelper {
//        Removed payumoney
        //void startPayUPayment(PayUmoneySdkInitilizer.PaymentParam param);

        void startRazorpayPayment(JSONObject options);
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new CatalogueCheckOutViewModel(getFirebaseAnalytics(),getGoogleTracker(),getNavigator(), getMessageHelper(), new UiHelper() {
            @Override
            public void startRazorpayPayment(JSONObject options) {
                final Activity activity = CatalogueCheckOutActivity.this;
                final Checkout co = new Checkout();
                try {
                    co.open(activity, options);
                } catch (Exception e) {
                    getMessageHelper().show("Error in payment: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, getIntentString(Constants.quoteId));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_catalogue_checkout;
    }

    @Override
    public void onPaymentSuccess(String s) {
        ((CatalogueCheckOutViewModel) vm).handleRazorpaySuccess(s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        getMessageHelper().showDismissInfo("RazorPay Error", s);
    }
}

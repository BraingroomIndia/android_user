package com.braingroom.user.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.databinding.ActivityCheckoutBinding;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.adapters.NonReactiveRecyclerViewAdapter;
import com.braingroom.user.viewmodel.CheckoutViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import timber.log.Timber;

public class CheckoutActivity extends BaseActivity implements PaymentResultListener {

    private RecyclerView mRecyclerView;
    private NonReactiveRecyclerViewAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onPaymentSuccess(String razorpayKey) {
        ((CheckoutViewModel) vm).handleRazorPaySuccess(razorpayKey);
    }

    @Override
    public void onPaymentError(int i, String reason) {
        Timber.tag(TAG).e("Error from Razor pay\t" + reason);
        getMessageHelper().show("Payment Failure, Reason : " + reason);
    }

    public interface UiHelper {
//        Removed payumoney
        //void startPayUPayment(PayUmoneySdkInitilizer.PaymentParam param);

        void startRazorpayPayment(JSONObject options);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getSupportActionBar() != null)
            getSupportActionBar().setElevation(0);
        mRecyclerView = ((ActivityCheckoutBinding) binding).pricingRecyclerview;
        mAdapter = new NonReactiveRecyclerViewAdapter(vm, ((CheckoutViewModel) vm).getViewProvider());
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {

        float discountFactor = getIntentFloat(Constants.discountFactor);
        if (discountFactor > 1 || discountFactor <= 0)
            discountFactor = 1;
        UiHelper uiHelper = new UiHelper() {
//            @Override
//            public void startPayUPayment(PaymentParams params, PayuConfig config, PayuHashes hashes) {
//                Intent intent = new Intent(CheckoutActivity.this, PayUBaseActivity.class);
//                intent.putExtra(PayuConstants.PAYU_CONFIG, config);
//                intent.putExtra(PayuConstants.PAYMENT_PARAMS, params);
//                intent.putExtra(PayuConstants.PAYU_HASHES, hashes);
//                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
//            }

            /* Removed payumoney
            @Override
             public void startPayUPayment(PayUmoneySdkInitilizer.PaymentParam param) {
            *//*    try {
                    ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,true);
                } catch (Exception e){e.printStackTrace();}*//*
                PayUmoneySdkInitilizer.startPaymentActivityForResult(CheckoutActivity.this, param);
            }
*/
            @Override
            public void startRazorpayPayment(JSONObject options) {

                if (!((CheckoutViewModel) vm).classData.getPriceCode().contains("INR")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("checkoutData", options.toString());
                    getNavigator().navigateActivityForResult(StripeActivity.class, bundle, ViewModel.REQ_CODE_STRIPE);
                } else {
                    final Activity activity = CheckoutActivity.this;
                    final Checkout co = new Checkout();
                    try {
                        co.open(activity, options);
                    } catch (Exception e) {
                        Timber.tag(TAG).e(e, "Error in starting razorPay");
                        getMessageHelper().show("Error in payment: " + e.getMessage());
                    }

                }
            }
        };

        return new CheckoutViewModel(getFirebaseAnalytics(), getGoogleTracker(), getHelperFactory(), getMessageHelper(), getNavigator(),
                uiHelper, (ClassData) getIntentSerializable("classData"), getIntentInt(Constants.paymentMode), discountFactor, getIntentString(Constants.promoCode), getIntentString(Constants.isIncentive));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_checkout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}

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
import com.braingroom.user.view.adapters.NonReactiveRecyclerViewAdapter;
import com.braingroom.user.viewmodel.CheckoutViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payUMoney.sdk.SdkConstants;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.zoho.livechat.android.MbedableComponent;
import com.zoho.salesiqembed.ZohoSalesIQ;

import org.json.JSONObject;

public class CheckoutActivity extends BaseActivity implements PaymentResultListener {

    private RecyclerView mRecyclerView;
    private NonReactiveRecyclerViewAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public void onPaymentSuccess(String razorpayKey) {
        ((CheckoutViewModel) vm).handleRazorpaySuccess(razorpayKey);
    }

    @Override
    public void onPaymentError(int i, String reason) {
        getMessageHelper().show("Payment Failure, Reason : " + reason);
    }

    public interface UiHelper {
        void startPayUPayment(PayUmoneySdkInitilizer.PaymentParam param);

        void startRazorpayPayment(JSONObject options);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  ZohoSalesIQ.init(getApplication(), "vbaQbJT6pgp%2F3Bcyb2J5%2FIhGMQOrLMwCtSBDWvN719iFMGR6B8HQyg%2BYib4OymZbE8IA0L0udBo%3D", "689wH7lT2QpWpcVrcMcCOyr5GFEXO50qvrL9kW6ZUoJBV99ST2d97x9bQ72vOdCZvEyaq1slqV%2BhFd9wYVqD4%2FOv9G5EQVmggE5fHIGwHTu%2BOv301MhrYfOQ0d2CzZkt0qlz0ytPLErfXRYn5bu%2FGGbVJmRXRnWU");
        super.onCreate(savedInstanceState);
        try {
            ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,true);
        } catch (Exception e){e.printStackTrace();}
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

        UiHelper uiHelper = new UiHelper() {
//            @Override
//            public void startPayUPayment(PaymentParams params, PayuConfig config, PayuHashes hashes) {
//                Intent intent = new Intent(CheckoutActivity.this, PayUBaseActivity.class);
//                intent.putExtra(PayuConstants.PAYU_CONFIG, config);
//                intent.putExtra(PayuConstants.PAYMENT_PARAMS, params);
//                intent.putExtra(PayuConstants.PAYU_HASHES, hashes);
//                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
//            }

            @Override
            public void startPayUPayment(PayUmoneySdkInitilizer.PaymentParam param) {
                try {
                    ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,true);
                } catch (Exception e){e.printStackTrace();}
                PayUmoneySdkInitilizer.startPaymentActivityForResult(CheckoutActivity.this, param);
            }

            @Override
            public void startRazorpayPayment(JSONObject options) {
                try {
                    ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,false);
                } catch (Exception e){e.printStackTrace();}
                final Activity activity = CheckoutActivity.this;
                final Checkout co = new Checkout();
                try {
                    co.open(activity, options);
                } catch (Exception e) {
                    getMessageHelper().show("Error in payment: " + e.getMessage());
                   // e.printStackTrace();
                }

            }
        };
        return new CheckoutViewModel(getHelperFactory(), getMessageHelper(), getNavigator(),
                uiHelper, (ClassData) getIntentSerializable("classData"), "gift".equals(getIntentString("checkoutType")));
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
        super.onActivityResult(requestCode,resultCode,data);
        try {
            ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,true);
        } catch (Exception e){e.printStackTrace();}

        if (requestCode ==
                PayUmoneySdkInitilizer.PAYU_SDK_PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String paymentId =
                        data.getStringExtra(SdkConstants.PAYMENT_ID);
                if (paymentId != null)
                    getMessageHelper().showDismissInfo("Congrats!", "Your class has been successfully booked");
            } else if (resultCode == RESULT_CANCELED) {
                getMessageHelper().show("paymemt cancelled");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_FAILED) {
                getMessageHelper().show("paymemt failure");
            } else if (resultCode == PayUmoneySdkInitilizer.RESULT_BACK) {
//                getMessageHelper().show("paymemt failure");
            }
        }
//        if (requestCode == PayuConstants.PAYU_REQUEST_CODE) {
//            if (classData != null) {
//                String response = classData.getStringExtra("payu_response");
//                Log.d("PAYU RESPONSE", "onActivityResult: " + response);
//                getMessageHelper().showDismissInfo("Congrats!", "Your class has been successfully booked");
//            } else {
//                getMessageHelper().show("paymemt failure");
//            }
//        }
    }
}

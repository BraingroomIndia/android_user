package com.braingroom.user.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.NgoFormViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class NgoFormActivity extends BaseActivity implements PaymentResultListener {

    @Override
    public void onPaymentSuccess(String razorpayKey) {
        ((NgoFormViewModel) vm).updatePaymentSuccess(razorpayKey);
    }

    @Override
    public void onPaymentError(int i, String reason) {
        getMessageHelper().show("Payment Failure, Reason : " + reason);
    }

    public interface UiHelper {
//        removed payumoney
//        void startPayUPayment(PayUmoneySdkInitilizer.PaymentParam param);

        void startRazorpayPayment(JSONObject options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(getIntentString("title"));
    }

    @NonNull
    @Override
    protected ViewModel createViewModel() {
        UiHelper uiHelper = new UiHelper() {
/*           removed payumoney
            @Override
            public void startPayUPayment(PayUmoneySdkInitilizer.PaymentParam param) {
                PayUmoneySdkInitilizer.startPaymentActivityForResult(NgoFormActivity.this, param);
            }*/

            @Override
            public void startRazorpayPayment(JSONObject options) {
                final Activity activity = NgoFormActivity.this;
                final Checkout co = new Checkout();
                try {
                    co.open(activity, options);
                } catch (Exception e) {
                    getMessageHelper().show("Error in payment: " + e.getMessage());
                    //e.printStackTrace();
                }

            }
        };

        String giftcardId = getIntentString("giftcardId");
        return new NgoFormViewModel(getMessageHelper(), getNavigator(), getHelperFactory(), giftcardId, uiHelper);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ngo_form;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ngo_form, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_apply) {
            ((NgoFormViewModel) vm).onSubmitClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

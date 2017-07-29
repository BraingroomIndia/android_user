package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;

import com.braingroom.user.R;
import com.braingroom.user.view.activity.BaseActivity;
import com.braingroom.user.viewmodel.PaySuccessViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.zoho.livechat.android.MbedableComponent;
import com.zoho.salesiqembed.ZohoSalesIQ;

/**
 * Created by godara on 07/07/17.
 */

public class PaySuccessActivity extends BaseActivity {
    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new PaySuccessViewModel(getNavigator(),  getIntentString("name"),
                getIntentString("transactionId"),getIntentString("class_name"), getIntentString("totalAmount"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar=getSupportActionBar();
        try {
            ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,true);
        } catch (Exception e){e.printStackTrace();}
        if (actionBar!=null)
            actionBar.hide();
    }

    @Override
    protected int getLayoutId() {
        String condition = "";
        condition = getIntentString("success") != null ? getIntentString("success") : "";
        if (PaySuccessViewModel.PAYMENT_SUCCESS.equalsIgnoreCase(condition))
            return R.layout.activity_pay_success;
        else
            return R.layout.activity_pay_failure;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}

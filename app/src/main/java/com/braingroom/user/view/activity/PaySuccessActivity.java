package com.braingroom.user.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;

import com.braingroom.user.R;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.view.activity.BaseActivity;
import com.braingroom.user.viewmodel.PaySuccessViewModel;
import com.braingroom.user.viewmodel.ViewModel;


/**
 * Created by godara on 07/07/17.
 */

public class PaySuccessActivity extends BaseActivity {
    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new PaySuccessViewModel(getNavigator(), getIntentString(Constants.name),
                getIntentString(Constants.BGTransactionId), getIntentString(Constants.className), getIntentString(Constants.amount));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
 /*      try {
            ZohoSalesIQ.Chat.setVisibility(MbedableComponent.CHAT,true);
        } catch (Exception e){e.printStackTrace();}*/
        if (actionBar != null)
            actionBar.hide();
    }

    @Override
    protected int getLayoutId() {
        boolean success;
        success = getIntentBoolean(Constants.success);
        if (success)
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

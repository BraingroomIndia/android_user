package com.braingroom.user.view.activity;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
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
        return new PaySuccessViewModel(getIntentString("time"), getIntentString("name"),
                getIntentString("transactionId"), getIntentString("totalAmount"));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay_success;
    }
}

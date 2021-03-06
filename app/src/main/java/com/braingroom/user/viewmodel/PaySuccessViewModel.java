package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.Spanned;

import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.HomeActivity;

import io.reactivex.functions.Action;

/**
 * Created by godara on 07/07/17.
 */

public class PaySuccessViewModel extends ViewModel {
    public static String PAYMENT_SUCCESS = "True";
    public static String PAYMENT_FAIL = "Fail";
    public final ObservableField<String> className;
    public final ObservableField<String> name;
    public final ObservableField<String> transactionId;
    public final ObservableField<Spanned> totalAmount;
    public final Action onDoneClicked, onTryAgainClicked;

    public PaySuccessViewModel(@NonNull final Navigator navigator, final String name, String transactionId, String className, String totalAmount) {
        this.className = new ObservableField<>(className);
        this.name = new ObservableField<>(name);
        this.transactionId = new ObservableField<>(transactionId);
        this.totalAmount = new ObservableField<>(CommonUtils.fromHtml(totalAmount));
        onDoneClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(HomeActivity.class, null);
                navigator.finishActivity();
            }
        };
        onTryAgainClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.finishActivity();
            }
        };
    }
}

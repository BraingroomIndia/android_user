package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;

/**
 * Created by godara on 07/07/17.
 */

public class PaySuccessViewModel extends ViewModel {
    public final ObservableField<String> time;
    public final ObservableField<String> name;
    public final ObservableField<String> transactionId;
    public final ObservableField<String> totalAmount;

    public PaySuccessViewModel(String time, String name, String transactionId, String totalAmount) {
        this.time = new ObservableField<>(time);
        this.name = new ObservableField<>(name);
        this.transactionId = new ObservableField<>(transactionId);
        this.totalAmount = new ObservableField<>(totalAmount);
    }
}

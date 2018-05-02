package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Spanned;

import io.reactivex.functions.Action;


/**
 * Created by android on 03/04/18.
 */

public class SessionItemViewModel extends ViewModel {
    public final String sessionName;
    public final String sessionDesc;
    public final String sessionPrice;
    public final String sessionOfferPrice;
    public final Action selectMicroSession;

    public final ObservableField<Spanned> singlePersionTotalPrice = new ObservableField<>();
    public ObservableBoolean isPersion = new ObservableBoolean(true);



    public SessionItemViewModel(String sessionName, String sessionDesc, Integer sessionPrice, Integer sessionOfferPrice) {
        this.sessionName = sessionName;
        this.sessionDesc = sessionDesc;
        this.sessionPrice = "₹" +sessionPrice;
        this.sessionOfferPrice = "₹" +sessionOfferPrice;


        selectMicroSession = new Action() {
            @Override
            public void run() throws Exception {
                //singlePersionTotalPrice.set(sessionOfferPrice.toString());
                isPersion.get();

            }
        };
    }
}

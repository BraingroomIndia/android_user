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

    //public ObservableBoolean isPersion = new ObservableBoolean(true);
    public final ObservableBoolean radioSelectMicroSession = new ObservableBoolean(false);
    ClassDetailViewModel classDetailViewModel;


    public SessionItemViewModel(String sessionName, String sessionDesc, Integer sessionPrice, Integer sessionOfferPrice) {
        this.sessionName = sessionName;
        this.sessionDesc = sessionDesc;
        this.sessionPrice = "₹" +sessionPrice;
        this.sessionOfferPrice = "₹" +sessionOfferPrice;


        selectMicroSession = new Action() {
            @Override
            public void run() throws Exception {
                //singlePersionTotalPrice.set(sessionOfferPrice.toString());
                //classDetailViewModel.singlePersionTotalPrice.set(sessionOfferPrice.toString());  //update total price for micro sessions
                radioSelectMicroSession.set(true);
                classDetailViewModel.radioSelectFullSession.get();
                //isPersion.get();
            }
        };
    }
}

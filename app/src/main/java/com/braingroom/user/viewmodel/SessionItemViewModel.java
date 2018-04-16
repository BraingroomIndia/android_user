package com.braingroom.user.viewmodel;


import android.databinding.ObservableBoolean;
import io.reactivex.functions.Action;


/**
 * Created by android on 03/04/18.
 */

public class SessionItemViewModel extends ViewModel {
    public final String sessionName;
    public final String sessionDesc;
    public final int sessionPrice;
    public final int sessionOfferPrice;
    public final Action selectMicroSession;
    public ObservableBoolean isPersion = new ObservableBoolean(true);


    public SessionItemViewModel(String sessionName, String sessionDesc, Integer sessionPrice, Integer sessionOfferPrice) {
        this.sessionName = sessionName;
        this.sessionDesc = sessionDesc;
        this.sessionPrice = sessionPrice;
        this.sessionOfferPrice = sessionOfferPrice;


        selectMicroSession = new Action() {
            @Override
            public void run() throws Exception {

            }
        };
    }

}

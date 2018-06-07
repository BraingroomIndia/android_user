package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.braingroom.user.view.MessageHelper;

import io.reactivex.functions.Action;


/**
 * Created by android on 03/04/18.
 */

public class SessionItemViewModel extends ViewModel {
    public final String sessionId;
    public final String sessionName;
    public final String sessionDesc;
    public final String sessionPrice;
    public final String sessionOfferPrice;
    public final Action selectMicroSession;
    MessageHelper messageHelper;



    //public ObservableBoolean isPersion = new ObservableBoolean(true);
    public final ObservableBoolean checkSelectMicroSession = new ObservableBoolean(false);
    public final ObservableBoolean checkFullSession;
    ClassDetailViewModel classDetailViewModel;
    public final ObservableBoolean fieldsEnabled = new ObservableBoolean();


    public SessionItemViewModel(ObservableBoolean checkFullSession, String sessionId, String sessionName, String sessionDesc, Integer sessionPrice, Integer sessionOfferPrice) {
        this.sessionId = sessionId;
        this.sessionName = sessionName;
        this.sessionDesc = sessionDesc;
        this.sessionPrice = "₹" +sessionPrice;
        this.sessionOfferPrice = "₹" +sessionOfferPrice;
        this.checkFullSession = checkFullSession;

        selectMicroSession = new Action() {
            @Override
            public void run() throws Exception {
                Log.d("This is test", "MICRO : " + String.valueOf(checkSelectMicroSession.get()));
                //messageHelper.show("test");
                //singlePersionTotalPrice.set(sessionOfferPrice.toString());
                //classDetailViewModel.singlePersionTotalPrice.set(sessionOfferPrice.toString());  //update total price for micro sessions
                //checkSelectMicroSession.set(true);
                //classDetailViewModel.checkSelectFullSession.get();
                //isPersion.get();
                //checkAll.set(false);
            }
        };
        if(sessionId.equals("")){
            classDetailViewModel.microSessionHeading.set(String.valueOf(View.INVISIBLE));
        }
    }
    public void onCheckedChanged(View view){
        fieldsEnabled.set(((CheckBox)view).isChecked());
        Log.d("Checked", "Full session" + ((CheckBox)view).isChecked());
        checkFullSession.set(false);
        checkFullSession.get();
    }
}

package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CouponFormActivity;

import io.reactivex.functions.Action;

public class GiftCouponViewModel extends ViewModel {

    public static final String GCTYPE_INDIVIDUAL = "INDIVIDUAL";
    public static final String GCTYPE_CORPORATE = "CORPORATE";
    public static final String GCTYPE_NGO = "NGO";

    public final ShowcaseGiftcardListViewModel individualVm, corporateVm, ngoVm;
    public final ObservableField<String> couponValue;

    public final ObservableBoolean mailMe = new ObservableBoolean(true);
    public final ObservableBoolean forIndividual = new ObservableBoolean(true);

    public final Action openCouponForm;

    public GiftCouponViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {

        this.individualVm = new ShowcaseGiftcardListViewModel("Individual", messageHelper, navigator, apiService.getIndividualGiftcards(), GCTYPE_INDIVIDUAL);
        this.corporateVm = new ShowcaseGiftcardListViewModel("Corporate", messageHelper, navigator, apiService.getCorporateGiftcards(), GCTYPE_CORPORATE);
        this.ngoVm = new ShowcaseGiftcardListViewModel("NGO", messageHelper, navigator, apiService.getNgoGiftcards(), GCTYPE_NGO);
        this.couponValue = new ObservableField<>("");

        this.openCouponForm = new Action() {
            @Override
            public void run() throws Exception {
                if ("".equals(couponValue.get())) {
                    messageHelper.show("Coupon value can't be empty");
                    return;
                }
                try {
                    int couponVal = Integer.parseInt(couponValue.get());
                    navigator.navigateActivity(CouponFormActivity.class, null);
                } catch (NumberFormatException e) {
                    messageHelper.show("only numbers allowed");
                }
            }
        };

    }
}

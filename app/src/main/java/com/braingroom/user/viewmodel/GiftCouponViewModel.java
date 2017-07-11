package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CouponFormActivity;

import io.reactivex.functions.Action;

public class GiftCouponViewModel extends ViewModel {

    public static final String GCTYPE_INDIVIDUAL = "INDIVIDUAL";
    public static final String GCTYPE_CORPORATE = "CORPORATE";
    public static final String GCTYPE_NGO = "NGO";

    public static final int GIFT_TYPE_SELF = 1;
    public static final int GIFT_TYPE_FRIEND = 1;

    public static final int GIFT_BY_INDIVIDUAL = 1;
    public static final int GIFT_BY_CORPORATE = 2;
    public static final int GIFT_BY_NGO = 3;

    public final ShowcaseGiftcardListViewModel individualVm, corporateVm, ngoVm;
    public final ObservableField<String> couponValue;

    public final ObservableBoolean giftByIndividual = new ObservableBoolean(true);
    public final ObservableBoolean giftByCorporate = new ObservableBoolean(false);
    public final ObservableBoolean giftByNgo = new ObservableBoolean(false);
    public final ObservableBoolean giftTypeSelf = new ObservableBoolean(true);
    public final ObservableBoolean giftTypeFriend = new ObservableBoolean(false);

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
                    Bundle data = new Bundle();
                    data.putInt("couponVal", Integer.parseInt(couponValue.get()));
                    data.putInt("giftType", 2);
                    data.putInt("giftBy", giftByIndividual.get() ? GIFT_BY_INDIVIDUAL : giftByCorporate.get() ? GIFT_BY_CORPORATE : GIFT_BY_NGO);
                    navigator.navigateActivity(CouponFormActivity.class, data);
                } catch (NumberFormatException e) {
                    messageHelper.show("only numbers allowed");
                }
            }
        };

    }
}

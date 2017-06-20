package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.response.GiftcardResp;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.NgoFormActivity;

import io.reactivex.functions.Action;

public class GiftcardItemViewModel extends ViewModel {


    public ObservableField<String> cardImage = new ObservableField<>();
    public ObservableField<String> cardName = new ObservableField<>();
    public ObservableField<String> cardId = new ObservableField<>();

    @NonNull
    public final int placeHolder = R.drawable.white_background_drawable;

    public GiftcardItemViewModel(@NonNull final Navigator navigator, @NonNull final GiftcardResp.DataSnippet data, final String giftcardType) {
        this.cardImage.set(data.getCardImage());
        this.cardName.set(data.getCardName());
        this.cardId.set(data.getId());
        onClicked = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                if (GiftCouponViewModel.GCTYPE_NGO.equals(giftcardType)) {
                    navigator.navigateActivity(NgoFormActivity.class, data);
                } else {
                    FilterData filterData = new FilterData();
                    filterData.setGiftId(cardId.get());
                    data.putSerializable("filterData",filterData);
                   /* data.putString("giftId", cardId.get());//senderId);*/
                    data.putString("origin", ClassListViewModel1.ORIGIN_GIFT);
                    navigator.navigateActivity(ClassListActivity.class, data);
                }
            }
        };

    }

    @Nullable
    public final Action onClicked;

}

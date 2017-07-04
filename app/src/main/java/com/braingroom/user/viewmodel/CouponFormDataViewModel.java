package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.request.SaveGiftCouponReq;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class CouponFormDataViewModel extends ViewModel {

    public final ObservableBoolean removable = new ObservableBoolean();
    public final DataItemViewModel emailAddress;
    public final DataItemViewModel denomination;
    public final DataItemViewModel nosCoupons;
    public final DataItemViewModel recipientsName;
    public final DataItemViewModel mobileNumber;
    public final DataItemViewModel personalisedMsg;
    public final ListDialogViewModel1 categoryVm;
    public final Action remove;

    public final MyConsumer<CouponFormDataViewModel> removeThisFragment;

    public CouponFormDataViewModel(int couponVal, int giftType, int giftBy, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator,
                                   @NonNull HelperFactory helperFactory, final MyConsumer<CouponFormDataViewModel> removeThisFragment, int index) {
        emailAddress = new DataItemViewModel("");
        denomination = new DataItemViewModel("");
        if (couponVal > 0) denomination.s_1.set(couponVal + "");
        nosCoupons = new DataItemViewModel("");
        recipientsName = new DataItemViewModel(null);
        mobileNumber = new DataItemViewModel(null);
        personalisedMsg = new DataItemViewModel(null);
        if (index > 0) removable.set(true);
        categoryVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Category", messageHelper, apiService.getCategory().map(new Function<CategoryResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CategoryResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CategoryResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getCategoryName(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), true, null, "");

        this.removeThisFragment = removeThisFragment;
        remove = new Action() {
            @Override
            public void run() throws Exception {
                removeThisFragment.accept(CouponFormDataViewModel.this);
            }
        };

        if (giftType == GiftCouponViewModel.GIFT_TYPE_SELF) {
            recipientsName.s_1.set(null);
            mobileNumber.s_1.set(null);
            personalisedMsg.s_1.set(null);
        } else {
            recipientsName.s_1.set("");
            mobileNumber.s_1.set("");
            personalisedMsg.s_1.set("");
//                }
        }
    }

    public SaveGiftCouponReq.GiftDetails getformData() {
        SaveGiftCouponReq.GiftDetails data = new SaveGiftCouponReq.GiftDetails();
        data.setCatId(android.text.TextUtils.join(",", categoryVm.getSelectedItemsId()));
        data.setDenomination(denomination.s_1.get());
        data.setEmailId(emailAddress.s_1.get());
        data.setNoCoupons(nosCoupons.s_1.get());
        data.setRecipientName(recipientsName.s_1.get() == null ? "" : recipientsName.s_1.get());
        data.setComment(personalisedMsg.s_1.get() == null ? "" : personalisedMsg.s_1.get());
        data.setMobile(mobileNumber.s_1.get() == null ? "" : mobileNumber.s_1.get());
        return data;
    }


}

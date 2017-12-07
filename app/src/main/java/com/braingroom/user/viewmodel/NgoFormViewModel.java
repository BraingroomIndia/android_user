package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.request.RazorBuySuccessReq;
import com.braingroom.user.model.request.SaveGiftCouponReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.SaveGiftCouponResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CouponFormActivity;
import com.braingroom.user.view.activity.NgoFormActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class NgoFormViewModel extends ViewModel {

    public final DataItemViewModel recipientsName;
    public final DataItemViewModel emailAddress;
    public final DataItemViewModel mobileNumber;
    public final DataItemViewModel denomination;
    public final DataItemViewModel personalisedMsg;
    public final ListDialogViewModel1 classTopicVm, ngoTypeVm;
    MessageHelper messageHelper;
    HelperFactory mHelperFactory;
    Navigator mNavigator;
    int isGuest = 0;
    public String gUserId;
    public SaveGiftCouponResp.Snippet couponPayData;
    NgoFormActivity.UiHelper uiHelper;

    public NgoFormViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator,
                            @NonNull HelperFactory helperFactory, String giftcardId, NgoFormActivity.UiHelper uiHelper) {
        this.messageHelper = messageHelper;
        this.mHelperFactory = helperFactory;
        this.mNavigator = navigator;
        this.uiHelper = uiHelper;
        emailAddress = new DataItemViewModel("");
        denomination = new DataItemViewModel("");
        recipientsName = new DataItemViewModel("");
        mobileNumber = new DataItemViewModel("");
        personalisedMsg = new DataItemViewModel("");
        ngoTypeVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Ngo name", messageHelper, apiService.getNgoList(giftcardId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), snippet.getId());
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), false, null, "");

        classTopicVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Segments", messageHelper, apiService.getNgoCategories(giftcardId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), snippet.getId());
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), false, null, "");

    }

    public void onSubmitClicked() {
        if (!getLoggedIn()) {
            mHelperFactory.createDialogHelper()
                    .showCustomView(R.layout.content_guest_payment_dialog, new GuestPaymentDialogViewModel(null, messageHelper, mNavigator, new CheckoutViewModel.UiHelper() {
                        @Override
                        public void onGuestLoginSuccess(String id) {
                            isGuest = 1;
                            saveGiftCoupon(id);
                        }

                        @Override
                        public void onCollectGiftDetail(String name, String email, String personalMsg) {

                        }
                    }, null, CouponFormActivity.class.getSimpleName()), false);

        } else {
            saveGiftCoupon(pref.getString(Constants.BG_ID, ""));
        }
    }


    public void saveGiftCoupon(String userId) {
        messageHelper.showProgressDialog("Please Wait...", "We are fetching more details");
        gUserId = userId;
        SaveGiftCouponReq req = new SaveGiftCouponReq();
        SaveGiftCouponReq.Snippet snippet = new SaveGiftCouponReq.Snippet();
        snippet.setUserId(userId);
        snippet.setGiftBy("3");
        snippet.setGiftType("1");
        snippet.setGiftCardSegId(android.text.TextUtils.join(",", classTopicVm.getSelectedItemsId()));
        snippet.setNgoName1(android.text.TextUtils.join(",", ngoTypeVm.getSelectedItemsId()));
        snippet.setIsGuest(isGuest);

        SaveGiftCouponReq.GiftDetails data = new SaveGiftCouponReq.GiftDetails();
        data.setCatId("1");
        data.setDenomination(denomination.s_1.get());
        data.setEmailId(emailAddress.s_1.get());
        data.setNoCoupons("");
        data.setRecipientName("");
        data.setComment(personalisedMsg.s_1.get());
        data.setMobile("");

        List<SaveGiftCouponReq.GiftDetails> dataList = new ArrayList<>();
        dataList.add(data);


        snippet.setGiftDetails(dataList);
        req.setData(snippet);
        apiService.saveGiftCoupon(req).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<SaveGiftCouponResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull SaveGiftCouponResp resp) throws Exception {

                if ("1".equals(resp.getResCode())) {
                    messageHelper.dismissActiveProgress();
                    couponPayData = resp.getData().get(0);
                    startPayment(resp.getData().get(0).getPrice(), resp.getData().get(0).getMobile(), resp.getData().get(0).getEmail());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                messageHelper.dismissActiveProgress();
                messageHelper.show("some error occurred");
            }
        });
    }

    public void startPayment(int amount, String mobile, String email) throws JSONException {
        JSONObject options = new JSONObject();
        options.put("name", "Gift Coupon");
        options.put("description", "By: Braingroom.com");
        //You can omit the image option to fetch the image from dashboard
        options.put("image", "https://www.braingroom.com/homepage/img/logo.jpg");
        options.put("currency", "INR");
        options.put("amount", "" + amount * 100);

        JSONObject preFill = new JSONObject();
        preFill.put("email", email);
        preFill.put("contact", mobile);
        options.put("prefill", preFill);
        uiHelper.startRazorpayPayment(options);
    }

    public void updatePaymentSuccess(String txnId) {
        messageHelper.showProgressDialog("Processing", "finalizing your purchase");
        RazorBuySuccessReq.Snippet req = new RazorBuySuccessReq.Snippet();
        req.setUserId(gUserId);
        req.setAmount(couponPayData.getPrice() + "");
        req.setIsGuest(isGuest + "");
        req.setTermId(couponPayData.getTermId());
        req.setTxnid(txnId);
        req.setUserEmail(couponPayData.getEmail());
        req.setUserMobile("" + couponPayData.getMobile());
        apiService.updateCouponPaymentSuccess(new RazorBuySuccessReq(req)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                if ("1".equals(baseResp.getResCode())) {
                    messageHelper.dismissActiveProgress();
                    messageHelper.show(baseResp.getResMsg());
                    // TODO: 09/07/17 show success screen
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                messageHelper.dismissActiveProgress();
                messageHelper.show("something went wrong!");
            }
        });
    }


}

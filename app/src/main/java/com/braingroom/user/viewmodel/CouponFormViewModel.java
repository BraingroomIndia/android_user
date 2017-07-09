package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.model.request.RazorBuySuccessReq;
import com.braingroom.user.model.request.SaveGiftCouponReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.SaveGiftCouponResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CouponFormActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class CouponFormViewModel extends ViewModel {
    public final MessageHelper messageHelper;
    public final Navigator navigator;
    public final HelperFactory mHelperFactory;
    public final List<CouponFormDataViewModel> formDataList = new ArrayList<>();
    public final Action submitClicked, notifyAdapter;
    public CouponFormActivity.UiHelper uiHelper;
    public SaveGiftCouponResp.Snippet couponPayData;
    public int isGuest = 0;
    public String gUserId, giftType, giftBy;

    public CouponFormViewModel(int couponVal, int giftType, int giftBy, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory,
                               Action notifyAdapter, CouponFormActivity.UiHelper uiHelper) {
        this.messageHelper = messageHelper;
        this.navigator = navigator;
        this.mHelperFactory = helperFactory;
        this.uiHelper = uiHelper;

        this.giftType = giftType + "";
        this.giftBy = giftBy + "";

        submitClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (!loggedIn.get()) {
                    mHelperFactory.createDialogHelper()
                            .showCustomView(R.layout.content_guest_payment_dialog, new GuestPaymentDialogViewModel(null, messageHelper, navigator, new CheckoutViewModel.UiHelper() {
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
        };
        try {
            addNewFormData(couponVal, giftType, giftBy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.notifyAdapter = notifyAdapter;
    }


    public void saveGiftCoupon(String userId) {
        messageHelper.showProgressDialog("Please Wait...", "We are fetching more details");
        gUserId = userId;
        SaveGiftCouponReq req = new SaveGiftCouponReq();
        SaveGiftCouponReq.Snippet snippet = new SaveGiftCouponReq.Snippet();
        snippet.setUserId(userId);
        snippet.setGiftBy(giftBy);
        snippet.setGiftType(giftType);
        snippet.setIsGuest(isGuest);
        List<SaveGiftCouponReq.GiftDetails> data = new ArrayList<>();
        for (CouponFormDataViewModel vm : formDataList) {
            data.add(vm.getformData());
        }

        snippet.setGiftDetails(data);
        req.setData(snippet);
        apiService.saveGiftCoupon(req).subscribe(new Consumer<SaveGiftCouponResp>() {
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

    public void addNewFormData(int couponVal, int giftType, int giftBy) throws Exception {
        formDataList.add(new CouponFormDataViewModel(couponVal, giftType, giftBy, messageHelper, navigator, mHelperFactory, new MyConsumer<CouponFormDataViewModel>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull CouponFormDataViewModel vm) {
                try {
                    removeNewFormData(vm);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, formDataList.size()));
        notifyAdapter.run();
    }

    public void removeNewFormData(CouponFormDataViewModel vm) throws Exception {
        if (formDataList.indexOf(vm) == 0) return;
        boolean status = formDataList.remove(vm);
        notifyAdapter.run();

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
        apiService.updateCouponPaymentSuccess(new RazorBuySuccessReq(req)).subscribe(new Consumer<BaseResp>() {
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

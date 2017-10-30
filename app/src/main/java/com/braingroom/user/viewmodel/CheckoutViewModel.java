package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.dto.ClassLevelData;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.dto.PayUCheckoutData;
import com.braingroom.user.model.request.CouponCodeReq;
import com.braingroom.user.model.request.GetBookingDetailsReq;
import com.braingroom.user.model.request.PromoCodeReq;
import com.braingroom.user.model.request.RazorSuccessReq;
import com.braingroom.user.model.request.SaveGiftCouponReq;
import com.braingroom.user.model.response.PromoInfo;
import com.braingroom.user.model.response.PromocodeResp;
import com.braingroom.user.model.response.RazorSuccessResp;
import com.braingroom.user.model.response.SaveGiftCouponResp;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CheckoutActivity;
import com.braingroom.user.view.activity.PaySuccessActivity;
import com.braingroom.user.view.adapters.ViewProvider;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.analytics.FirebaseAnalytics;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lombok.Getter;

public class CheckoutViewModel extends ViewModel {
    @Override
    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_LOGIN)
            gUserId = pref.getString(Constants.BG_ID, "");
    }

    public final ListDialogViewModel1 locationsVm;
    public final ObservableInt totalAmount;
    public final ObservableInt totalAmountAfterPromo;

    public final ObservableBoolean showBookOption = new ObservableBoolean(false);

    public final Action onProceedClicked, onOpenPromoCode, onApplyPromoCode, onOpenCouponCode, onApplyCouponCode, onShowPriceDetailsClicked;

    public final ObservableField<String> couponCode;
    public final ObservableField<String> promoCode;
    public String appliedPromoCodeId;
    public final ObservableField<String> appliedCouponCode;
    public final ObservableField<String> appliedPromoCode;
    public float appliedPromoAmount = 0, appliedCouponAmount = 0;

    public final ObservableField<String> classImage;
    public final ObservableField<String> defaultPrice;
    public final ObservableField<String> classTopic;
    public final ObservableField<String> classDate;
    public final CheckoutActivity.UiHelper uiHelper;
    public final ObservableBoolean isLocation = new ObservableBoolean(true);
    public final ObservableBoolean isGift = new ObservableBoolean(false);

    public final List<String> pricingTableList = new ArrayList<>();

    public final ObservableBoolean applyingPromoCode = new ObservableBoolean(false);
    public final ObservableBoolean applyingCouponCode = new ObservableBoolean(false);

    public String selectedLocalityId;
    String gUserId = pref.getString(Constants.BG_ID, "");

    public int isGuest = 0;

    ClassData classData;
    PayUCheckoutData mChekcoutData;
    public final boolean usePayU = false;
    MessageHelper messageHelper;
    Navigator navigator;
    HelperFactory helperFactory;
    final String promo;


    //private PayUChecksum checksum;

    private final Map<String, Integer> sublevelTextMap = new HashMap<>();

    public SaveGiftCouponResp.Snippet couponPayData;

    public interface UiHelper {
        void onGuestLoginSuccess(String userId);

        void onCollectGiftDetail(String name, String email, String personalMsg);
    }

    @Getter
    ViewProvider viewProvider = new ViewProvider() {
        @Override
        public int getView(ViewModel vm) {
            return R.layout.item_level_pricing;
        }
    };

    public Action dataChangeAction = new Action() {
        @Override
        public void run() throws Exception {
            int totalPrice = 0;
            if (appliedCouponAmount > 0 || appliedPromoAmount > 0) {
                messageHelper.showDismissInfo("Warning", "Your discount has been reset");
                appliedCouponCode.set(null);
                appliedPromoCode.set(null);
                appliedCouponAmount = 0;
                appliedPromoAmount = 0;

            }
            showBookOption.set(false);

            for (ViewModel nonReactiveItem : nonReactiveItems) {
                totalPrice = totalPrice + ((LevelPricingItemViewModel) nonReactiveItem).totalPrice.get();
                if (Integer.parseInt(((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()) > 0)
                    showBookOption.set(true);
            }
            totalAmount.set(totalPrice);
            totalAmountAfterPromo.set(totalPrice);


        }
    };
    private Action cartAmount = new Action() {
        @Override
        public void run() throws Exception {
            if (totalAmount.get() < appliedCouponAmount)
                appliedCouponAmount = totalAmount.get();
            if (totalAmount.get() < appliedPromoAmount)
                appliedPromoAmount = totalAmount.get();

            totalAmountAfterPromo.set((int) (totalAmount.get() - appliedPromoAmount - appliedCouponAmount));
        }
    };

    public CheckoutViewModel(@NonNull final FirebaseAnalytics mFirebaseAnalytics, @NonNull final Tracker mTracker, @NonNull final HelperFactory helperFactory, @NonNull final MessageHelper messageHelper,
                             @NonNull final Navigator navigator, final CheckoutActivity.UiHelper uiHelper, final ClassData classData, final boolean isGift, final String promo) {
        this.mFirebaseAnalytics = mFirebaseAnalytics;
        this.mTracker = mTracker;
        setScreenName(classData.getClassTopic());
        totalAmount = new ObservableInt(0);
        totalAmountAfterPromo = new ObservableInt(0);
        couponCode = new ObservableField<>();
        appliedCouponCode = new ObservableField<>(null);
        this.promoCode = new ObservableField<>();
        appliedPromoCode = new ObservableField<>(null);
        this.uiHelper = uiHelper;
        this.classData = classData;
        gUserId = pref.getString(Constants.BG_ID, "");
        this.promo = promo;
//        ZohoSalesIQ.Tracking.setPageTitle("Booking Page " +classData.getClassTopic());
        this.messageHelper = messageHelper;
        this.navigator = navigator;
        this.helperFactory = helperFactory;
        isLocation.set(!isEmpty(classData.getLocation()));
        classImage = new ObservableField<>(classData.getImage());
        defaultPrice = new ObservableField<>(classData.getLevelDetails().get(0).getPrice());
        classTopic = new ObservableField<>(classData.getClassTopic());
        if ("fixed".equalsIgnoreCase(classData.getClassTypeData())) {
            classDate = new ObservableField<>(classData.getSessionTime() + ", " + classData.getSessionDate());
        } else {
            classDate = new ObservableField<>();
        }
        this.isGift.set(isGift);

        LinkedHashMap<String, Integer> locationsData = new LinkedHashMap<>();
        if (isLocation.get())
            for (int i = 0; i < classData.getLocation().size(); i++) {
                locationsData.put(classData.getLocation().get(i).getLocationArea(), Integer.valueOf(classData.getLocation().get(i).getLocalityId()));
            }

        locationsVm = new ListDialogViewModel1(helperFactory.createDialogHelper()
                , "Choose a location", messageHelper, Observable.just(new ListDialogData1(locationsData))
                , new LinkedHashMap<String, Integer>(), false, new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedData) throws Exception {
                if (selectedData.values().iterator().hasNext()) {
                    selectedLocalityId = "" + selectedData.values().iterator().next();
                }
            }
        }, "");
        if (!isLocation.get())
            selectedLocalityId = "-1";

        List<Integer> priceList;
        for (ClassLevelData classLevelData : classData.getLevelDetails()) {
            priceList = new ArrayList<>();
            int range = 0;
            if (classData.getIsCoupleClass() == 1) {
                for (int i = 0; i < 1000; i++) {
                    if (i % 2 == 0)
                        priceList.add(Integer.parseInt(classLevelData.getGroups().get(0).getPrice()));
                    else
                        priceList.add(0);
                }
            } else if ("group".equalsIgnoreCase(classData.getPricingType())) {
                for (int i = 0; i < classLevelData.getGroups().size(); i++) {
                    if (i == 0) {
                        priceList.add(Integer.parseInt(classLevelData.getGroups().get(i).getPrice()));
                        range = Integer.parseInt(classLevelData.getGroups().get(i).getEndRange()) - Integer.parseInt(classLevelData.getGroups().get(i).getStartRange());
                        priceList.addAll(Collections.nCopies(range - 1, 0));

                        pricingTableList.add("Flat Rs." + classLevelData.getGroups().get(i).getPrice() + " between  " + classLevelData.getGroups().get(i).getStartRange() + "-" + classLevelData.getGroups().get(i).getEndRange() + " people");
                    } else {
                        range = Integer.parseInt(classLevelData.getGroups().get(i).getEndRange()) - Integer.parseInt(classLevelData.getGroups().get(i).getStartRange());
                        priceList.addAll(Collections.nCopies(range, Integer.parseInt(classLevelData.getGroups().get(i).getPrice())));
                        pricingTableList.add("Per person Rs." + classLevelData.getGroups().get(i).getPrice() + " between  " + classLevelData.getGroups().get(i).getStartRange() + "-" + classLevelData.getGroups().get(i).getEndRange() + " people");
                    }
                }
            } else {
                priceList.add(Integer.parseInt(classLevelData.getPrice()));
            }
            nonReactiveItems.add(new LevelPricingItemViewModel(classLevelData.getLevelName(), classLevelData.getLevelId(), getLevelsTitleText(classLevelData.getLevelName()), classLevelData.getDescription(), priceList, dataChangeAction, messageHelper));
        }
        onProceedClicked = new Action() {
            @Override
            public void run() throws Exception {

                if (selectedLocalityId == null) {
                    messageHelper.show("Select locality for class");
                    return;
                }
                if (!getLoggedIn() && isGuest == 0) {
                    helperFactory.createDialogHelper()
                            .showCustomView(R.layout.content_guest_payment_dialog, new GuestPaymentDialogViewModel(classData, messageHelper, navigator, new UiHelper() {
                                @Override
                                public void onGuestLoginSuccess(String userId) {
                                    try {
                                        isGuest = 1;
                                        gUserId = userId;
                                        if (isGift) {
                                            collectGiftingDetails();
                                        } else
                                            startPayment();
                                    } catch (JSONException e) {
                                        messageHelper.show("Something went wrong. JSON error");
                                    }
                                }

                                @Override
                                public void onCollectGiftDetail(String name, String email, String personalMsg) {

                                }
                            }, classData.getId(), CheckoutActivity.class.getSimpleName()), false);
                    return;
                }
                if (isGift) {
                    collectGiftingDetails();
                } else
                    startPayment();

            }
        };


        onOpenPromoCode = new Action() {
            @Override
            public void run() throws Exception {
                applyingPromoCode.set(true);
                applyingCouponCode.set(false);
                promoCode.set("");
                appliedPromoCode.set(null);
                appliedPromoAmount = 0;
                couponCode.set(null);
                appliedCouponCode.set(null);
                appliedCouponAmount = 0;
               /* if (!getLoggedIn() && isGuest == 0) {
                    helperFactory.createDialogHelper()
                            .showCustomView(R.layout.content_guest_payment_dialog, new GuestPaymentDialogViewModel(classData, messageHelper, navigator, new UiHelper() {
                                @Override
                                public void onGuestLoginSuccess(String id) {

                                    gUserId = id;
                                    isGuest = 1;

                                    try {
                                        cartAmount.run();
                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void onCollectGiftDetail(String name, String email, String personalMsg) {

                                }
                            }, classData.getId(), CheckoutActivity.class.getSimpleName()), false);

                } else {*/
                promoCode.set("");
                appliedPromoCode.set(null);
                appliedPromoAmount = 0;
                couponCode.set(null);
                appliedCouponCode.set(null);
                appliedCouponAmount = 0;
                cartAmount.run();
//                }


            }
        };
        if (promo != null)
            try {
                onOpenPromoCode.run();
                promoCode.set(promo);
            } catch (Exception e) {
                e.printStackTrace();
            }

        onOpenCouponCode = new Action() {
            @Override
            public void run() throws Exception {
                applyingCouponCode.set(true);
                applyingPromoCode.set(false);
                promoCode.set(null);
                appliedPromoCode.set(null);
                appliedPromoAmount = 0;
                couponCode.set("");
                appliedCouponCode.set(null);
                appliedCouponAmount = 0;
                if (!getLoggedIn() && isGuest == 0) {
                    helperFactory.createDialogHelper()
                            .showCustomView(R.layout.content_guest_payment_dialog, new GuestPaymentDialogViewModel(classData, messageHelper, navigator, new UiHelper() {
                                @Override
                                public void onGuestLoginSuccess(String id) {
                                    gUserId = id;
                                    isGuest = 1;
                                    try {
                                        cartAmount.run();
                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void onCollectGiftDetail(String name, String email, String personalMsg) {

                                }
                            }, classData.getId(), CheckoutActivity.class.getSimpleName()), false);

                } else {
                    promoCode.set(null);
                    appliedPromoCode.set(null);
                    appliedPromoAmount = 0;
                    couponCode.set("");
                    appliedCouponCode.set(null);
                    appliedCouponAmount = 0;
                    cartAmount.run();
                }


            }
        };


        onShowPriceDetailsClicked = new Action() {
            @Override
            public void run() throws Exception {
                helperFactory.createDialogHelper().showListDialog("Price division", pricingTableList);
            }
        };

        onApplyPromoCode = new Action() {
            @Override
            public void run() throws Exception {
                if (!getLoggedIn() && isGuest == 0) {
                    helperFactory.createDialogHelper()
                            .showCustomView(R.layout.content_guest_payment_dialog, new GuestPaymentDialogViewModel(classData, messageHelper, navigator, new UiHelper() {
                                @Override
                                public void onGuestLoginSuccess(String id) {

                                    gUserId = id;
                                    isGuest = 1;

                                    try {
                                        cartAmount.run();
                                    } catch (Exception e) {
                                    }
                                }

                                @Override
                                public void onCollectGiftDetail(String name, String email, String personalMsg) {

                                }
                            }, classData.getId(), CheckoutActivity.class.getSimpleName()), false);

                } else if (promoCode != null && promoCode.get().length() > 3) {
                    PromoCodeReq.Snippet snippet = new PromoCodeReq.Snippet();
                    List<RazorSuccessReq.Levels> levelsList = new ArrayList<>();
                    snippet.setClassId(classData.getId());
                    for (ViewModel nonReactiveItem : nonReactiveItems) {
                        if (Integer.parseInt(((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()) > 0) {
                            levelsList.add(new RazorSuccessReq.Levels(((LevelPricingItemViewModel) nonReactiveItem).levelId, ((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()));
                        }
                    }
                    if (levelsList.isEmpty()) {
                        messageHelper.showDismissInfo("Error", "Please select level.");
                        return;
                    }
                    snippet.setTotalTicket("{\"tickets\":" + gson.toJson(levelsList) + "}");
                    snippet.setCode(promoCode.get());
                    snippet.setIsGuest(isGuest);
                    snippet.setUserId(gUserId);
                    snippet.setTotalAmount(totalAmount.get() + "");
                    apiService.applyPromoCode(snippet).subscribe(new Consumer<PromocodeResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull PromocodeResp resp) throws Exception {
                            if (resp.getData().size() > 0) {
                                appliedPromoCode.set(promoCode.get());
                                appliedPromoAmount = resp.getData().get(0).getAmount();
                                applyingPromoCode.set(false);
                                cartAmount.run();
//                                promoCode.set(null);
                            } else {
                                messageHelper.show(resp.getResMsg());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable th) throws Exception {
                            Log.d("Checkout ", "accept: ");
                        }
                    });
                } else if (getLoggedIn() && isGuest == 1) {
                    messageHelper.show("code length should be more than 3");
                }
            }
        };

        onApplyCouponCode = new Action() {
            @Override
            public void run() throws Exception {
                if (couponCode != null && couponCode.get().length() > 3) {
                    CouponCodeReq.Snippet snippet = new CouponCodeReq.Snippet();
                    snippet.setClassId(classData.getId());
                    snippet.setCode(couponCode.get());
                    snippet.setIsGuest(isGuest);
                    snippet.setUserId(gUserId);
                    List<RazorSuccessReq.Levels> levelsList = new ArrayList<>();
                    for (ViewModel nonReactiveItem : nonReactiveItems) {
                        if (Integer.parseInt(((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()) > 0) {
                            levelsList.add(new RazorSuccessReq.Levels(((LevelPricingItemViewModel) nonReactiveItem).levelId, ((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()));
                        }
                    }
                    snippet.setTotalTicket("{\"tickets\":" + gson.toJson(levelsList) + "}");
                    apiService.applyCouponCode(snippet).subscribe(new Consumer<PromocodeResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull PromocodeResp resp) throws Exception {
                            if (resp.getData().size() > 0) {

                                appliedCouponCode.set(couponCode.get());
                                appliedCouponAmount = resp.getData().get(0).getAmount();
                                if (appliedCouponAmount > totalAmount.get())
                                    appliedCouponAmount = totalAmount.get();
                                applyingCouponCode.set(false);
                                cartAmount.run();
//
                            } else {
                                messageHelper.showDismissInfo("Error", resp.getResMsg());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Throwable th) throws Exception {
                            Log.d("Checkout ", "accept: ");
                        }
                    });
                } else {
                    messageHelper.show("code length should be more than 3");
                }
            }
        };

    }

    public void saveGiftClassData(String userId, int isGuest, String giftRecepientEmail, String giftRecepientName, String giftPersonalMsg) {

        gUserId = userId;
        List<RazorSuccessReq.Levels> levelsList = new ArrayList<>();
        for (ViewModel nonReactiveItem : nonReactiveItems) {
            if (Integer.parseInt(((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()) > 0) {
                levelsList.add(new RazorSuccessReq.Levels(((LevelPricingItemViewModel) nonReactiveItem).levelId, ((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()));
            }
        }
        SaveGiftCouponReq.GiftDetails data = new SaveGiftCouponReq.GiftDetails();
        data.setClassId(classData.getId());
        data.setDenomination(totalAmountAfterPromo.get() + "");
        data.setEmailId(giftRecepientEmail);
        data.setRecipientName(giftRecepientName);
        data.setComment(giftPersonalMsg);
        data.setMobile("");

        List<SaveGiftCouponReq.GiftDetails> dataList = new ArrayList<>();
        dataList.add(data);

        SaveGiftCouponReq req = new SaveGiftCouponReq();
        SaveGiftCouponReq.Snippet snippet = new SaveGiftCouponReq.Snippet();

        snippet.setUserId(gUserId);
        snippet.setGiftBy("");
        snippet.setGiftType("");
        snippet.setIsGuest(isGuest);
        snippet.setTotalTicket("{\"tickets\":" + gson.toJson(levelsList) + "}");

        snippet.setGiftDetails(dataList);
        req.setData(snippet);

        apiService.saveGiftCoupon(req).subscribe(new Consumer<SaveGiftCouponResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull SaveGiftCouponResp resp) throws Exception {
                if ("1".equals(resp.getResCode())) {
                    messageHelper.dismissActiveProgress();
                    couponPayData = resp.getData().get(0);
                    startRazorpayPayment(resp.getData().get(0).getPrice(), resp.getData().get(0).getMobile(), resp.getData().get(0).getEmail());
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

    public void startRazorpayPayment(int amount, String mobile, String email) throws JSONException {
        if (amount == 0) {
            handleRazorpaySuccess("");
            return;
        }
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


    public void startPayment() throws JSONException {
        boolean classLevelSelected = false;

        for (ViewModel nonReactiveItem : nonReactiveItems) {
            if (Integer.parseInt(((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()) > 0) {
                classLevelSelected = true;
                break;
            }
        }
        if (!classLevelSelected) {
            messageHelper.showDismissInfo("Error", "Select class levels");
            return;
        }
        GetBookingDetailsReq.Snippet snippet = new GetBookingDetailsReq.Snippet();
            /*snippet.setTxnId(UUID.randomUUID().toString());*/
        snippet.setAmount("" + totalAmountAfterPromo.get());
        snippet.setClassId(classData.getId());
        snippet.setLocalityId(selectedLocalityId);
        snippet.setIsGuest(isGuest);
        snippet.setUserId(gUserId);
        JSONArray levels = new JSONArray();
        JSONObject levelObj;
        for (ViewModel nonReactiveItem : nonReactiveItems) {
            levelObj = new JSONObject();
            if (Integer.parseInt(((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()) > 0) {
                levelObj.put(((LevelPricingItemViewModel) nonReactiveItem).levelId, ((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get());
                levels.put(levelObj);
            }
        }
        snippet.setLevels(levels.toString());
        messageHelper.showProgressDialog(null, "Initiating Payment...");
        apiService.getBookingDetails(new GetBookingDetailsReq(snippet)
                , appliedPromoCodeId != null ? appliedPromoCodeId : null
                , appliedPromoCodeId != null ? appliedPromoCode.get() : null).subscribe(new Consumer<PayUCheckoutData>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull PayUCheckoutData chekcoutData) throws Exception {

                mChekcoutData = chekcoutData;
                if (usePayU) {
                    /*    PayUmoneySdkInitilizer.PaymentParam.Builder builder = new
                                PayUmoneySdkInitilizer.PaymentParam.Builder()
                                .setMerchantId("5513008")
                                .setKey(chekcoutData.getKey())
                                .setIsDebug(true) // for Live mode - setIsDebug(false)
                                .setAmount(Integer.parseInt(chekcoutData.getAmount()))
                                .setTnxId(chekcoutData.getTxnId())
                                .setPhone(chekcoutData.getPhone())
                                .setProductName(chekcoutData.getProductinfo())
                                .setFirstName(chekcoutData.getFirstname())
                                .setEmail(chekcoutData.getEmail())
                                .setsUrl(chekcoutData.getSurl())
                                .setfUrl(chekcoutData.getFurl())
                                .setUdf1(chekcoutData.getUdf1())
                                .setUdf2(chekcoutData.getUdf2())
                                .setUdf3(chekcoutData.getUdf3())
                                .setUdf4(chekcoutData.getUdf4());
                        PayUmoneySdkInitilizer.PaymentParam param = builder.build();
                        param.setMerchantHash(chekcoutData.getPaymentHash());
                        messageHelper.dismissActiveProgress();
                        UiHelper.startPayUPayment(param);*/
                } else {
                    if (totalAmountAfterPromo.get() == 0) {
                        handleRazorpaySuccess("");
                        return;
                    }
                    JSONObject options = new JSONObject();
                    options.put("name", classData.getClassTopic());
                    options.put("description", "By: " + classData.getClassProvider());
                    //You can omit the image option to fetch the image from dashboard
                    options.put("image", "https://www.braingroom.com/homepage/img/logo.jpg");
                    options.put("currency", "INR");
                    options.put("amount", "" + chekcoutData.getAmount() * 100);
                    JSONObject preFill = new JSONObject();
                    preFill.put("email", chekcoutData.getEmail());
                    preFill.put("contact", chekcoutData.getPhone());
                    options.put("prefill", preFill);
                        /*Log.d(TAG, "accept: name\t:\t" + classData.getClassTopic() + "\ndescription, By: " + classData.getTeacher()
                                + "\namount\t:\t" + chekcoutData.getAmount() * 100 + "\nemail\t:\t" + chekcoutData.getEmail()
                                + "\ncontact\t:\t" + chekcoutData.getPhone());*/
                    messageHelper.dismissActiveProgress();
                    uiHelper.startRazorpayPayment(options);
                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                messageHelper.show(throwable.getMessage());
                Log.d("PayUCheckoutData", "accept: " + throwable.getMessage());
            }
        });

    }


    public String getLevelsTitleText(String levelName) {
        if (sublevelTextMap.get(levelName) == null) {
            sublevelTextMap.put(levelName, 1);
            return "Level " + 1;
        } else {
            int newLevel = sublevelTextMap.get(levelName) + 1;
            sublevelTextMap.put(levelName, newLevel);
            return "Level " + newLevel;
        }
    }

    public void collectGiftingDetails() {
        helperFactory.createDialogHelper()
                .showCustomView(R.layout.content_gift_details_dialog, new GiftingDetailsDialogViewModel(messageHelper, new UiHelper() {
                    @Override
                    public void onGuestLoginSuccess(String userId) {
                        gUserId = userId;
                        isGuest = 1;

                    }

                    @Override
                    public void onCollectGiftDetail(String name, String email, String personalMsg) {
                        saveGiftClassData(gUserId, isGuest, email, name, personalMsg);

                    }
                }), false);
    }

    public void handleRazorpaySuccess(final String razorpayId) {
        String date;

        final Bundle bundle = new Bundle();


        if (isGift.get()) {
            bundle.putString(Constants.BGTransactionId, couponPayData.getBGtransactionid());
            bundle.putString(Constants.className, classData.getClassTopic());
            bundle.putString(Constants.name, couponPayData.getName());
            bundle.putString(Constants.amount, couponPayData.getPrice() + "");
            messageHelper.showProgressDialog("Processing", "finalizing your purchase");
            RazorSuccessReq.Snippet snippet = new RazorSuccessReq.Snippet();
            snippet.setBgTxnid(couponPayData.getBGtransactionid());
            snippet.setBookingType(Constants.giftClass);
            snippet.setTermId(couponPayData.getTermId());
            snippet.setAmount("" + totalAmountAfterPromo.get());
            snippet.setClassId(classData.getId());
            snippet.setUserId(gUserId);
            snippet.setLocalityId(selectedLocalityId);
            snippet.setRazorPayTxnid(razorpayId);
            snippet.setUserEmail(couponPayData.getEmail());
            snippet.setUserMobile(couponPayData.getMobile());
            snippet.setUserEmail(couponPayData.getEmail());
            snippet.setUserMobile(couponPayData.getMobile());
            snippet.setIsGuest(isGuest);
            snippet.setCouponCode(appliedCouponCode.get());
            snippet.setCouponAmount(appliedCouponAmount + "");
            snippet.setPromoCode(appliedPromoCode.get());
            snippet.setPromoAmount(appliedPromoAmount + "");
            List<RazorSuccessReq.Levels> levelsList = new ArrayList<>();
            for (ViewModel nonReactiveItem : nonReactiveItems) {
                if (Integer.parseInt(((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()) > 0) {
                    levelsList.add(new RazorSuccessReq.Levels(((LevelPricingItemViewModel) nonReactiveItem).levelId, ((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()));
                }
            }
            String temp = gson.toJson(levelsList);
            temp = "{\"tickets\":" + temp + "}";
            snippet.setTickets(temp);
            apiService.postRazorpaySuccess(new RazorSuccessReq(snippet)).doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                    messageHelper.dismissActiveProgress();
                    messageHelper.show(throwable.getMessage());
                    bundle.putBoolean(Constants.success, false);
//                    navigator.navigateActivity(PaySuccessActivity.class, bundle);
                }
            }).doOnComplete(new Action() {
                @Override
                public void run() throws Exception {
                    messageHelper.dismissActiveProgress();
                    navigator.navigateActivity(PaySuccessActivity.class, bundle);
                    if (bundle.getBoolean(Constants.success))
                        navigator.finishActivity();
                }
            }).subscribe(new Consumer<RazorSuccessResp>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull RazorSuccessResp resp) throws Exception {
                    messageHelper.dismissActiveProgress();
                    messageHelper.show(resp.getResMsg());
                    if (isEmpty(resp)) {
                        bundle.putBoolean(Constants.success, false);
                    } else if (isEmpty(resp.getData())) {
                        bundle.putBoolean(Constants.success, false);
                    } else if (isEmpty(resp.getData().get(0))) {
                        bundle.putBoolean(Constants.success, false);
                    } else
                        bundle.putBoolean(Constants.success, true);


                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                    messageHelper.dismissActiveProgress();
                    messageHelper.show(throwable.getMessage());
                    bundle.putBoolean(Constants.success, false);
//                    navigator.navigateActivity(PaySuccessActivity.class, bundle);

                }
            });
            /*.subscribe(new Consumer<RazorSuccessResp>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull RazorSuccessResp resp) throws Exception {
                    messageHelper.show(resp.getResMsg());
                    if (isEmpty(resp)) {
                        bundle.putBoolean(Constants.success, false);
                    } else if (isEmpty(resp.getData())) {
                        bundle.putBoolean(Constants.success, false);
                    } else if (isEmpty(resp.getData().get(0))) {
                        bundle.putBoolean(Constants.success, false);
                    } else
                        bundle.putBoolean(Constants.success, true);
                    messageHelper.dismissActiveProgress();
//                    navigator.navigateActivity(PaySuccessActivity.class, bundle);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                    messageHelper.dismissActiveProgress();
                    messageHelper.show(throwable.getMessage());
                }
            });*/
        } else {

            bundle.putString(Constants.BGTransactionId, mChekcoutData.getBGtransactionid());
            bundle.putString(Constants.className, classData.getClassTopic());
            bundle.putString(Constants.name, mChekcoutData.getFirstname());
            bundle.putString(Constants.amount, mChekcoutData.getAmount() + "");
            messageHelper.showProgressDialog("Wait", "Processing your payment...");
            RazorSuccessReq.Snippet snippet = new RazorSuccessReq.Snippet();
            snippet.setBookingType(Constants.normalBooking);
            snippet.setBgTxnid(mChekcoutData.getBGtransactionid());
            snippet.setAmount("" + totalAmountAfterPromo.get());
            snippet.setClassId(classData.getId());
            snippet.setUserId(mChekcoutData.getUdf1());
            snippet.setLocalityId(selectedLocalityId);
            snippet.setRazorPayTxnid(razorpayId);
            snippet.setUserEmail(mChekcoutData.getEmail());
            snippet.setUserMobile(mChekcoutData.getPhone());
            snippet.setUserId(mChekcoutData.getUdf1());
            snippet.setIsGuest(isGuest);
            snippet.setCouponCode(appliedCouponCode.get());
            snippet.setCouponAmount(appliedCouponAmount + "");
            snippet.setPromoCode(appliedPromoCode.get());
            snippet.setPromoAmount(appliedPromoAmount + "");
            List<RazorSuccessReq.Levels> levelsList = new ArrayList<>();
            for (ViewModel nonReactiveItem : nonReactiveItems) {
                if (Integer.parseInt(((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()) > 0) {
                    levelsList.add(new RazorSuccessReq.Levels(((LevelPricingItemViewModel) nonReactiveItem).levelId, ((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()));
                }
            }

            String temp = gson.toJson(levelsList);
            temp = "{\"tickets\":" + temp + "}";
            snippet.setTickets(temp);
            apiService.postRazorpaySuccess(new RazorSuccessReq(snippet)).doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                    messageHelper.dismissActiveProgress();
                    messageHelper.show(throwable.getMessage());
                    bundle.putBoolean(Constants.success, false);
//                    navigator.navigateActivity(PaySuccessActivity.class, bundle);
                }
            }).doOnComplete(new Action() {
                @Override
                public void run() throws Exception {
                    messageHelper.dismissActiveProgress();
                    navigator.navigateActivity(PaySuccessActivity.class, bundle);
                    if (bundle.getBoolean(Constants.success))
                        navigator.finishActivity();
                }
            }).subscribe(new Consumer<RazorSuccessResp>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull RazorSuccessResp resp) throws Exception {
                    messageHelper.dismissActiveProgress();
                    messageHelper.show(resp.getResMsg());
                    if (isEmpty(resp)) {
                        bundle.putBoolean(Constants.success, false);
                    } else if (isEmpty(resp.getData())) {
                        bundle.putBoolean(Constants.success, false);
                    } else if (isEmpty(resp.getData().get(0))) {
                        bundle.putBoolean(Constants.success, false);
                    } else
                        bundle.putBoolean(Constants.success, true);


                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                    messageHelper.dismissActiveProgress();
                    messageHelper.show(throwable.getMessage());
                    bundle.putBoolean(Constants.success, false);
//                    navigator.navigateActivity(PaySuccessActivity.class, bundle);

                }
            });
        }

    }
/*
    public PayuHashes generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
        PayuHashes payuHashes = new PayuHashes();
        PostData postData = new PostData();

        // payment Hash;
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setAmount(mPaymentParams.getAmount());
        checksum.setKey(mPaymentParams.getKey());
        checksum.setRazorPayTxnid(mPaymentParams.getTxnId());
        checksum.setEmail(mPaymentParams.getEmail());
        checksum.setSalt(salt);
        checksum.setProductinfo(mPaymentParams.getProductInfo());
        checksum.setFirstname(mPaymentParams.getFirstName());
        checksum.setUdf1(mPaymentParams.getUdf1());
        checksum.setUdf2(mPaymentParams.getUdf2());
        checksum.setUdf3(mPaymentParams.getUdf3());
        checksum.setUdf4(mPaymentParams.getUdf4());
        checksum.setUdf5(mPaymentParams.getUdf5());

        postData = checksum.getHash();
        if (postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setPaymentHash(postData.getResult());
        }

        // checksum for payemnt related details
        // var1 should be either user credentials or default
        String var1 = mPaymentParams.getUserCredentials() == null ? PayuConstants.DEFAULT : mPaymentParams.getUserCredentials();
        String key = mPaymentParams.getKey();

        if ((postData = calculateHash(key, PayuConstants.PAYMENT_RELATED_DETAILS_FOR_MOBILE_SDK, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // Assign post data first then check for success
            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(postData.getResult());
        //vas
        if ((postData = calculateHash(key, PayuConstants.VAS_FOR_MOBILE_SDK, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setVasForMobileSdkHash(postData.getResult());

        // getIbibocodes
        if ((postData = calculateHash(key, PayuConstants.GET_MERCHANT_IBIBO_CODES, PayuConstants.DEFAULT, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
            payuHashes.setMerchantIbiboCodesHash(postData.getResult());

        if (!var1.contentEquals(PayuConstants.DEFAULT)) {
            // get user card
            if ((postData = calculateHash(key, PayuConstants.GET_USER_CARDS, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) // todo rename storedc ard
                payuHashes.setStoredCardsHash(postData.getResult());
            // save user card
            if ((postData = calculateHash(key, PayuConstants.SAVE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setSaveCardHash(postData.getResult());
            // delete user card
            if ((postData = calculateHash(key, PayuConstants.DELETE_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setDeleteCardHash(postData.getResult());
            // edit user card
            if ((postData = calculateHash(key, PayuConstants.EDIT_USER_CARD, var1, salt)) != null && postData.getCode() == PayuErrors.NO_ERROR)
                payuHashes.setEditCardHash(postData.getResult());
        }

        if (mPaymentParams.getOfferKey() != null) {
            postData = calculateHash(key, PayuConstants.OFFER_KEY, mPaymentParams.getOfferKey(), salt);
            if (postData.getCode() == PayuErrors.NO_ERROR) {
                payuHashes.setCheckOfferStatusHash(postData.getResult());
            }
        }

        if (mPaymentParams.getOfferKey() != null && (postData = calculateHash(key, PayuConstants.CHECK_OFFER_STATUS, mPaymentParams.getOfferKey(), salt)) != null && postData.getCode() == PayuErrors.NO_ERROR) {
            payuHashes.setCheckOfferStatusHash(postData.getResult());
        }

        // we have generated all the hases now lest launch sdk's ui
        return payuHashes;
    }

    private PostData calculateHash(String key, String command, String var1, String salt) {
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setKey(key);
        checksum.setCommand(command);
        checksum.setVar1(var1);
        checksum.setSalt(salt);
        return checksum.getHash();
    }*/

    public void afterPayment(Bundle data) {
        navigator.navigateActivity(PaySuccessActivity.class, data);
    }


}

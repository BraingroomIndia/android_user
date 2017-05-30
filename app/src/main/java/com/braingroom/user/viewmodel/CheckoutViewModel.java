package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.dto.ClassLevelData;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.dto.PayUCheckoutData;
import com.braingroom.user.model.request.PayUBookingDetailsReq;
import com.braingroom.user.model.request.RazorSuccessReq;
import com.braingroom.user.model.response.PromocodeResp;
import com.braingroom.user.model.response.RazorSuccessResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CheckoutActivity;
import com.braingroom.user.view.adapters.ViewProvider;
import com.payUMoney.sdk.PayUmoneySdkInitilizer;
import com.payu.india.Extras.PayUChecksum;
import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Model.PostData;
import com.payu.india.Payu.PayuConstants;
import com.payu.india.Payu.PayuErrors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import lombok.Getter;

import static com.braingroom.user.FCMInstanceIdService.TAG;

public class CheckoutViewModel extends ViewModel {

    public final int GUEST_USER = 1;
    public final int REGISTERED_USER = 0;
    public final ListDialogViewModel1 locationsVm;
    public final ObservableInt totalAmount;
    public final ObservableInt totalAmountAfterPromo;

    public final Action onProceedClicked, onApplyPromoCode, onOpenPromoCode, onShowPriceDetailsClicked;

    public final ObservableField<String> couponCode;
    public final ObservableField<String> promoCode;
    public String appliedPromoCodeId;
    public final ObservableField<String> appliedCouponCode;
    public final ObservableField<String> appliedPromoCode;
    public float appliedPromoAmount = 0;

    public final ObservableField<String> classImage;
    public final ObservableField<String> defaultPrice;
    public final ObservableField<String> classTopic;
    public final ObservableField<String> classDate;
    public final CheckoutActivity.UiHelper uiHelper;
    public final ObservableBoolean isLocation = new ObservableBoolean(true);

    public final List<String> pricingTableList = new ArrayList<>();

    public String selectedLocalityId;

    final ClassData classData;
    PayUCheckoutData mChekcoutData;
    public final boolean usePayU = false;
    MessageHelper messageHelper;
    Navigator navigator;


    private PayUChecksum checksum;

    final Map<String, Integer> sublevelTextMap = new HashMap<>();

    public interface UiHelper {
        void initiateGuestPayment(String userId);
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
            for (ViewModel nonReactiveItem : nonReactiveItems) {
                totalPrice = totalPrice + ((LevelPricingItemViewModel) nonReactiveItem).totalPrice.get();
            }
            totalAmount.set(totalPrice);
            totalAmountAfterPromo.set((int) (totalPrice - appliedPromoAmount));
        }
    };

    public CheckoutViewModel(@NonNull final HelperFactory helperFactory, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, final CheckoutActivity.UiHelper uiHelper, final ClassData classData) {

        String temp= classData.getPricingType().equalsIgnoreCase("Group")?classData.getLevelDetails().get(0).getGroups().get(0).getPrice():
                classData.getLevelDetails().get(0).getPrice();
        totalAmount = new ObservableInt(0);

        totalAmountAfterPromo = new ObservableInt(0);
        couponCode = new ObservableField<>();
        appliedCouponCode = new ObservableField<>();
        promoCode = new ObservableField<>();
        appliedPromoCode = new ObservableField<>();
        this.uiHelper = uiHelper;
        this.classData = classData;

        this.messageHelper = messageHelper;
        this.navigator = navigator;
        if (classData.getClassType().equalsIgnoreCase("Online Classes") || classData.getClassType().equalsIgnoreCase("Webinars"))//Edited by Vikas Godara;
            isLocation.set(false);
        classImage = new ObservableField<>(classData.getImage());
        defaultPrice = new ObservableField<>(classData.getLevelDetails().get(0).getPrice());
        classTopic = new ObservableField<>(classData.getClassTopic());
        if ("fixed".equalsIgnoreCase(classData.getClassTypeData())) {
            classDate = new ObservableField<>(classData.getSessionTime() + ", " + classData.getSessionDate());
        } else {
            classDate = new ObservableField<>();
        }

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
        });
        if (!isLocation.get())
            selectedLocalityId = "-1";

        List<Integer> priceList;
        for (ClassLevelData classLevelData : classData.getLevelDetails()) {
            priceList = new ArrayList<>();
            if ("group".equalsIgnoreCase(classData.getPricingType())) {
                int range = 0;
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
                if (!loggedIn.get()) {
                    helperFactory.createDialogHelper()
                            .showCustomView(R.layout.content_guest_payment_dialog, new GuestPaymentDialogViewModel(messageHelper, navigator, new UiHelper() {
                                @Override
                                public void initiateGuestPayment(String userId) {
                                    try {

                                        startPayment(userId, GUEST_USER);
                                    } catch (JSONException e) {
                                        messageHelper.show("Something went wrong. JSON error");
                                    }
                                }
                            }));
                    return;
                }
                startPayment(pref.getString(Constants.BG_ID, ""), REGISTERED_USER);

            }
        };


        onOpenPromoCode = new Action() {
            @Override
            public void run() throws Exception {
                promoCode.set("");
                appliedPromoCode.set(null);
                appliedPromoAmount = 0;
                dataChangeAction.run();

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
                if (promoCode != null && promoCode.get().length() > 3) {
                    apiService.applyPromoCode(promoCode.get()).subscribe(new Consumer<PromocodeResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull PromocodeResp resp) throws Exception {
                            if (resp.getData().size() > 0) {
                                appliedPromoCode.set(resp.getData().get(0).getPromoCode());
                                appliedPromoAmount = Float.parseFloat(resp.getData().get(0).getAmount());
                                appliedPromoCodeId = resp.getData().get(0).getId();
                                dataChangeAction.run();
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
                } else {
                    messageHelper.show("code length should be more than 3");
                }
            }
        };

    }

    public void startPayment(String userId, int isGuest) throws JSONException {
        if (totalAmountAfterPromo.get() != 0) {
            PayUBookingDetailsReq.Snippet snippet = new PayUBookingDetailsReq.Snippet();
            snippet.setTxnId(UUID.randomUUID().toString());
            snippet.setAmount("" + totalAmountAfterPromo.get());
            snippet.setClassId(classData.getId());
            snippet.setLocalityId(selectedLocalityId);
            snippet.setIsGuest(isGuest);
            // TODO remove hardcoded
            snippet.setUserId(userId);
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
            apiService.getPayUCheckoutData(new PayUBookingDetailsReq(snippet)
                    , appliedPromoCodeId != null ? appliedPromoCodeId : null
                    , appliedPromoCodeId != null ? appliedPromoCode.get() : null).subscribe(new Consumer<PayUCheckoutData>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull PayUCheckoutData chekcoutData) throws Exception {
                    messageHelper.dismissActiveProgress();
                    mChekcoutData = chekcoutData;
                    if (usePayU) {

//                                PaymentParams mPaymentParams = new PaymentParams();
//                                mPaymentParams.setKey(chekcoutData.getKey());
//                                mPaymentParams.setKey("TmRBpDL0");
//                                mPaymentParams.setAmount(chekcoutData.getAmount());
//                                mPaymentParams.setProductInfo(chekcoutData.getProductinfo());
//                                mPaymentParams.setFirstName(chekcoutData.getFirstname());
//                                mPaymentParams.setEmail(chekcoutData.getEmail());
//                                mPaymentParams.setPhone(chekcoutData.getPhone());
//                                mPaymentParams.setTxnId(chekcoutData.getTxnId());
//                                mPaymentParams.setTxnId("" + System.currentTimeMillis());
//                                mPaymentParams.setSurl(chekcoutData.getSurl());
//                                mPaymentParams.setFurl(chekcoutData.getFurl());
//                                mPaymentParams.setUdf1("udf1");
//                                mPaymentParams.setUdf2("udf2");
//                                mPaymentParams.setUdf3("udf3");
//                                mPaymentParams.setUdf4("udf4");
//                                mPaymentParams.setUdf5("udf5");
//                                mPaymentParams.setUdf1(chekcoutData.getUdf1());
//                                mPaymentParams.setUdf2(chekcoutData.getUdf2());
//                                mPaymentParams.setUdf3(chekcoutData.getUdf3());
//                                mPaymentParams.setUdf4(chekcoutData.getUdf4());
//                                mPaymentParams.setUserCredentials(chekcoutData.getKey() + ":" + chekcoutData.getEmail());
//                                mPaymentParams.setHash(chekcoutData.getVasMobileSdkHash());

//                                PayuConfig payuConfig = new PayuConfig();
//                                payuConfig.setEnvironment(PayuConstants.STAGING_ENV);
//                                PayuHashes hashes = new PayuHashes();
//                                hashes.setPaymentHash(chekcoutData.getPaymentHash());
//                                hashes.setVasForMobileSdkHash(chekcoutData.getVasMobileSdkHash());
//                                hashes.setSaveCardHash(chekcoutData.getUserCardHash());
//                                hashes.setPaymentRelatedDetailsForMobileSdkHash(chekcoutData.getPaymentMobileSdkHash());
//                                String salt = "v7DfI78GT5";

                        PayUmoneySdkInitilizer.PaymentParam.Builder builder = new
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
                        uiHelper.startPayUPayment(param);
                    } else {
                        JSONObject options = new JSONObject();
                        options.put("name", classData.getClassTopic());
                        options.put("description", "By: " + classData.getTeacher());
                        //You can omit the image option to fetch the image from dashboard
                        options.put("image", "https://www.braingroom.com/homepage/img/logo.jpg");
                        options.put("currency", "INR");
                        options.put("amount", "" + Integer.parseInt(chekcoutData.getAmount()) * 100);

                        JSONObject preFill = new JSONObject();
                        preFill.put("email", chekcoutData.getEmail());
                        preFill.put("contact", chekcoutData.getPhone());
                        options.put("prefill", preFill);
                        Log.d(TAG, "accept: name\t:\t"+classData.getClassTopic()+"\ndescription, By: " + classData.getTeacher()
                        +"\namount\t:\t"+ Integer.parseInt(chekcoutData.getAmount()) * 100+"\nemail\t:\t"+ chekcoutData.getEmail()
                                +"\ncontact\t:\t"+ chekcoutData.getPhone());
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
        } else {
            messageHelper.show("select classes first");
        }
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

    public void handleRazorpaySuccess(String razorpayId) {

        messageHelper.showDismissInfo(null, "Processing your payment...");
        RazorSuccessReq.Snippet snippet = new RazorSuccessReq.Snippet();
        snippet.setAmount("" + totalAmountAfterPromo.get());
        snippet.setClassId(classData.getId());
        // TODO remove hardcoding
        snippet.setUserId(mChekcoutData.getUdf1());
        snippet.setLocalityId(selectedLocalityId);
        snippet.setTxnid(razorpayId);
        snippet.setUserEmail(mChekcoutData.getEmail());
        snippet.setUserMobile(mChekcoutData.getPhone());
        // TODO: 19/04/17 remove hardcoded user id
        snippet.setUserId(pref.getString(Constants.BG_ID,""));
        Log.d(TAG, "\n\n\nhandleRazorpaySuccess: amount\t:\t" +totalAmountAfterPromo.get()+"\nclassID\t:\t"+classData.getId()+
                "\nUserID\t:\t"+pref.getString(Constants.BG_ID,"")+"LocalityId\t:\t"+selectedLocalityId+"\n TxnId\t:\t"+razorpayId+
                "\nEmailId\t:\t"+mChekcoutData.getEmail()+"\nPhoneNo.\t:\t"+mChekcoutData.getPhone()+"\n\n\n" );

        List<RazorSuccessReq.Levels> levelsList = new ArrayList<>();
        for (ViewModel nonReactiveItem : nonReactiveItems) {
            if (Integer.parseInt(((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()) > 0) {
                levelsList.add(new RazorSuccessReq.Levels(((LevelPricingItemViewModel) nonReactiveItem).levelId, ((LevelPricingItemViewModel) nonReactiveItem).countVm.countText.get()));
            }
        }
        snippet.setTickets(gson.toJson(levelsList));
        apiService.postRazorpaySuccess(new RazorSuccessReq(snippet)).subscribe(new Consumer<RazorSuccessResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull RazorSuccessResp resp) throws Exception {
                messageHelper.dismissActiveProgress();
                messageHelper.show(resp.getResMsg());
                navigator.finishActivity();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                messageHelper.dismissActiveProgress();
                messageHelper.show(throwable.getMessage());
            }
        });
    }

    public PayuHashes generateHashFromSDK(PaymentParams mPaymentParams, String salt) {
        PayuHashes payuHashes = new PayuHashes();
        PostData postData = new PostData();

        // payment Hash;
        checksum = null;
        checksum = new PayUChecksum();
        checksum.setAmount(mPaymentParams.getAmount());
        checksum.setKey(mPaymentParams.getKey());
        checksum.setTxnid(mPaymentParams.getTxnId());
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
    }


}

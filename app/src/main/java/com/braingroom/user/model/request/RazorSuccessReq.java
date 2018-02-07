package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class RazorSuccessReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {

        @SerializedName("user_id")
        public String userId;

        @SerializedName("is_guest")
        int isGuest;

        @SerializedName("user_email")
        public String userEmail;

        @SerializedName("user_mobile")
        public String userMobile;

        @SerializedName("locality_id")
        public String localityId;

        @SerializedName("class_id")
        public String classId;

        @SerializedName("tickets")
        public String tickets;

        @SerializedName("amount")
        public String amount;

        @SerializedName("txnid")
        public String razorPayTxnid;

        @SerializedName("bg_txnid")
        private String bgTxnid;

        @SerializedName("term_id")
        private String termId;

        @SerializedName("coupon_id")
        private String couponCode;

        @SerializedName("coupon_value")
        private String couponAmount;

        @SerializedName("promo_id")
        private String promoCode;

        @SerializedName("promo_value")
        private String promoAmount;

        @SerializedName("book_type")
        private int bookingType;

        @SerializedName("is_insentive")
        private String isIncentive;

        @SerializedName("cod_amount")
        private String codAmount;

        @SerializedName("payment_mode")
        private int paymentMode;


        @SerializedName("price_code")
        private String priceCode;

        @SerializedName("stripe_token")
        private String stripeToken;
    }

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Levels {
        @SerializedName("level_id")
        String levelId;
        @SerializedName("tickets")
        String tickets;
    }

}

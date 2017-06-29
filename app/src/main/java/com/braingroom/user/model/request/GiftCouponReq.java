package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * Created by godara on 28/06/17.
 */


@AllArgsConstructor(suppressConstructorProperties = true)
public class GiftCouponReq {

    @SerializedName("braingroom")
    public Snippet data;

    @Setter
    public static class Snippet {

        @SerializedName("user_id")
        public String userId;

        @SerializedName("gift_type")
        public String giftType;

        @SerializedName("gift_by")
        public String giftBy;

        @SerializedName("gift_details")
        public List<GiftDetail> giftDetails;


    }

    @Setter
    private static class GiftDetail {

        @SerializedName("cat_id")
        public String catId;

        @SerializedName("denomination")
        public String denomination;

        @SerializedName("email_id")
        public String emailId;

        @SerializedName("no_coupons")
        public String noCoupons;

        @SerializedName("recipient_name")
        public String recipientName;

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("comment")
        public String comment;

    }

}



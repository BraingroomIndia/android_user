package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by godara on 22/05/17.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SaveGiftCouponReq {

    @SerializedName("user_id")
    public String userId;
    @SerializedName("gift_type")
    public String giftType;
    @SerializedName("gift_by")
    public String giftBy;

    @SerializedName("gift_details")
    List<Snippet> data;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {
        @SerializedName("cat_id")
        private String catId;

        @SerializedName("denomination")
        private String denomination;

        @SerializedName("email_id")
        private String emailId;

        @SerializedName("no_coupons")
        private String noCoupons;

        @SerializedName("recipient_name")
        private String recipientName;

        @SerializedName("mobile")
        private String mobile;

        @SerializedName("comment")
        private String comment;
    }
}

package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/24/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class RazorBuySuccessReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {
        @SerializedName("term_id")
        private String termId;

        @SerializedName("amount")
        private String amount;

        @SerializedName("razorPayTxnid")
        private String txnid;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("user_email")
        private String userEmail;

        @SerializedName("user_mobile")
        private String userMobile;

        @SerializedName("is_guest")
        private String isGuest;

    }

}

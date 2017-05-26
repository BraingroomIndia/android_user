package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
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
        public String txnid;
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

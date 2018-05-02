package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class GetBookingDetailsReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {

        @SerializedName("locality_id")
        String localityId;

        @SerializedName("amount")
        String amount;

        @SerializedName("class_id")
        String classId;

        @SerializedName("levels")
        String levels;

        @SerializedName("txnid")
        String txnId = "";

        @SerializedName("user_id")
        String userId;

        @SerializedName("is_guest")
        int isGuest;

        @SerializedName("class_session_id")
        String classSessionId;
    }

//    @Data
//    @AllArgsConstructor(suppressConstructorProperties = true)
//    public static class Levels {
//        @SerializedName("level_id")
//        String levelId;
//        @SerializedName("tickets")
//        String tickets;
//    }
}

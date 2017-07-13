package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class PromocodeReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {
        @SerializedName("user_id")
        String userId;

        @SerializedName("class_id")
        String classId;

        @SerializedName("total_ticket")
        String totalTicket;

        @SerializedName("is_guest")
        int isGuest;

        @SerializedName("promo_code")
        String code;

        @SerializedName("total_amount")
        String totalAmount;
    }
}

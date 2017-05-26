package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class PayUHashResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {

        @SerializedName("payment_hash")
        String paymentHash;
        @SerializedName("vas_for_mobile_sdk_hash")
        String vasMobileSdkHash;
        @SerializedName("payment_related_details_for_mobile_sdk_hash")
        String paymentMobileSdkHash;
        @SerializedName("save_user_card_hash")
        String userCardHash;

    }
}

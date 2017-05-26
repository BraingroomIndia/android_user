package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class PayUHashGenReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {

        @SerializedName("key")
        String key;

        @SerializedName("txnid")
        String txnId;

        @SerializedName("amount")
        String amount;

        @SerializedName("productinfo")
        String productInfo;

        @SerializedName("firstname")
        String firstName;

        @SerializedName("email")
        String email;

        @SerializedName("phone")
        String phone;

        @SerializedName("udf1")
        String udf1;

        @SerializedName("udf2")
        String udf2;

        @SerializedName("udf3")
        String udf3;

        @SerializedName("udf4")
        String udf4;

        @SerializedName("surl")
        String sUrl = "https://www.braingroom.com/vendor_classes/paySuccess";

        @SerializedName("furl")
        String fUrl = " https://www.braingroom.com/Homes/payFailure";

        @SerializedName("service_provider")
        String serviceProvider = "payu_paisa";

        @SerializedName("promoid")
        String promoId;

        @SerializedName("promovalue")
        String promoValue;

    }
}

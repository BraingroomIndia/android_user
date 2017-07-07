package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/24/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SocialLoginReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {

        @SerializedName("user_type")
        String userType = "2";

        String phone = "";

        @SerializedName("first_name")
        String firstName;

        @SerializedName("last_name")
        String lastName = "";

        @SerializedName("email")
        String email ="";

        @SerializedName("social_network_id")
        String socialId = "";

        @SerializedName("address_latitude")
        String latitude = "";

        @SerializedName("address_longitude")
        String longitude = "";

        @SerializedName("ip_address")
        String ipAddress = "";

        @SerializedName("reg_id")
        String regId = "";

        @SerializedName("referralCode")
        String referralCode;

    }

}

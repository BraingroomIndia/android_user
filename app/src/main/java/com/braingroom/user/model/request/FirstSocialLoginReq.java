package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 24/05/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class FirstSocialLoginReq {
    @SerializedName("braingroom")
    Snippet data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("user_id")
        String userId;

        @SerializedName("email")
        String emailId;

        @SerializedName("mobile")
        String mobile;

        @SerializedName("referal_code")
        String referralCode;
    }
}

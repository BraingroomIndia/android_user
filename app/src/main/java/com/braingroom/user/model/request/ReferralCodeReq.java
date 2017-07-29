package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

/**
 * Created by godara on 29/07/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class ReferralCodeReq {
    @SerializedName("braingroom")
    Snippet data;

    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("referal_code")
        String referralCode;
    }
}

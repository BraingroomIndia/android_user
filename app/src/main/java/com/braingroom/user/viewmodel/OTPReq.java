package com.braingroom.user.viewmodel;

import com.braingroom.user.model.request.CommonUserIdReq;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 03/07/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class OTPReq {
    @SerializedName("braingroom")
    Snippet data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("user_id")
        String userId;

        @SerializedName("mobile")
        String mobile;
    }
}

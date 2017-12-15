package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 27/07/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class RegisterUserDeviceReq {
    @SerializedName("braingroom")
    Snippet data;

    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("device_id")
        String deviceId;

        @SerializedName("user_id")
        String userId;
    }
}

package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 23/06/17.
 */

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class LogoutReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {

        @SerializedName("user_id")
        public String userId;

        @SerializedName("device_id")
        public String deviceId;
    }

}
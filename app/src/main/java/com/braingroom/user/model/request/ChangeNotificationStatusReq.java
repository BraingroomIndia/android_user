package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

/**
 * Created by godara on 21/06/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class ChangeNotificationStatusReq {


    @SerializedName("braingroom")
    Snippet data;

    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("user_id")
        public String userId;

        @SerializedName("notification_id")
        public String notificationId;
    }
}

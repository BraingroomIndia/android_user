package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * Created by godara on 17/05/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class ChangePasswordReq {

    @SerializedName("braingroom")
    public Snippet data;

    @Data
    @Setter
    public static class Snippet {

        @SerializedName("uuid")
        public String uuid;
        @SerializedName("old_password")
        public String oldPassword;
        @SerializedName("new_password")
        public String newPassword;

    }

}
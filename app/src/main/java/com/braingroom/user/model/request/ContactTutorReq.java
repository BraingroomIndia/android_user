package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 31/08/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class ContactTutorReq {
    @SerializedName("braingroom")
    Snippet data;

    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("user_id")
        String userId;

        @SerializedName("class_id")
        String classId;

        @SerializedName("message")
        String message;
    }
}


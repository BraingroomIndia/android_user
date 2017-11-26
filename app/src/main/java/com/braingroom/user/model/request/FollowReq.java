package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

/*
 * Created by godara on 23/08/17.
*/
@AllArgsConstructor(suppressConstructorProperties = true)
public class FollowReq {

    @SerializedName("braingroom")
    public Snippet data;

    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("following_user_id")
        public String followingUserId;

        @SerializedName("user_id")
        public String userId;
    }
}

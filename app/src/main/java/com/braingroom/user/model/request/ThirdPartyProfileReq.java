package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * Created by godara on 30/08/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
public class ThirdPartyProfileReq {
    @SerializedName("braingroom")
    Snippet data;

    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {

        @SerializedName("id")
        public String id;
        @SerializedName("following_id")
        public String followingId;
    }
}

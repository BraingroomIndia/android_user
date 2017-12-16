package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 15/12/17.
 */

public class DeepLinkDataReq {

    @SerializedName("braingroom")
    private Snippet data;

    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("url")
        private String url;
    }

    public DeepLinkDataReq(String url) {
        data = new Snippet(url);
    }
}

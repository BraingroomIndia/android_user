package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 26/05/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class InstituteReq {

    @SerializedName("braingroom")
    public Snippet data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet{
        @SerializedName("search_key")
        public String searchKey;

    }
}

package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 06/09/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class QuoteDetailsReq {
    @SerializedName("braingroom")
    Snippet data;

    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("quote_id")
        String quoteId;
    }
}

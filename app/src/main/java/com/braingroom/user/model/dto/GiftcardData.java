package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by agrahari on 26/03/17.
 */

public class GiftcardData implements Serializable {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("id")
        public String id;

        @SerializedName("title")
        public String cardName;

        @SerializedName("gift_image")
        public String cardImage;

    }
}

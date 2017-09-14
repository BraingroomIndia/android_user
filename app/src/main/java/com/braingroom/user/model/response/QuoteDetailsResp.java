package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Created by godara on 06/09/17.
 */

public class QuoteDetailsResp extends BaseResp {

    @Getter
    @SerializedName("braingroom")
    List<Snippet> data;

    @Getter
    public static class Snippet {

        @SerializedName("id")
        private String id;

        @SerializedName("class_id")
        private String classId;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("class_name")
        private String className;

        @SerializedName("class_image")
        private String classImage;

        @SerializedName("organization")
        private String organization;

        @SerializedName("location")
        private String location;

        @SerializedName("class_mode")
        private String classMode;

        @SerializedName("single_unit_price")
        private int unitPrice;

        @SerializedName("single_unit_message")
        private String unitMessage;

        @SerializedName("total_price")
        private int totalPrice;

        @SerializedName("total_message")
        private String totalMessage;

        @SerializedName("catalog_id")
        private String catalogId;

        @SerializedName("added_date")
        private String date;


    }
}

package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class SaveGiftCouponResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {
        @SerializedName("price")
        private int price;

        @SerializedName("term_id")
        private String termId;

        @SerializedName("key")
        private String key;

        @SerializedName("name")
        private String name;

        @SerializedName("phone")
        private String mobile;

        @SerializedName("email")
        private String email;

        @SerializedName("description")
        private String description;

        @SerializedName("image")
        private String image;

        @SerializedName("success_url")
        private String successUrl;
    }
}

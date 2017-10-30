package com.braingroom.user.model.QRCode;

import com.braingroom.user.model.request.ClassDetailReq;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;

/**
 * Created by godara on 17/10/17.
 */

public class ClassDetail {
    public String type;

    @SerializedName("braingroom")
    public Snippet reqData;

    @Getter
    public static class Snippet {
        @SerializedName("id")
        String id;

        @SerializedName("promo_code")
        String promoCode;

    }
}

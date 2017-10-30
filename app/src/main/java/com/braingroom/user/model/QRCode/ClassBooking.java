package com.braingroom.user.model.QRCode;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 25/10/17.
 */

public class ClassBooking {

    public String type;

    @SerializedName("braingroom")
    public Snippet reqData;

    public static class Snippet {
        @SerializedName("id")
        public String id;

        @SerializedName("promo_code")
        public String promoCode;

    }
}

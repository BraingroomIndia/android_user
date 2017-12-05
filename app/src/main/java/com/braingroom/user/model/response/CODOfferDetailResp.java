package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/*
 * Created by godara on 01/12/17.
*/

@Getter
public class CODOfferDetailResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Getter
    public static class Snippet {

        @SerializedName("offer_percentage")
        private float discountFactor;

        @SerializedName("title")
        public String title;

        @SerializedName("content")
        public String message;

        public Snippet() {
            this.discountFactor = 0;
            this.title = "Dummy Title";
            this.message = "Dummy Message";
        }

        public float getDiscountFactor() {
            return (float) ((float) 1 - (discountFactor / (float) 100));
        }
    }

    public CODOfferDetailResp() {
        super("0", "Network error");
        data = new ArrayList<>();
        data.add(new Snippet());

    }
}

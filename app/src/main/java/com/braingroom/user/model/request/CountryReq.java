package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;


/**
 * Created by godara on 10/12/17.
 */

public class CountryReq {

    @SerializedName("braingroom")
    private Snippet data;

    public static class Snippet {
        @SerializedName("only_major_countries")
        String majorCountry;

        public Snippet() {
            this.majorCountry = "1";
        }

    }

    public CountryReq() {
        data = new Snippet();
    }
}

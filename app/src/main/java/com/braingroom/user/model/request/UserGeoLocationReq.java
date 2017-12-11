package com.braingroom.user.model.request;

import com.braingroom.user.utils.Constants;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;

/**
 * Created by godara on 11/12/17.
 */

public class UserGeoLocationReq {

    @SerializedName("braingroom")
    Snippet data;

    public static class Snippet {

        @SerializedName("geo")
        private String geo;

        public Snippet() {
            geo = Constants.GEO_TAG;
        }
    }

    public UserGeoLocationReq() {
        this.data = new Snippet();
    }

}

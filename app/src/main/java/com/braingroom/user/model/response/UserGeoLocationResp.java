package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

import lombok.Getter;

/**
 * Created by godara on 11/12/17.
 */

public class UserGeoLocationResp {

    @SerializedName(value = "res_code", alternate = {"res_id"})
    private String resCode;

    @Getter
    @SerializedName("res_msg")
    private String resMsg;


    @SerializedName("braingroom")
    private List<Snippet> data;


    public Snippet getData() {
        if (getResCode())
            return data.get(0);
        else return new Snippet();
    }


    public boolean getResCode() {
        return !(data == null || data.isEmpty() || data.get(0) == null);
    }

    @Getter
    public static class Snippet {


        @SerializedName("title")
        private String title;

        @SerializedName("content")
        private String message;

        @SerializedName("country_id")
        private String countryId;

        @SerializedName("country_name")
        private String countryName;

        @SerializedName("city_id")
        private String cityId;

        @SerializedName("city_name")
        private String cityName;

    }
}

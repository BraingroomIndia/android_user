package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class CityReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data

    public static class Snippet {
        @SerializedName("state_id")
        String id;
        @SerializedName("only_major_cities")
        String majorCity;
        @SerializedName("country_id")
        String countryId;

        public Snippet(String id) {
            this.id = id;
            majorCity = null;
            countryId = null;
        }

        public Snippet(boolean majorCity) {
            this.id = null;
            this.majorCity = majorCity ? "1" : null;
        }

        public Snippet(boolean majorCity, String countryId) {
            this.id = null;
            this.majorCity = majorCity ? "1" : null;
            this.countryId = countryId;
        }
    }
}

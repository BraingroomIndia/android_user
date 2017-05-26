package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class StateReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("country_id")
        String countryId;
    }
}

package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class MarkerDataReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {

        @SerializedName("lat")
        String latitude;

        @SerializedName("lng")
        String longitude;

    }
}

package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class OnlineBookinHistoryReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("id")
        String id;

        @SerializedName("class_type")
        String classType = "2";

        public Snippet(String id) {
            this.id = id;
        }
    }
}

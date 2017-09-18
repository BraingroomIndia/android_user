package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * Created by himan on 1/24/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class LoginReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {
        String email;
        String password = "";
        @SerializedName("social_network_id")
        String socialId = "";
        String latitude = "";
        String longitude = "";
        @SerializedName("reg_id")
        String regId = "";
    }

}

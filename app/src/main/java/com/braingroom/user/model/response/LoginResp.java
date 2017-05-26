package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/24/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class LoginResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        String id;
        String uuid;
        @SerializedName("profile_pic")
        String profilePic;
        @SerializedName("city_id")
        String cityId;
        String name;
        @SerializedName("mobile")
        String mobile;
        @SerializedName("referal_code")
        String referralCode;
    }

}

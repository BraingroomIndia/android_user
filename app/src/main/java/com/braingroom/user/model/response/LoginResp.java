package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by himan on 1/24/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class LoginResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("id")
        String id;

        @SerializedName("uuid")
        String uuid;

        @SerializedName("profile_pic")
        String profilePic;

        @SerializedName("city_id")
        String cityId;

        String name;

        @SerializedName("mobile")
        String mobile;

        @SerializedName("is_mobile_verified")
        int isVerified;

        @SerializedName("referal_code")
        String referralCode;

        @SerializedName("login_type")
        String loginType;

        @SerializedName("email")
        private String emailId;

        private String password;
    }

}

package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by godara on 01/08/17.
 */

@Getter
public class FirstSocialLoginResp extends BaseResp {
    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("mobile")
        private String mobile;

        @SerializedName("referal_code")
        private String referralCode;

        @SerializedName("user_id")
        private String userId;

    }

}

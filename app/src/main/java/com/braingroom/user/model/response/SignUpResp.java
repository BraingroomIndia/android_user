package com.braingroom.user.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by godara on 27/05/17.
 */
@Getter
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class SignUpResp extends BaseResp {
    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("uuid")
        public String uuid;

        @SerializedName("user_id")
        String userId;

        @Expose(serialize = false)
        String loginType;

        @Expose(serialize = false)
        String emailId;

        @Expose(serialize = false)
        String mobileNumber;

        @Expose(serialize = false)
        private String password;


    }
}

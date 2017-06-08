package com.braingroom.user.model.response;

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
public class SignUpResp extends BaseResp{
    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet{
        @SerializedName("first_name")
        public String firstName;

        @SerializedName("email")
        public String email;

        @SerializedName("password")
        public String password;

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("country_id")
        public String countryId;

        @SerializedName("state_id")
        public String stateId;

        @SerializedName("city_id")
        public String cityId;

        @SerializedName("locality_id")
        public String localityId;

        @SerializedName("category_id")
        public String categoryId;

        @SerializedName("referal_code")
        public String referalCode;

        @SerializedName("community_id")
        public String communityId;

        @SerializedName("d_o_b")
        public String dOB;

        @SerializedName("gender")
        public String gender;

        @SerializedName("profile_image")
        public String profileImage;

        @SerializedName("latitude")
        public String latitude;

        @SerializedName("longitude")
        public String longitude;

        @SerializedName("user_type_id")
        public Integer userTypeId;

        @SerializedName("status")
        public Integer status;

        @SerializedName("uuid")
        public String uuid;

        @SerializedName("add_date")
        public Integer addDate;

        @SerializedName("modify_date")
        public Integer modifyDate;

        @SerializedName("sync_time")
        public Integer syncTime;

        @SerializedName("verification_code")
        public String verificationCode;
    }
}

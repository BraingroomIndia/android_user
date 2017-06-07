package com.braingroom.user.model.request;

import com.braingroom.user.model.response.BaseResp;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * Created by godara on 11/05/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class SignUpReq extends BaseResp{
    @SerializedName("braingroom")
    Snippet data;

    @Data
    @Setter
    public static class Snippet implements Serializable{

        @SerializedName("name")
        public String name;

        @SerializedName("email")
        public String email;

        @SerializedName("password")
        public String password;

        @SerializedName("mobile_no")
        public String mobileNo;

        @SerializedName("country")
        private String country;

        @SerializedName("state")
        private String state;

        @SerializedName("city")
        public String cityId;

        @SerializedName("locality")
        public String locality;

        @SerializedName("category_id")
        public String categoryId;

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

        @SerializedName("community_id")
        public String communityId;

        @SerializedName("school_id")
        String schoolName;

        @SerializedName("institute_name1")
        private String instituteName1;

        @SerializedName("institute_poy1")
        private String institutePoy1;

        @SerializedName("institute_name2")
        private String instituteName2;

        @SerializedName("institute_poy2")
        private String institutePoy2;

        @SerializedName("referal_code")
        private String referalCode;

    }
}

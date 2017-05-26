package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ProfileResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {

        @SerializedName("category_id")
        public String categoryId;

        @SerializedName("category_name")
        public String categoryName;

        @SerializedName("community_id")
        public String communityId;

        @SerializedName("user_preference_id")
        public String userPreferenceId;

        @SerializedName("name")
        public String name;

        @SerializedName("profile_pic")
        public String profilePic;

        @SerializedName("email")
        public String email;

        @SerializedName("contact_no")
        public String contactNo;

        @SerializedName("city_id")
        public String cityId;

        @SerializedName("city")
        public String city;

        @SerializedName("locality_id")
        public String localityId;

        @SerializedName("locality")
        public String locality;

        @SerializedName("institute_name1")
        public String ugInstituteName;

        @SerializedName("institute_poy1")
        public String ugInstitutePassingYear;

        @SerializedName("institute_name2")
        public Object pgInstituteName;

        @SerializedName("institute_poy2")
        public String pgInstitutePassingYear;

        @SerializedName("gender")
        public String gender;

        @SerializedName("dob")
        public String dob;

        @SerializedName("profile_image")
        public String profileImage;

        @SerializedName("uuid")
        public String uuid;


    }
}

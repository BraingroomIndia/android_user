package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by agrahari on 26/03/17.
 */
@Setter
@Getter
public class ProfileData {
    @SerializedName("category_id")
    String categoryId;

    @SerializedName("category_name")
    String categoryName;

    @SerializedName("community_id")
    String communityId;

    @SerializedName("user_preference_id")
    String userPreferenceId;

    @SerializedName("name")
    String name;

    @SerializedName("profile_pic")
    String profilePic;

    @SerializedName("email")
    String email;

    @SerializedName("contact_no")
    String contactNo;

    @SerializedName("city_id")
    String cityId;

    @SerializedName("city")
    String city;

    @SerializedName("locality_id")
    String localityId;

    @SerializedName("locality")
    String locality;

    @SerializedName("institute_name1")
    String ugInstituteName;

    @SerializedName("institute_poy1")
    String ugInstitutePassingYear;

    @SerializedName("institute_name2")
    String pgInstituteName;

    @SerializedName("institute_poy2")
    String pgInstitutePassingYear;

    @SerializedName("gender")
    String gender;

    @SerializedName("d_o_b")
    String dob;

    @SerializedName("profile_image")
    String profileImage;

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("follower_cnt")
    long follower_count;

    @SerializedName("following_cnt")
    long following_count;

    @SerializedName("post_cnt")
    long post_count;


}

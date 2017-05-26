package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ProfileUpdateReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {

        @SerializedName("uuid")
        public String uuid;

        @SerializedName("first_name")
        public String firstName = "";

        @SerializedName("email")
        public String email = "";

        @SerializedName("mobile")
        public String mobile = "";

        @SerializedName("country_id")
        public String countryId = "";

        @SerializedName("state_id")
        public String stateId = "";

        @SerializedName("city_id")
        public String cityId = "";

        @SerializedName("locality_id")
        public String localityId = "";

        @SerializedName("category_id")
        public String categoryId = "";

        @SerializedName("community_id")
        public String communityId = "";

        @SerializedName("institution_name")
        public String institutionName = "";

        @SerializedName("registration_id")
        public String registrationId = "";

        @SerializedName("expertise_area")
        public String expertiseArea = "";

        @SerializedName("address")
        public String address = "";

        @SerializedName("description")
        public String description = "";

        @SerializedName("gender")
        public String gender = "";

        @SerializedName("profile_image")
        public String profileImage = "";

        @SerializedName("primary_verification_media1")
        public String primaryVerificationMedia1 = "";

        @SerializedName("primary_verification_media2")
        public String primaryVerificationMedia2 = "";

        @SerializedName("secoundary_verification_media1")
        public String secoundaryVerificationMedia1 = "";

        @SerializedName("secoundary_verification_media2")
        public String secoundaryVerificationMedia2 = "";

        @SerializedName("secoundary_verification_media3")
        public String secoundaryVerificationMedia = "";

        @SerializedName("official_reg_id")
        public String officialRegId = "";

        @SerializedName("area_of_expertise")
        public String areaOfExpertise = "";

    }
}

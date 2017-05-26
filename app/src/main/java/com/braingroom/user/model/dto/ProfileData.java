package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by agrahari on 26/03/17.
 */

public class ProfileData {
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
    public String pgInstituteName;

    @SerializedName("institute_poy2")
    public String pgInstitutePassingYear;

    @SerializedName("gender")
    public String gender;

    @SerializedName("d_o_b")
    public String dob;

    @SerializedName("profile_image")
    public String profileImage;

    @SerializedName("uuid")
    public String uuid;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getUserPreferenceId() {
        return userPreferenceId;
    }

    public void setUserPreferenceId(String userPreferenceId) {
        this.userPreferenceId = userPreferenceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocalityId() {
        return localityId;
    }

    public void setLocalityId(String localityId) {
        this.localityId = localityId;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getUgInstituteName() {
        return ugInstituteName;
    }

    public void setUgInstituteName(String ugInstituteName) {
        this.ugInstituteName = ugInstituteName;
    }

    public String getUgInstitutePassingYear() {
        return ugInstitutePassingYear;
    }

    public void setUgInstitutePassingYear(String ugInstitutePassingYear) {
        this.ugInstitutePassingYear = ugInstitutePassingYear;
    }

    public String getPgInstituteName() {
        return pgInstituteName;
    }

    public void setPgInstituteName(String pgInstituteName) {
        this.pgInstituteName = pgInstituteName;
    }

    public String getPgInstitutePassingYear() {
        return pgInstitutePassingYear;
    }

    public void setPgInstitutePassingYear(String pgInstitutePassingYear) {
        this.pgInstitutePassingYear = pgInstitutePassingYear;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

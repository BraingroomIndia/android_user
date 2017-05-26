package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by agrahari on 26/03/17.
 */

public class VendorProfileData {

    @SerializedName("id")
    public String id;

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

    @SerializedName("interest")
    public String interest;

    @SerializedName("institution")
    public String institution;

    @SerializedName("expertise_area")
    public String expertiseArea;

    @SerializedName("user_rating")
    public String userRating;

    @SerializedName("address")
    public String address;

    @SerializedName("description")
    public String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getExpertiseArea() {
        return expertiseArea;
    }

    public void setExpertiseArea(String expertiseArea) {
        this.expertiseArea = expertiseArea;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

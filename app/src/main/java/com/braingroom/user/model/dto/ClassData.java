package com.braingroom.user.model.dto;

import android.text.Spanned;
import android.text.TextUtils;

import com.braingroom.user.utils.CommonUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/*
 * Created by agrahari on 26/03/17.
 */
@Getter
@Setter
public class ClassData implements Serializable {


    @SerializedName("id")
    private String id;

    @SerializedName("wishlist")
    private String wishlist;


    @SerializedName("group_id")
    private String groupId;

    @SerializedName("detail_class_link")
    private String classWebUrl;

    @SerializedName("featured_status")
    private String featuredStatus;

    @SerializedName(value = "class_provided")
    private String teacher;

    @SerializedName("class_provider_id")
    private String teacherId;

    @SerializedName("class_provider_pic")
    private String teacherPic;

    @SerializedName("category")
    private String category;

    @SerializedName("category_id")
    private String categoryId;

    @SerializedName("segment")
    private String segment;

    @SerializedName("segment_id")
    private String segmentId;

    @SerializedName("is_couple_class")
    private int isCoupleClass;

    @SerializedName("class_type_data")
    private String classTypeData;

    public boolean getIsCode() {
        return (isCode == 1);
    }

    @SerializedName("is_cod_avaiable")
    private int isCode;

    @SerializedName("session_date")
    private String sessionDate;

    @SerializedName("sesssion_time")
    private String sessionTime;

    @SerializedName("class_type")
    private String classType;

    @SerializedName("no_of_seats")
    private String noOfSeats;

    @SerializedName("no_of_session")
    private String noOfSession;

    @SerializedName("class_date")
    private Boolean classDate;

    @SerializedName("class_start_time")
    private String classStartTime;

    @SerializedName("class_duration")
    private String classDuration;

    @SerializedName("class_topic")
    private String classTopic;

    @SerializedName("class_summary")
    private String classSummary;

    @SerializedName("class_ratting")
    private Integer rating;

    @SerializedName("photo")
    private String image;

    @SerializedName("pic_name")
    private String picName;

    @SerializedName("video")
    private String videoId;

    @SerializedName("level_id")
    private String levelId;

    @SerializedName("level_name")
    private String levelName;

    @SerializedName("Description")
    private String description;

    @SerializedName("about_academy")
    private String aboutAcademy;

    @SerializedName("expert_level_id")
    private String expertLevelId;

    @SerializedName("price")
    private String price;

    @SerializedName("price_type")
    private String pricingType;

    @SerializedName("location")
    private List<ClassLocationData> location;

    @SerializedName("locality")
    private String locality;

    @SerializedName("price_symbol")
    private String priceSymbol = "&#8377;";

    @SerializedName("price_code")
    private String priceCode;

    @SerializedName("vendorClasseLevelDetail")
    private List<ClassLevelData> levelDetails;

    @SerializedName("catalog_description")
    private String catalogDescription;

    @SerializedName("class_provided_by")
    private String classProvider;

    @SerializedName("localities")
    private List<String> catalogLocations;


    public List<ClassLevelData> getLevelDetails() {
        return levelDetails;
    }

    public void setLevelDetails(List<ClassLevelData> levelDetails) {
        this.levelDetails = levelDetails;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherPic() {
        return teacherPic;
    }

    public void setTeacherPic(String teacherPic) {
        this.teacherPic = teacherPic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeaturedStatus() {
        return featuredStatus;
    }

    public void setFeaturedStatus(String featuredStatus) {
        this.featuredStatus = featuredStatus;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getClassTypeData() {
        return classTypeData;
    }

    public void setClassTypeData(String classTypeData) {
        this.classTypeData = classTypeData;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getNoOfSeats() {
        return noOfSeats;
    }

    public void setNoOfSeats(String noOfSeats) {
        this.noOfSeats = noOfSeats;
    }

    public String getNoOfSession() {
        return noOfSession;
    }

    public void setNoOfSession(String noOfSession) {
        this.noOfSession = noOfSession;
    }

    public Boolean getClassDate() {
        return classDate;
    }

    public void setClassDate(Boolean classDate) {
        this.classDate = classDate;
    }

    public String getClassStartTime() {
        return classStartTime;
    }

    public void setClassStartTime(String classStartTime) {
        this.classStartTime = classStartTime;
    }

    public String getClassDuration() {
        return classDuration;
    }

    public void setClassDuration(String classDuration) {
        this.classDuration = classDuration;
    }

    public String getClassTopic() {
        return classTopic;
    }

    public void setClassTopic(String classTopic) {
        this.classTopic = classTopic;
    }

    public String getClassSummary() {
        return classSummary;
    }

    public void setClassSummary(String classSummary) {
        this.classSummary = classSummary;
    }

    public String getRating() {
        if (rating == null)
            rating = 4;
        return rating.toString();
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpertLevelId() {
        return expertLevelId;
    }

    public void setExpertLevelId(String expertLevelId) {
        this.expertLevelId = expertLevelId;
    }

    public List<ClassLocationData> getLocation() {
        return location;
    }

    public void setLocation(List<ClassLocationData> location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getPricingType() {
        return pricingType;
    }

    public void setPricingType(String pricingType) {
        this.pricingType = pricingType;
    }


    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getWishlist() {
        return wishlist;
    }

    public void setWishlist(String wishlist) {
        this.wishlist = wishlist;
    }

    public String getClassWebUrl() {
        return classWebUrl;
    }

    public void setClassWebUrl(String classWebUrl) {
        this.classWebUrl = classWebUrl;
    }

    public String getCatalogDescription() {
        return catalogDescription;
    }

    public void setCatalogDescription(String catalogDescription) {
        this.catalogDescription = catalogDescription;
    }

    public String getClassProvider() {
        return classProvider;
    }

    public void setClassProvider(String classProvider) {
        this.classProvider = classProvider;
    }

    public List<String> getCatalogLocations() {
        return catalogLocations;
    }

    public void setCatalogLocations(List<String> catalogLocations) {
        this.catalogLocations = catalogLocations;
    }

    public Spanned getPriceSymbol() {
        return CommonUtils.fromHtml(getPriceSymbolNonSpanned());
    }

    public String getPriceSymbolNonSpanned() {
        return TextUtils.isEmpty(priceSymbol) ? "&#8377;" : priceSymbol;
    }
}

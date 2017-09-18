package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by agrahari on 26/03/17.
 */

public class ClassLocationData implements Serializable {

    @SerializedName("locality_id")
    private String localityId;

    @SerializedName("locality")
    private String locality;

    @SerializedName("location_area")
    private String locationArea;

    @SerializedName("latitude")
    private String latitude;

    @SerializedName("longitude")
    private String longitude;

    @SerializedName("class_topic")
    private String classTopic;

    @SerializedName("class_id")
    private String classId;

    @SerializedName("color_code")
    private String colorCode;


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

    public String getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(String locationArea) {
        this.locationArea = locationArea;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getClassTopic() {
        return classTopic;
    }

    public void setClassTopic(String classTopic) {
        this.classTopic = classTopic;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}

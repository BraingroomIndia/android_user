package com.braingroom.user.model.dto;

import com.braingroom.user.model.request.GeneralFilterReq;

import java.io.Serializable;

/**
 * Created by agrahari on 07/04/17.
 */
public class FilterData implements Serializable {

    String keywords = "";
    String startDate = "";
    String endDate = "";
    String categoryId = "";
    String segmentId = "";
    String classType = "";
    String communityId = "";
    String classSchedule = "";
    String classProvider = "";
    String locationId = "";
    String city = "";


    String sortOrder = "";

    public void setFilterData(FilterData data) {
        setKeywords(data.getKeywords());
        setStartDate(data.getStartDate());
        setEndDate(data.getEndDate());
        setCategoryId(data.getCategoryId());
        setSegmentId(data.getSegmentId());
        setClassType(data.getClassType());
        setCommunityId(data.getCommunityId());
        setClassSchedule(data.getClassSchedule());
        setClassProvider(data.getClassProvider());
        setLocationId(data.getLocationId());
        setCity(data.getCity());
        if (sortOrder.equals(""))
            setSortOrder(data.getSortOrder());
    }

    public void setSortData(String sortOrder) {
        setSortOrder(sortOrder);
    }

    public GeneralFilterReq getFilterReq() {
        return new GeneralFilterReq(new GeneralFilterReq.Snippet("", keywords, startDate, endDate, categoryId, segmentId, classType, communityId, classSchedule, classProvider, locationId, sortOrder,""));
    }


    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        if (keywords == null) keywords = "";
        this.keywords = keywords;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        if (startDate == null) startDate = "";
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        if (endDate == null) endDate = "";
        this.endDate = endDate;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        if (categoryId == null) categoryId = "";
        this.categoryId = categoryId;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        if (segmentId == null) segmentId = "";
        this.segmentId = segmentId;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        if (classType == null) classType = "";
        this.classType = classType;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        if (communityId == null) communityId = "";
        this.communityId = communityId;
    }

    public String getClassSchedule() {
        return classSchedule;
    }

    public void setClassSchedule(String classSchedule) {
        if (classSchedule == null) classSchedule = "";
        this.classSchedule = classSchedule;
    }

    public String getClassProvider() {
        return classProvider;
    }

    public void setClassProvider(String classProvider) {
        if (classProvider == null) classProvider = "";
        this.classProvider = classProvider;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        if (locationId == null) locationId = "";
        this.locationId = locationId;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null) city = "";
        this.city = city;
    }
}

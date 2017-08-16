package com.braingroom.user.model.dto;

import com.braingroom.user.model.request.ConnectFeedReq;

import java.io.Serializable;

/**
 * Created by agrahari on 07/04/17.
 */
public class ConnectFilterData implements Serializable {

    public String categId;
    public String segId;
    public String groupId;
    public int isMyGroup;
    public String majorCateg;
    public String minorCateg;
    public String countryId;
    public String stateId;
    public String cityId;
    public String localityId;
    public String searchQuery;
    public String instituteId;
    public String authorId;

    public ConnectFilterData() {
        this.categId = "";
        this.segId = "";
        this.groupId = "";
        this.isMyGroup = 0;
        this.majorCateg = "";
        this.minorCateg = "";
        this.countryId = "";
        this.stateId = "";
        this.cityId = "";
        this.localityId = "";
        this.searchQuery = "";
        this.instituteId = "";
        this.authorId = "";
    }

    public void setFilterData(ConnectFilterData data) {
        setCategId(data.getCategId());
        setSegId(data.getSegId());
        setGroupId(data.getGroupId());
        setIsMyGroup(data.getIsMyGroup());
        setMajorCateg(data.getMajorCateg());
        setMinorCateg(data.getMinorCateg());
        setCountryId(data.getCountryId());
        setStateId(data.getStateId());
        setCityId(data.getCityId());
        setLocalityId(data.getLocalityId());
        setSearchQuery(data.getSearchQuery());
    }


    public ConnectFeedReq getFilterReq() {
        ConnectFeedReq.Snippet snippet = new ConnectFeedReq.Snippet();
        snippet.setCategId(categId);
        snippet.setSegId(segId);
        snippet.setGroupId(groupId);
        snippet.setIsMyGroup(isMyGroup);
        snippet.setMajorCateg(majorCateg);
        snippet.setMinorCateg(minorCateg);
        snippet.setCountryId(countryId);
        snippet.setStateId(stateId);
        snippet.setCityId(cityId);
        snippet.setLocalityId(localityId);
        snippet.setSearchQuery(searchQuery);
        snippet.setInstituteId(instituteId);
        snippet.setAuthorId(authorId);
        return new ConnectFeedReq(snippet);
    }

    public boolean isEqual(ConnectFilterData filterData) {
        try {
            return this.majorCateg.equals(filterData.majorCateg) &&
                    this.minorCateg.equals(filterData.minorCateg);

        } catch (Exception e) {
            return false;
        }

    }

    public void setInstituteId(String instituteId) {
        this.instituteId = instituteId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getCategId() {
        return categId;
    }

    public void setCategId(String categId) {
        this.categId = categId == null ? "" : categId;
    }

    public String getSegId() {
        return segId;
    }

    public void setSegId(String segId) {
        this.segId = segId == null ? "" : segId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? "" : groupId;
    }

    public int getIsMyGroup() {
        return isMyGroup;
    }

    public void setIsMyGroup(int isMyGroup) {
        this.isMyGroup = isMyGroup;
    }

    public String getMajorCateg() {
        return majorCateg;
    }

    public void setMajorCateg(String majorCateg) {
        this.majorCateg = majorCateg == null ? "" : majorCateg;
    }

    public String getMinorCateg() {
        return minorCateg;
    }

    public void setMinorCateg(String minorCateg) {
        this.minorCateg = minorCateg == null ? "" : minorCateg;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId == null ? "" : countryId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId == null ? "" : stateId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId == null ? "" : cityId;
    }

    public String getLocalityId() {
        return localityId;
    }

    public void setLocalityId(String localityId) {
        this.localityId = localityId == null ? "" : localityId;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery == null ? "" : searchQuery;
    }

}

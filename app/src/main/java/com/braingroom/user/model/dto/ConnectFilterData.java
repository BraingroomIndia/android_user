package com.braingroom.user.model.dto;

import com.braingroom.user.model.request.ConnectFeedReq;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by agrahari on 07/04/17.
 */
public class ConnectFilterData implements Serializable {

    @SerializedName("categ_id")
    public String categId;
    @SerializedName("seg_id")
    public String segId;
    @SerializedName("group_id")
    public String groupId;
    @SerializedName("is_my_group")
    public int isMyGroup;
    @SerializedName("major_categ")
    public String majorCateg;
    @SerializedName("minor_categ")
    public String minorCateg;
    @SerializedName("country_id")
    public String countryId;
    @SerializedName("state_id")
    public String stateId;
    @SerializedName("city_id")
    public String cityId;
    @SerializedName("locality_id")
    public String localityId;
    @SerializedName("search_query")
    public String searchQuery;
    @SerializedName("institute_id")
    public String instituteId;
    @SerializedName("author_id")
    public String authorId;
    @SerializedName("best_posts")
    private boolean featuredPost;

    public void setFeaturedPost(boolean featuredPost) {
        this.featuredPost = featuredPost;
    }

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

        this.featuredPost = false;
    }

    public String getInstituteId() {
        return instituteId;
    }

    public String getAuthorId() {
        return authorId;
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
        snippet.setFeaturedPost(this.featuredPost ? "1" : "0");
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

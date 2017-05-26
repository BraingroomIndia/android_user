package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ConnectFeedReq {

    @SerializedName("braingroom")
    Snippet data;

    public static class Snippet {

        @SerializedName("user_id")
        public String userId;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId == null ? "" : userId;
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
}

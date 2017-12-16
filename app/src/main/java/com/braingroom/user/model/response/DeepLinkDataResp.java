package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

/*
 * Created by godara on 15/12/17.
 */

public class DeepLinkDataResp {

    @SerializedName("type")
    public String type;
    @SerializedName("braingroom")

    public Snippet data;

    public static class Snippet {

        @SerializedName("search_key")
        public String searchKey;

        @SerializedName("class_type")
        public String classType;

        @SerializedName("city_id")
        public String cityId;

        @SerializedName("location_id")
        public String locationId;

        @SerializedName("catlog")
        public String catlog;

        @SerializedName("class_provider")
        public String classProvider;

        @SerializedName("class_schedule")
        public String classSchedule;

        @SerializedName("community_id")
        public String communityId;

        @SerializedName("end_date")
        public String endDate;

        @SerializedName("gift_id")
        public String giftId;

        @SerializedName("price_sort_status")
        public String priceSortStatus;

        @SerializedName("sort_by_latest")
        public String sortByLatest;

        @SerializedName("start_date")
        public String startDate;

        @SerializedName("search_cat_id")
        public String searchCatId;

        @SerializedName("search_seg_id")
        public String searchSegId;

        @SerializedName("id")
        public String id;

        @SerializedName("is_Catalogue")
        public String isCatalogue;

        @SerializedName("user_id")
        public String userId;

        @SerializedName("author_id")
        public String authorId;

        @SerializedName("categ_id")
        public String categId;

        @SerializedName("country_id")
        public String countryId;

        @SerializedName("best_posts")
        public String bestPosts;

        @SerializedName("group_id")
        public String groupId;

        @SerializedName("institute_id")
        public String instituteId;

        @SerializedName("is_my_group")
        public String isMyGroup;

        @SerializedName("locality_id")
        public String localityId;

        @SerializedName("major_categ")
        public String majorCateg;

        @SerializedName("minor_categ")
        public String minorCateg;

        @SerializedName("search_query")
        public String searchQuery;

        @SerializedName("seg_id")
        public String segId;

        @SerializedName("state_id")
        public String stateId;

        @SerializedName("post_id")
        public String postId;

    }
}

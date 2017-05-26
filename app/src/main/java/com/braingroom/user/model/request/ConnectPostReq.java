package com.braingroom.user.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 * Case 1 : For Article & Videos
 * ------------------------
 * {
 * "braingroom" : {
 * "uuid" : "",
 * "category_id" : "",
 * "segment_id" : "",
 * "post_title" : "",
 * "post_summary" : "",
 * "country_id" : "",
 * "state_id" : "",
 * "city_id" : "",
 * "locality_id" : "",
 * "post_thumb_upload" : "",
 * }
 * }
 * <p>
 * Case 2 : For Decide & Discuss
 * -------------------------
 * {
 * "braingroom" : {
 * "uuid" : "",
 * "category_id" : "",
 * "segment_id" : "",
 * "post_title" : "",
 * "post_summary" : "",
 * "country_id" : "",
 * "state_id" : "",
 * "city_id" : "",
 * "locality_id" : "",
 * "post_thumb_upload" : "",
 * }
 * }
 * <p>
 * Case 3 : For Knowledge Nuggets
 * -----------------------------
 * {
 * "braingroom" : {
 * "uuid" : "",
 * "post_type" : "",
 * "post_title" : "",
 * "post_summary" : "",
 * "country_id" : "",
 * "state_id" : "",
 * "city_id" : "",
 * "locality_id" : "",
 * "post_thumb_upload" : "",
 * "videouploadway" : "", // 1 -> Youtube link, 2 -> Manual Upload
 * "video" : "", // if videouploadway -> 1
 * "video_upload" : "", // if videouploadway -> 2
 * "class_link" : ""
 * }
 * }
 * <p>
 * <p>
 * Case 4 : For Buy & Sell
 * ----------------
 * {
 * "braingroom" : {
 * "uuid" : "",
 * "post_type" : "",
 * "group_id" : "",
 * "post_title" : "",
 * "post_summary" : "",
 * "country_id" : "",
 * "state_id" : "",
 * "city_id" : "",
 * "locality_id" : "",
 * "post_thumb_upload" : ""
 * }
 * }
 * <p>
 * Case 5 : For Learning Partner
 * -------------------------
 * {
 * "braingroom" : {
 * "uuid" : "",
 * "post_type" : "",
 * "group_id" : "",
 * "post_title" : "",
 * "activity_type" : "",
 * "post_summary" : "",
 * "country_id" : "",
 * "state_id" : "",
 * "city_id" : "",
 * "locality_id" : "",
 * "proposed_location" : "",
 * "proposed_date_type" : "", // proposed_date_type -> 1/2
 * "request_date" : "", // if proposed_date_type -> 1
 * "proposed_date_from" : "", // if proposed_date_type -> 2
 * "proposed_date_to" : "", // if proposed_date_type -> 2
 * "request_time" : "",
 * "request_note" : "",
 * "privacy_type" : ""
 * }
 * }
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ConnectPostReq {

    public static class ArticleAndVideos {

        @SerializedName("braingroom")
        ArticleAndVideosSnippet data;

        public static class ArticleAndVideosSnippet {

            @SerializedName("uuid")
            @Expose
            public String uuid;
            @SerializedName("category_id")
            @Expose
            public String categoryId;
            @SerializedName("segment_id")
            @Expose
            public String segmentId;
            @SerializedName("post_title")
            @Expose
            public String postTitle;
            @SerializedName("post_summary")
            @Expose
            public String postSummary;
            @SerializedName("country_id")
            @Expose
            public String countryId;
            @SerializedName("state_id")
            @Expose
            public String stateId;
            @SerializedName("city_id")
            @Expose
            public String cityId;
            @SerializedName("locality_id")
            @Expose
            public String localityId;
            @SerializedName("post_thumb_upload")
            @Expose
            public String postThumbUpload;
        }

    }

    public static class DecideAndDiscuss {

        @SerializedName("braingroom")
        DecideAndDiscussSnippet data;

        public static class DecideAndDiscussSnippet {

            @SerializedName("uuid")
            @Expose
            public String uuid;
            @SerializedName("category_id")
            @Expose
            public String categoryId;
            @SerializedName("segment_id")
            @Expose
            public String segmentId;
            @SerializedName("post_title")
            @Expose
            public String postTitle;
            @SerializedName("post_summary")
            @Expose
            public String postSummary;
            @SerializedName("country_id")
            @Expose
            public String countryId;
            @SerializedName("state_id")
            @Expose
            public String stateId;
            @SerializedName("city_id")
            @Expose
            public String cityId;
            @SerializedName("locality_id")
            @Expose
            public String localityId;
            @SerializedName("post_thumb_upload")
            @Expose
            public String postThumbUpload;
        }
    }

    public static class KnowledgeNuggets {

        @SerializedName("braingroom")
        KnowledgeNuggetsSnippet data;

        public static class KnowledgeNuggetsSnippet {

            @SerializedName("uuid")
            @Expose
            public String uuid;
            @SerializedName("post_type")
            @Expose
            public String postType;
            @SerializedName("post_title")
            @Expose
            public String postTitle;
            @SerializedName("post_summary")
            @Expose
            public String postSummary;
            @SerializedName("country_id")
            @Expose
            public String countryId;
            @SerializedName("state_id")
            @Expose
            public String stateId;
            @SerializedName("city_id")
            @Expose
            public String cityId;
            @SerializedName("locality_id")
            @Expose
            public String localityId;
            @SerializedName("post_thumb_upload")
            @Expose
            public String postThumbUpload;
            @SerializedName("videouploadway")
            @Expose
            public String videouploadway;
            @SerializedName("video")
            @Expose
            public String video;
            @SerializedName("video_upload")
            @Expose
            public String videoUpload;
            @SerializedName("class_link")
            @Expose
            public String classLink;
        }

    }

    public static class BuyAndSell {

        @SerializedName("braingroom")
        BuyAndSellSnippet data;

        public static class BuyAndSellSnippet {

            @SerializedName("uuid")
            @Expose
            public String uuid;
            @SerializedName("post_type")
            @Expose
            public String postType;
            @SerializedName("group_id")
            @Expose
            public String groupId;
            @SerializedName("post_title")
            @Expose
            public String postTitle;
            @SerializedName("post_summary")
            @Expose
            public String postSummary;
            @SerializedName("country_id")
            @Expose
            public String countryId;
            @SerializedName("state_id")
            @Expose
            public String stateId;
            @SerializedName("city_id")
            @Expose
            public String cityId;
            @SerializedName("locality_id")
            @Expose
            public String localityId;
            @SerializedName("post_thumb_upload")
            @Expose
            public String postThumbUpload;
        }

    }

    public static class LearningPartner {

        @SerializedName("braingroom")
        LearningPartnerSnippet data;

        public static class LearningPartnerSnippet {

            @SerializedName("uuid")
            @Expose
            public String uuid;
            @SerializedName("post_type")
            @Expose
            public String postType;
            @SerializedName("group_id")
            @Expose
            public String groupId;
            @SerializedName("post_title")
            @Expose
            public String postTitle;
            @SerializedName("activity_type")
            @Expose
            public String activityType;
            @SerializedName("post_summary")
            @Expose
            public String postSummary;
            @SerializedName("country_id")
            @Expose
            public String countryId;
            @SerializedName("state_id")
            @Expose
            public String stateId;
            @SerializedName("city_id")
            @Expose
            public String cityId;
            @SerializedName("locality_id")
            @Expose
            public String localityId;
            @SerializedName("proposed_location")
            @Expose
            public String proposedLocation;
            @SerializedName("proposed_date_type")
            @Expose
            public String proposedDateType;
            @SerializedName("request_date")
            @Expose
            public String requestDate;
            @SerializedName("proposed_date_from")
            @Expose
            public String proposedDateFrom;
            @SerializedName("proposed_date_to")
            @Expose
            public String proposedDateTo;
            @SerializedName("request_time")
            @Expose
            public String requestTime;
            @SerializedName("request_note")
            @Expose
            public String requestNote;
            @SerializedName("privacy_type")
            @Expose
            public String privacyType;
        }

    }

}

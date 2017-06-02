package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * Created by godara on 02/06/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class LearningPartnerPostReq {

    @SerializedName("braingroom")
    Snippet data;

    @Setter
    public static class Snippet {

        @SerializedName("uuid")
        public String uuid;

        @SerializedName("post_type")
        public String postType;

        @SerializedName("group_id")
        public String groupId;

        @SerializedName("post_title")
        public String postTitle;

        @SerializedName("activity_type")
        public String activityType;

        @SerializedName("post_summary")
        public String postSummary;

        @SerializedName("country_id")
        public String countryId;

        @SerializedName("state_id")
        public String stateId;

        @SerializedName("city_id")
        public String cityId;

        @SerializedName("locality_id")
        public String localityId;

        @SerializedName("proposed_location")
        public String proposedLocation;

        @SerializedName("proposed_date_type")
        public String proposedDateType;

        @SerializedName("request_date")
        public String requestDate;

        @SerializedName("proposed_date_from")
        public String proposedDateFrom;

        @SerializedName("proposed_date_to")
        public String proposedDateTo;

        @SerializedName("request_time")
        public String requestTime;

        @SerializedName("request_note")
        public String requestNote;

        @SerializedName("privacy_type")
        public String privacyType;
    }

}

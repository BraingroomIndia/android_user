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
public class KnowledgeNuggetsReq {

    @SerializedName("braingroom")
    Snippet data;

    @Setter
    public static class Snippet {

        @SerializedName("uuid")
        public String uuid;

        @SerializedName("post_type")
        public String postType;

        @SerializedName("post_title")
        public String postTitle;

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

        @SerializedName("post_thumb_upload")
        public String postThumbUpload;

        @SerializedName("videouploadway")
        public String videouploadway;

        @SerializedName("video")
        public String video;

        @SerializedName("video_upload")
        public String videoUpload;

        @SerializedName("class_link")
        public String classLink;
    }

}

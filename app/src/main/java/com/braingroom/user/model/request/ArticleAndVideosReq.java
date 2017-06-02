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
public class ArticleAndVideosReq {

    @SerializedName("braingroom")
    Snippet data;

    @Setter
    public static class Snippet {

        @SerializedName("uuid")
        public String uuid;

        @SerializedName("category_id")
        public String categoryId;

        @SerializedName("segment_id")
        public String segmentId;

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
    }

}


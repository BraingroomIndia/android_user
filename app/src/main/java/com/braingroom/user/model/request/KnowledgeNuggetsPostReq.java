package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by godara on 02/06/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class KnowledgeNuggetsPostReq {

    @SerializedName("braingroom")
    Snippet data;

    @Setter
    @Getter
    public static class Snippet {

        @SerializedName("uuid")
        String uuid;

        @SerializedName("post_type")
        String postType = "tips_tricks";

        @SerializedName("group_id")
        String groupId;

        @SerializedName("post_title")
        String postTitle;

        @SerializedName("post_summary")
        String postSummary;

        @SerializedName("country_id")
        String countryId;

        @SerializedName("state_id")
        String stateId;

        @SerializedName("city_id")
        String cityId;

        @SerializedName("locality_id")
        String localityId;

        @SerializedName("post_thumb_upload")
        String postThumbUpload;


        @SerializedName("video")
        String video;

        @SerializedName("youtube_url")
        String youtubeUrl;


        @SerializedName("class_link")
        String classLink;
    }

}

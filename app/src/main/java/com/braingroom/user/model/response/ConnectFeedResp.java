package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ConnectFeedResp extends BaseResp {

    @SerializedName("next_page")
    int nextPage;

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    public static class Snippet {

        @SerializedName("id")
        private String id;

        @SerializedName("vendor_image")
        private String vendorImage;

        @SerializedName("vendor_name")
        private String vendorName;

        @SerializedName("title")
        private String title;

        @SerializedName("detail")
        private String description;

        @SerializedName("segment_id")
        private String segId;

        @SerializedName("segment_name")
        private String segName;

        @SerializedName("num_likes")
        private String numLikes;

        @SerializedName("num_comments")
        private String numComments;

        @SerializedName("num_reports")
        private String numReports;

        @SerializedName("liked")
        private int liked;

        @SerializedName("reported")
        private int reported;

        @SerializedName("accepted")
        private int isAccepted = 0;

        @SerializedName("accept_count")
        private int numAccepted;

        @SerializedName("user_id")
        private String postOwner;

        @SerializedName("post_image")
        private String image;

        @SerializedName("post_video")
        private String video;

        @SerializedName("post_type")
        private String postType;

        @SerializedName("date")
        private String date;
    }
}

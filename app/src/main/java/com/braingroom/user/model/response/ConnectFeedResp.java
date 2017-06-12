package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class ConnectFeedResp extends BaseResp {

    @SerializedName("next_page")
    int nextPage;

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @EqualsAndHashCode(callSuper = false)
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

        @SerializedName("institute_name")
        private String instituteName;

        @SerializedName("category_id")
        private String categoryId;

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

        @SerializedName("share_url")
        private String shareUrl;
    }
}

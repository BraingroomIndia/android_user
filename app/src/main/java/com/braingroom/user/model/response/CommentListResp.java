package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class CommentListResp extends BaseResp {

    @SerializedName("next_page")
    int nextPage;

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    public static class Snippet {

        @SerializedName("id")
        public String id;

        @SerializedName("user_name")
        public String userName;

        @SerializedName("user_image")
        public String userImage;

        @SerializedName("comment")
        public String comment;

        @SerializedName("num_likes")
        public String numLikes;

        @SerializedName("liked")
        public Integer liked;

        @SerializedName("date")
        public String date;

        @SerializedName("replies")
        public List<Reply> replies;

    }

    @Data
    public static class Reply {

        @SerializedName("reply_id")
        public String replyId;

        @SerializedName("user_name")
        public String userName;

        @SerializedName("user_image")
        public String userImage;

        @SerializedName("reply")
        public String reply;

        @SerializedName("reply_date")
        public String replyDate;

        @SerializedName("num_likes")
        public String numLikes;

        @SerializedName("liked")
        public int liked;
    }
}

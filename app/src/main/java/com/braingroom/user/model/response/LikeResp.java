package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class LikeResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("post_id")
        String postId;
        @SerializedName("comment_id")
        String commentId;
        @SerializedName("reply_id")
        String replyId;
        @SerializedName("liked")
        int liked;
    }
}

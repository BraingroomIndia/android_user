package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 22/05/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class CommentViewReplyReq {
    @SerializedName("braingroom")
    Snippet data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {

        @SerializedName("user_id")
        String userId;

        @SerializedName("comment_id")
        String replyId;


    }
}

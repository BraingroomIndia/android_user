package com.braingroom.user.model.request;

import com.braingroom.user.model.response.BaseResp;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by godara on 22/05/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class CommentViewReply extends BaseResp {


    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {
        @SerializedName("user_name")
        public String userName;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("user_image")
        public String userImage;

        @SerializedName("reply")
        public String reply;

        @SerializedName("reply_id")
        public String replyId;
    }
}

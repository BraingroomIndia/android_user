package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by godara on 23/08/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class FollowResp extends BaseResp {
    @Getter
    @SerializedName("braingroom")
    public List<Snippet> data;

    @Getter
    public static class Snippet {
        @SerializedName("following_user_id")
        public String followingUserId;

        @SerializedName("user_id")
        public String userId;
    }
}

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
        @SerializedName("status")
        public int status;
    }
}

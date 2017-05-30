package com.braingroom.user.model.response;

/**
 * Created by godara on 18/05/17.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;


public class ChangePasswordResp extends BaseResp {


    @SerializedName("sync_time")
    String syncTime;
    @Getter
    @SerializedName("braingroom")
    List<Snippet> data;

    public static class Snippet {


    }
}

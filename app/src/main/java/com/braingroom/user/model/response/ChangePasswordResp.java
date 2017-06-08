package com.braingroom.user.model.response;

/**
 * Created by godara on 18/05/17.
 */

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;


@EqualsAndHashCode(callSuper = false)
public class ChangePasswordResp extends BaseResp {


    @SerializedName("sync_time")
    String syncTime;
    @Getter
    @SerializedName("braingroom")
    List<Snippet> data;

    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {


    }
}

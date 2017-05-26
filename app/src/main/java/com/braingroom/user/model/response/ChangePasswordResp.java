package com.braingroom.user.model.response;

/**
 * Created by godara on 18/05/17.
 */
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;


public class ChangePasswordResp {
    @Getter
    @SerializedName("braingroom")
    Snippet data;
    public static class Snippet{
        @Getter
        @SerializedName("res_code")
        String resCode;

        @Getter
        @SerializedName("res_msg")
        String resMsg;

        @SerializedName("sync_time")
        String syncTime;
    }
}

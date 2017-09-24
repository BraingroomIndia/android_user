package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by godara on 22/06/17.
 */
@Getter
@AllArgsConstructor(suppressConstructorProperties = true)
public class NotificationCountResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Getter
    public static class Snippet {
        @SerializedName("count")
        int count;
    }
}

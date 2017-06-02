package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by godara on 01/06/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class GroupActivitiesResp extends BaseResp {


    @SerializedName("braingroom")
    List<Snippet> snippet;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {

        @SerializedName("id")
        public String id;

        @SerializedName("activity_name")
        public String activityName;
    }
}

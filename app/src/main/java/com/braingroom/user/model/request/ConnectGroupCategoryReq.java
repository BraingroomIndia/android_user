package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ConnectGroupCategoryReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("group")
        String group;// "all_group" , "my_group"
        @SerializedName("categ_n_segment")
        String categoryNSegment; // "categ", "segment" , "both"
    }
}

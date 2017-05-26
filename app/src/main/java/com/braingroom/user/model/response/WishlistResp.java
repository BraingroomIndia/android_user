package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class WishlistResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("class_id")
        String classId;
        @SerializedName("user_id")
        String userId;
        @SerializedName("status")
        String status;
        @SerializedName("add_date")
        String addDate;
        @SerializedName("modify_date")
        String modfyDate;
    }


}

package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by ashketchup on 8/12/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ReviewAddResp {
    @SerializedName("res_code")
    Integer resCode;
    @SerializedName("res_msg")
    String resMsg;
    @SerializedName("braingroom")
    List<Snippet> braingroom = null;
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet{

        @SerializedName("user_id")
        String userId;

        @SerializedName("review_type")
        String reviewType;

        @SerializedName("vendor_id")
        String vendorId;

        @SerializedName("class_id")
        String classId;

        @SerializedName("review")
        String review;

        @SerializedName("rating")
        String rating;
    }
}


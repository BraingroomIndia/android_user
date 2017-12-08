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
public class ReviewGetResp {
    @SerializedName("res_code")
    Integer resCode;

    @SerializedName("res_msg")
    String resMsg;

    @SerializedName("braingroom")
    List<Snippet> braingroom = null;
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet{
        @SerializedName("id")
        String id;

        @SerializedName("user_id")
        String userId;

        @SerializedName("first_name")
        String firstName;

        @SerializedName("user_type_id")
        private String userTypeId;

        @SerializedName("timestamp")
        String timeStamp;

        @SerializedName("review_type")
        String reviewType;

        @SerializedName("review_message")
        String reviewMessage;

        @SerializedName("rating")
        String rating;

        @SerializedName("class_id")
        String classId;
    }
}

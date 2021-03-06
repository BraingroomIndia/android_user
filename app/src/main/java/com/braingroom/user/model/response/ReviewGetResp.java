package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

/**
 * Created by ashketchup on 8/12/17.
 */
public class ReviewGetResp {
    @SerializedName("res_code")
    private Integer resCode;

    @Getter
    @SerializedName("res_msg")
    String resMsg;

    @Getter
    @SerializedName("braingroom")
    List<Snippet> data = null;

    public boolean getResCode() {
        return data != null && !data.isEmpty() && data.get(0) != null;
    }

    @Getter
    public static class Snippet {
        @SerializedName("id")
        private String id;

        @SerializedName("user_id")
        private String userId;

        @SerializedName("first_name")
        private String firstName;

        @SerializedName("user_type_id")
        private String userTypeId;

        @SerializedName("add_date")
        private String timeStamp;

        @SerializedName("review_type")
        private String reviewType;

        @SerializedName("review_message")
        private String reviewMessage;

        @SerializedName("rating")
        private Integer rating;

        @SerializedName("class_id")
        private String classId;

        @SerializedName("class_topic")
        private String classTopic;

        public String getClassTopic() {
            return this.classTopic == null ? "Vendor Review" : (this.classTopic.trim().isEmpty() ? "Vendor Review" : this.classTopic);
        }
    }

    public ReviewGetResp() {
        this.data = null;
        this.resMsg = "";
    }
}

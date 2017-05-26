package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class VendorReviewResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {

        @SerializedName("id")
        public String id;

        @SerializedName("rating")
        public String rating;

        @SerializedName("review")
        public String review;

        @SerializedName("reviewer_name")
        public String reviewerName;

        @SerializedName("class_id")
        public String classId;

        @SerializedName("vendor_id")
        public String vendorId;

        @SerializedName("user_id")
        public String userId;

        @SerializedName("status")
        public String status;

        @SerializedName("add_date")
        public String addDate;
    }
}

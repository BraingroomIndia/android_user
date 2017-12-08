package com.braingroom.user.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by ashketchup on 8/12/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ReviewAddReq {
    @SerializedName("braingroom")
    @Expose
    Snippet braingroom;
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
        public Snippet(String userId , int reviewtype , String vendorOrClass,String review,String rating){
            if(reviewtype==2){
                vendorId=vendorOrClass;
                classId="";
                reviewType="2";
            }
            if(reviewtype==3){
                classId=vendorOrClass;
                vendorId="";
                reviewType="3";
            }
            this.userId=userId;
            this.review=review;
            this.rating=rating;
        }
    }
}

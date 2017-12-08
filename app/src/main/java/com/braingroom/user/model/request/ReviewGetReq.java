package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by ashketchup on 8/12/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ReviewGetReq {
    @SerializedName("braingroom")
    Snippet braingroom;
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet{
        @SerializedName("user_id")
        String userId;
        @SerializedName("vendor_id")
        String vendorId;
        @SerializedName("class_id")
        String classId;
        public Snippet(String userId,int reviewType,String id){
            if(reviewType==2){
                vendorId=id;
                classId="";
            }
            if(reviewType==3){
                classId=id;
                vendorId="";
            }
            this.userId=userId;
        }
    }
}

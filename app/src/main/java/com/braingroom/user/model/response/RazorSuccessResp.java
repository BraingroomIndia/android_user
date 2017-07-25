package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by himan on 1/24/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class RazorSuccessResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("txnid")
        public String txnid;

        @SerializedName("booking_id")
        public String bookingId;

        @SerializedName("class_name")
        public String className;

        @SerializedName("email")
        public String email;

        @SerializedName("user_name")
        String userName;

        @SerializedName("total_amount")
        String totalAmount;
    }

}

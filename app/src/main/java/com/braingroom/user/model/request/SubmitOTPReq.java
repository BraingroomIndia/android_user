package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * Created by godara on 01/07/17.
 */

@AllArgsConstructor(suppressConstructorProperties = true)
public class SubmitOTPReq {
    @SerializedName("braingroom")
    Snippet data;

    @Setter
    public static class Snippet {

        @SerializedName("mobile_no")
        String mobileNo = "";

        @SerializedName("otp")
        String OTP = "";
    }

}

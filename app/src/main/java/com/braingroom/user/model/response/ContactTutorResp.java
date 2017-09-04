package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by godara on 31/08/17.
 */
@Getter
@AllArgsConstructor(suppressConstructorProperties = true)
public class ContactTutorResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Getter
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {


        @SerializedName("status")
        String displayText;

        @SerializedName("mobile")
        String mobileNumber;
    }
}

package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * Created by godara on 06/06/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ContactAdmin {

    @SerializedName("braingroom")
    Snippet data;

    @Setter
    public static class Snippet {

        @SerializedName("name")
        String name;

        @SerializedName("mobile")
        String mobile;

        @SerializedName("email")
        String email;

        @SerializedName("message")
        String message;

        @SerializedName("datetime")
        String datetime;
    }
}

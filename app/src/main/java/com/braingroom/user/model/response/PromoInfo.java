package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by godara on 27/10/17.
 */

public class PromoInfo extends BaseResp {


    @SerializedName("braingroom")

    public Snippet data;


    public static class Snippet {

        @SerializedName("title")
        public String title;
        @SerializedName("content")
        public String content;

    }

}

package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by godara on 24/08/17.
 */
@Getter
public class ConnectSectionResp extends BaseResp {

    @SerializedName("braingroom")
    public List<Snippet> data;

    @Getter
    public static class Snippet {

        @SerializedName("name")
        public String name;

        @SerializedName("img_url")
        public String imgUrl;

        @SerializedName("major_category")
        public String majorCategory;

        @SerializedName("minor_category")
        public String minorCategory;
    }
}

package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class LikedUsersListResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    public static class Snippet {

        @SerializedName("user_name")
        public String userName;

        @SerializedName("user_image")
        public String userImage;

    }
}

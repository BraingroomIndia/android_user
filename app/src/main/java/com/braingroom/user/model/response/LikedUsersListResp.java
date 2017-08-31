package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class LikedUsersListResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName(value = "user_id", alternate = {"id"})
        private String userId;

        @SerializedName(value = "user_name", alternate = {"name"})
        public String userName;

        @SerializedName(value = "user_image", alternate = {"photo"})
        public String userImage;

        @SerializedName("follow_status")
        public int followStatus;

    }
}

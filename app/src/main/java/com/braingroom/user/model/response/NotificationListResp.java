package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class NotificationListResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    public static class Snippet {

        @SerializedName("notification_id")
        public String notificationId;

        @SerializedName("post_id")
        public String postId;

        @SerializedName("description")
        public String description;

        @SerializedName("status")
        public String status;

    }

}

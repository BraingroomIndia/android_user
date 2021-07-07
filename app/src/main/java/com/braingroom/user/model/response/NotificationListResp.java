package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class NotificationListResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("notification_id")
        public String notificationId;

        @SerializedName("post_id")
        public String postId;

        @SerializedName("class_id")
        public String classId;

        @SerializedName("description")
        public String description;

        @SerializedName("status")
        public String status;

    }

}

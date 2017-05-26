package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ChatListResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    public static class Snippet {

        @SerializedName("user_id")
        public String userId;

        @SerializedName("text")
        public String text;

        @SerializedName("time")
        public String time;

    }

}

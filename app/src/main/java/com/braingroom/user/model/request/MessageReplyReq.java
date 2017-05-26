package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/24/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class MessageReplyReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {
        @SerializedName("sender_id")
        public String senderId;

        @SerializedName("reciever_id")
        public String recieverId;

        @SerializedName("quote_id")
        public String quoteId;

        @SerializedName("message_type")
        public String messageType;

        @SerializedName("message")
        public String message;

        @SerializedName("status")
        public String status;

    }

}

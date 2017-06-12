package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class MessageListResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("sender_id")
        public String senderId;

        @SerializedName("username")
        public String senderName;

        @SerializedName("reciever_id")
        public String recieverId;

        @SerializedName("sender_pic")
        public String senderPic;

        @SerializedName("Message")
        public Message message;

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Message {

        @SerializedName("id")
        public String id;

        @SerializedName("quote_id")
        public String quoteId;

        @SerializedName("message_type")
        public String messageType;

        @SerializedName("message")
        public String message;

        @SerializedName("status")
        public String status;

        @SerializedName("add_date")
        public String addDate;

        @SerializedName("modify_date")
        public String modifyDate;
    }
}

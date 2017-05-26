package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by agrahari on 26/03/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ReplyData implements Serializable {

    @SerializedName("reply_id")
    public String replyId;

    @SerializedName("user_name")
    public String userName;

    @SerializedName("user_image")
    public String userImage;

    @SerializedName("reply")
    public String reply;

    @SerializedName("reply_date")
    public String replyDate;

    @SerializedName("num_likes")
    public String numLikes = "0";

    @SerializedName("liked")
    public int liked;
}

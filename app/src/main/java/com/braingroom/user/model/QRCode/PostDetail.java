package com.braingroom.user.model.QRCode;

import com.braingroom.user.model.request.ConnectPostByIdReq.Snippet;
import com.google.gson.annotations.SerializedName;

/**
 * Created by godara on 17/10/17.
 */

public class PostDetail {

    public String type;

    @SerializedName("braingroom")
    public Snippet reqData;
}

package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class BaseResp {

    @SerializedName(value = "res_code", alternate = {"res_id"})
    String resCode;

    @SerializedName("res_msg")
    String resMsg;

    public BaseResp() {
    }

    public BaseResp(String resCode, String resMsg) {
        this.resCode = resCode;
        this.resMsg = resMsg;
    }

    public String getResCode() {
        return resCode;
    }

}
 
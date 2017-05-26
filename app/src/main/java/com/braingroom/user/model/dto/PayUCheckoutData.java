package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

/**
 * Created by agrahari on 26/03/17.
 */
@Data
public class PayUCheckoutData implements Serializable {

    @SerializedName("key")
    public String key;

    @SerializedName("txnid")
    public String txnId;

    @SerializedName("amount")
    public String amount;

    @SerializedName("firstname")
    public String firstname;

    @SerializedName("email")
    public String email;

    @SerializedName("phone")
    public String phone;

    @SerializedName("productinfo")
    public String productinfo;

    @SerializedName("surl")
    public String surl;

    @SerializedName("furl")
    public String furl;

    @SerializedName("service_provider")
    public String serviceProvider;

    @SerializedName("class_id")
    public String classId;

    @SerializedName("user_type_id")
    public String userTypeId;

    @SerializedName("udf1")
    public String udf1;

    @SerializedName("udf2")
    public String udf2;

    @SerializedName("udf3")
    public String udf3;

    @SerializedName("udf4")
    public String udf4;

    private String paymentHash;
    private String vasMobileSdkHash;
    private String paymentMobileSdkHash;
    private String userCardHash;
}

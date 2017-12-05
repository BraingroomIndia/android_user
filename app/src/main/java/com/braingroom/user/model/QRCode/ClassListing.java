package com.braingroom.user.model.QRCode;

import com.braingroom.user.model.request.GeneralFilterReq;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by godara on 17/10/17.
 */

public class ClassListing implements Serializable {

    @SerializedName("type")
    public String type;

    @SerializedName("promo_code")
    public String promoCode;

    @SerializedName("is_incentive")
    public String isIncentive;

    @SerializedName("braingroom")
    public GeneralFilterReq.Snippet reqData;

}

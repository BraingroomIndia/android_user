package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by android on 03/04/18.
 */

public class SessionLevelData implements Serializable {
    @SerializedName("session_name")
    private  String sessionName;
    @SerializedName("session_desc")
    private  String sessionDescription;
    @SerializedName("session_start")
    private  String sessionStart;
    @SerializedName("price")
    private  String price;
    @SerializedName("offer_price")
    private  String offerPrice;
    @SerializedName("min_persion_allowed")
    private  String minPersionAllowed;

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public void setSessionDescription(String sessionDescription) {
        this.sessionDescription = sessionDescription;
    }

    public String getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(String sessionStart) {
        this.sessionStart = sessionStart;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getMinPersionAllowed() {
        return minPersionAllowed;
    }

    public void setMinPersionAllowed(String minPersionAllowed) {
        this.minPersionAllowed = minPersionAllowed;
    }
}

package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by android on 06/04/18.
 */

public class FullSessionData implements Serializable {
    @SerializedName("session_id")
    private String sessionId;
    @SerializedName("session_name")
    private String sessionName;
    @SerializedName("session_desc")
    private String sessionDesc;
    @SerializedName("session_start")
    private String sessionStart;
    @SerializedName("session_end")
    private String sessionEnd;
    @SerializedName("price")
    private String price;
    @SerializedName("offer_price")
    private String offerPrice;
    @SerializedName("min_persion_allowed")
    private String minPersionAllowed;
    @SerializedName("additional_ticket_price")
    private String additionalTicketPrice;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionDesc() {
        return sessionDesc;
    }

    public void setSessionDesc(String sessionDesc) {
        this.sessionDesc = sessionDesc;
    }

    public String getSessionStart() {
        return sessionStart;
    }

    public void setSessionStart(String sessionStart) {
        this.sessionStart = sessionStart;
    }

    public String getSessionEnd() {
        return sessionEnd;
    }

    public void setSessionEnd(String sessionEnd) {
        this.sessionEnd = sessionEnd;
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

    public String getAdditionalTicketPrice() {
        return additionalTicketPrice;
    }

    public void setAdditionalTicketPrice(String additionalTicketPrice) {
        this.additionalTicketPrice = additionalTicketPrice;
    }
}

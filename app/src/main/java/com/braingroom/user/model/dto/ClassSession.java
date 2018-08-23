package com.braingroom.user.model.dto;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ClassSession implements Serializable {


    @SerializedName("session_id")
    private String sessionId;

    @SerializedName("session_name")
    private String sessionName;

    @SerializedName("session_desc")
    private String sessionDesc;

    @SerializedName("session_start")
    private String sessionStart;

    @SerializedName("price")
    private String price;

    @SerializedName("offer_price")
    private String offerPrice;

    @SerializedName("min_person_allowed")
    private String minPersonAllowed;

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

    public String getMinPersonAllowed() {
        return minPersonAllowed;
    }

    public void setMinPersonAllowed(String minPersonAllowed) {
        this.minPersonAllowed = minPersonAllowed;
    }

    public String getAdditionalTicketPrice() {
        return additionalTicketPrice;
    }

    public void setAdditionalTicketPrice(String additionalTicketPrice) {
        this.additionalTicketPrice = additionalTicketPrice;
    }

}

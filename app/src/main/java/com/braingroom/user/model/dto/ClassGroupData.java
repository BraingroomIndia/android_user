package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by agrahari on 26/03/17.
 */

public class ClassGroupData implements Serializable {

    @SerializedName("group_price")
    private String price;

    @SerializedName("des")
    private String description;

    @SerializedName("start_range")
    private String startRange;

    @SerializedName("end_range")
    private String endRange;


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartRange() {
        return startRange;
    }

    public void setStartRange(String startRange) {
        this.startRange = startRange;
    }

    public String getEndRange() {
        return endRange;
    }

    public void setEndRange(String endRange) {
        this.endRange = endRange;
    }
}

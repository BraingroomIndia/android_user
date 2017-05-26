package com.braingroom.user.model.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by agrahari on 26/03/17.
 */

public class ClassLevelData implements Serializable {

    @SerializedName("level_id")
    private String levelId;

    @SerializedName("level_name")
    private String levelName;

    @SerializedName("price")
    private String price;

    @SerializedName("Description")
    private String description;

    @SerializedName("expert_level_id")
    private String expertLevelId;

    @SerializedName("Group")
    private List<ClassGroupData> groups;


    public String getLevelId() {
        return levelId;
    }

    public void setLevelId(String levelId) {
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

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

    public String getExpertLevelId() {
        return expertLevelId;
    }

    public void setExpertLevelId(String expertLevelId) {
        this.expertLevelId = expertLevelId;
    }

    public List<ClassGroupData> getGroups() {
        return groups;
    }

    public void setGroups(List<ClassGroupData> groups) {
        this.groups = groups;
    }

}

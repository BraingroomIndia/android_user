package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class CatalogueGroupResp extends BaseResp {

    @SerializedName("next_page")
    int nextPage;

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("id")
        public String id;

        @SerializedName("group_name")
        public String groupName;

        @SerializedName("segment_id")
        public String segmentId;

        @SerializedName("description")
        public String description;

        @SerializedName("group_image")
        public String groupImage;

        @SerializedName("banner_image")
        public String bannerImage;

        @SerializedName("status")
        public String status;

        @SerializedName("add_date")
        public String addDate;

        @SerializedName("modify_date")
        public String modifyDate;

    }
}

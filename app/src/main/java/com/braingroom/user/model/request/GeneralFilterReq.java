package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class GeneralFilterReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {

        @SerializedName("logged_in_userid")
        String loggedin_userid = "13";

        @SerializedName("search_key")
        String keywords = "";

        @SerializedName("start_date")
        String startDate = "";

        @SerializedName("end_date")
        String endDate = "";

        @SerializedName("search_cat_id")
        String categoryId = "";

        @SerializedName("search_seg_id")
        String segmentId = "";

        @SerializedName("class_type")
        String classType = "";

        @SerializedName("community_id")
        String communityId = "";

        @SerializedName("class_schedule")
        String classSchedule = "";

        @SerializedName("class_provider")
        String classProvider = "";

        @SerializedName("city_id")
        String cityId="";

        @SerializedName("location_id")
        String localityId = "";

        @SerializedName("price_sort_status")
        String sort = "";

        @SerializedName("sort_by_latest")
        String sortCat = "";

        @SerializedName("catlog")
        String catalog = "";

        @SerializedName("gift_id")
        String giftId = "";

    }
}

package com.braingroom.user.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

/**
 * Created by godara on 24/06/17.
 */
@AllArgsConstructor(suppressConstructorProperties = true)
public class QuoteReq {

    @SerializedName("braingroom")
    Snippet data;

    @Data
    public static class Snippet {

        @SerializedName("user_id")
        String userId;

        @SerializedName("class_id")
        String classId;

        @SerializedName("organization")
        String organization;

        @SerializedName("total_seats")
        String totalSeats;

        @SerializedName("location")
        String location;

        @SerializedName("class_type")
        String classType;

        @SerializedName("quote_data")
        String date;

        @SerializedName("mobile")
        String mobile;

        @SerializedName("explain_catalogue_class")
        String explainCatalogueClass;

        @SerializedName("description_yes")
        String descriptionYes;

        @SerializedName("description")
        String description;

        @SerializedName("catalog_id")
        String catalogueId;

        @SerializedName("quote_unit")
        String quoteUnit;

        @SerializedName("quote_unit_value")
        String quoteUnitValue;


    }
}

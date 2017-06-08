package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class VendorProfileResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("id")
        public String id;

        @SerializedName("category_id")
        public String categoryId;

        @SerializedName("category_name")
        public String categoryName;

        @SerializedName("community_id")
        public String communityId;

        @SerializedName("user_preference_id")
        public String userPreferenceId;

        @SerializedName("name")
        public String name;

        @SerializedName("email")
        public String email;

        @SerializedName("contact_no")
        public String contactNo;

        @SerializedName("city_id")
        public String cityId;

        @SerializedName("city")
        public String city;

        @SerializedName("locality_id")
        public String localityId;

        @SerializedName("locality")
        public String locality;

        @SerializedName("interest")
        public String interest;

        @SerializedName("institution")
        public String institution;

        @SerializedName("expertise_area")
        public String expertiseArea;

        @SerializedName("user_rating")
        public String userRating;

        @SerializedName("address")
        public String address;

        @SerializedName("description")
        public String description;

        //Edited By Vikas Godara
        @SerializedName("classes")
        public List<ClassDetail> classes;

        @SerializedName("review")
        public List<Review> review;

        @EqualsAndHashCode(callSuper = false)
        public static class ClassDetail {

            @SerializedName("id")
            public String id;

            @SerializedName("class_topic")
            public String classTopic;

            @SerializedName("about_class")
            public String aboutClass;

            @SerializedName("class_timing_id")
            public String classTimingId;

            @SerializedName("class_summary")
            public String classSummary;

            @SerializedName("catalog_description")
            public String catalogDescription;

            @SerializedName("no_of_session")
            public String noOfSession;

            @SerializedName("starting_month")
            public String startingMonth;

            @SerializedName("end_month")
            public String endMonth;

            @SerializedName("day_of_week")
            public String dayOfWeek;

            @SerializedName("time_of_day")
            public String timeOfDay;

            @SerializedName("class_date")
            public String classDate;

            @SerializedName("class_duration")
            public String classDuration;

            @SerializedName("class_type_id")
            public String classTypeId;

            @SerializedName("community_id")
            public String communityId;

            @SerializedName("max_ticket_available")
            public String maxTicketAvailable;

            @SerializedName("upload_video_name")
            public String uploadVideoName;

            @SerializedName("trending_status")
            public String trendingStatus;

            @SerializedName("featured_status")
            public String featuredStatus;

            @SerializedName("recommended_status")
            public String recommendedStatus;
            @SerializedName("catalogue_status")
            public String catalogueStatus;

            @SerializedName("age_category")
            public String ageCategory;

            @SerializedName("is_type")
            public String isType;
            @SerializedName("is_price_type")
            public String isPriceType;

            @SerializedName("class_nature")
            public String classNature;

            @SerializedName("price")
            public String price;

            @SerializedName("location")
            public String location;

            @SerializedName("photo")
            public String photo;


        }
        public static class Review {

            @SerializedName("text")
            public String text;

            @SerializedName("by")
            public String by;

        }
        //Edited By Vikas Godara



    }


}

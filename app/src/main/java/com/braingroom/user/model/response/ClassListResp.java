package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
@Getter
public class ClassListResp extends BaseResp {

    @SerializedName("next_page")
    int nextPage;

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @EqualsAndHashCode(callSuper = false)
    @Getter
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {

        @SerializedName("id")
        private String id;

        @SerializedName("wishlist")
        private String wishlist;

        @SerializedName("group_id")
        private String groupId;

        @SerializedName("featured_status")
        private String featuredStatus;

        @SerializedName("detail_class_link")
        private String classWebUrl;

        @SerializedName(value = "class_ratting", alternate = {"rating"})
        private Integer rating;

        @SerializedName("class_provided")
        private String teacher;

        @SerializedName("class_provider_id")
        private String teacherId;

        @SerializedName("class_provider_pic")
        private String teacherPic;

        @SerializedName("category")
        private String category;

        @SerializedName("category_id")
        private String categoryId;

        @SerializedName("segment")
        private String segment;

        @SerializedName("segment_id")
        private String segmentId;

        @SerializedName("is_couple_class")
        private int isCoupleClass;

        @SerializedName(value = "class_type_data")
        private String classTypeData;

        @SerializedName("session_date")
        private String sessionDate;

        @SerializedName("is_cod_avaiable")
        private int isCode;

        @SerializedName("sesssion_time")
        private String sessionTime;

        @SerializedName("class_type")
        private String classType;

        @SerializedName("no_of_seats")
        private String noOfSeats;

        @SerializedName("no_of_session")
        private String noOfSession;

        @SerializedName("class_date")
        private Boolean classDate;

        @SerializedName("class_start_time")
        private String classStartTime;

        @SerializedName("class_duration")
        private String classDuration;

        @SerializedName("class_topic")
        private String classTopic;

        @SerializedName("class_summary")
        private String classSummary;


        @SerializedName("photo")
        private String photo;

        @SerializedName("pic_name")
        private String picName;

        @SerializedName("video")
        private String videoId;

        @SerializedName("level_id")
        private String levelId;

        @SerializedName("level_name")
        private String levelName;

        @SerializedName("Description")
        private String description;

        @SerializedName("expert_level_id")
        private String expertLevelId;

        @SerializedName("price")
        private String price;


        @SerializedName("price_type")
        private String pricingType;

        @SerializedName("about_academy")
        private String aboutAcademy;

        @SerializedName("locality ")
        private String locality;
        //Edited By Vikas Goodara
        @SerializedName(value = "location", alternate = {"vendorClasseLocationDetail", "VendorClasseLocationDetail"})
        public List<Location> location;
        //Edited By Vikas Goodara
        @SerializedName(value = "vendorClasseLevelDetail", alternate = "VendorClasseLevelDetail")
        public List<ClassLevel> classLevels; //Edited by Vikas Godara

        @SerializedName("catalog_description")
        private String catalogDescription;

        @SerializedName("class_provided_by")
        private String classProvider;

        @SerializedName("localities")
        private List<String> catalogLocations;
        @SerializedName("price_symbol")
        private String priceSymbol;

        @SerializedName("price_code")
        private String priceCode;

        @SerializedName(value = "full_session")
        public List<ClassSession> fullSession;

        @SerializedName(value = "sessions")
        public List<ClassSession> microSessions;
   }

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Location {

        @SerializedName("locality_id")
        private String localityId;

        @SerializedName("locality")
        private String locality;

        @SerializedName("location_area")
        private String locationArea;

        @SerializedName("latitude")
        private String latitude;

        @SerializedName("longitude")
        private String longitude;

    }

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class ClassLevel {
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
        private List<ClassGroup> groups;
    }

    @EqualsAndHashCode(callSuper = false)
    public static class ClassGroup {
        @SerializedName("group_price")
        private String price;

        @SerializedName("des")
        private String description;

        @SerializedName("start_range")
        private String startRange;

        @SerializedName("end_range")
        private String endRange;
    }

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public class ClassSession {


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


    }
}
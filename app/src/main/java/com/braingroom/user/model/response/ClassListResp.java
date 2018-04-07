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
public class ClassListResp /*extends BaseResp*/ {

    @SerializedName("next_page")
    int nextPage;

    @SerializedName("braingroom")
    List<Snippet> data;

    @SerializedName("braingroom")
    List<MicroSessions> microSessionsdata;

    public List<MicroSessions> getMicroSessionsdata() {
        return microSessionsdata;
    }

    @SerializedName("res_code")
    private Integer resCode;

    public boolean getResCode() {
        return microSessionsdata != null && !microSessionsdata.isEmpty() && microSessionsdata.get(0) != null;
    }

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

        @SerializedName("full_session")
        private List<FullSession> fullSession;

        @SerializedName("sessions")
        private List<MicroSessions> microSessions;

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
    public static class FullSession {
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
        private Integer price;

        @SerializedName("offer_price")
        private Integer offerPrice;

        @SerializedName("min_person_allowed")
        private Integer minPersonAllowed;

        @SerializedName("additional_ticket_price")
        private Integer additionalTicketPrice;

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

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getOfferPrice() {
            return offerPrice;
        }

        public void setOfferPrice(Integer offerPrice) {
            this.offerPrice = offerPrice;
        }

        public Integer getMinPersonAllowed() {
            return minPersonAllowed;
        }

        public void setMinPersonAllowed(Integer minPersonAllowed) {
            this.minPersonAllowed = minPersonAllowed;
        }

        public Integer getAdditionalTicketPrice() {
            return additionalTicketPrice;
        }

        public void setAdditionalTicketPrice(Integer additionalTicketPrice) {
            this.additionalTicketPrice = additionalTicketPrice;
        }
    }

    @Data
    @Getter
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class MicroSessions {
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
        private Integer price;

        @SerializedName("offer_price")
        private Integer offerPrice;

        @SerializedName("min_person_allowed")
        private Integer minPersonAllowed;

        @SerializedName("additional_ticket_price")
        private Integer additionalTicketPrice;

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

        public Integer getPrice() {
            return price;
        }

        public void setPrice(Integer price) {
            this.price = price;
        }

        public Integer getOfferPrice() {
            return offerPrice;
        }

        public void setOfferPrice(Integer offerPrice) {
            this.offerPrice = offerPrice;
        }

        public Integer getMinPersonAllowed() {
            return minPersonAllowed;
        }

        public void setMinPersonAllowed(Integer minPersonAllowed) {
            this.minPersonAllowed = minPersonAllowed;
        }

        public Integer getAdditionalTicketPrice() {
            return additionalTicketPrice;
        }

        public void setAdditionalTicketPrice(Integer additionalTicketPrice) {
            this.additionalTicketPrice = additionalTicketPrice;
        }
    }


}

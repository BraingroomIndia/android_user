package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by godara on 29/05/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class BookingHistoryResp {
    @SerializedName("res_code")
    public String resCode;

    @SerializedName("res_msg")
    public String resMsg;

    @SerializedName("total_ticket")
    public Integer totalTicket;

    @SerializedName("braingroom")
    public List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("PayuTransaction")
        public PayuTransaction payuTransaction;

        @SerializedName("tickets")
        public Tickets tickets;

        @SerializedName("class_detail")
        public ClassDetail classDetail;


    }

    @Getter
    @EqualsAndHashCode(callSuper = false)
    public static class ClassDetail {

        @SerializedName("id")

        public String id;
        @SerializedName("class_provided_by")

        public String teacher;
        @SerializedName("category")

        public String category;
        @SerializedName("segment")

        public String segment;
        @SerializedName("class_type_data")

        public String classTypeData;
        @SerializedName("class_type")

        public String classType;
        @SerializedName("no_of_seats")

        public String noOfSeats;
        @SerializedName("no_of_session")

        public String noOfSession;
        @SerializedName("class_date")

        public String classDate;
        @SerializedName("class_start_time")

        public String classStartTime;
        @SerializedName("class_duration")

        public String classDuration;
        @SerializedName("class_topic")

        public String classTopic;
        @SerializedName("class_summary")

        public String classSummary;
        @SerializedName("class_ratting")

        public Integer classRatting;
        @SerializedName("photo")

        public String photo;
        @SerializedName("VendorClasseLevelDetail")

        public List<ClassLevel> classLevels;
        @SerializedName("VendorClasseLocationDetail")

        public List<Location> location;


        @Getter
        @EqualsAndHashCode(callSuper = false)
        public class ClassLevel {

            @SerializedName("level_id")

            public String levelId;
            @SerializedName("level_name")

            public String levelName;
            @SerializedName("price")

            public String price;
            @SerializedName("Description")

            public String description;
            @SerializedName("expert_level_id")

            public String expertLevelId;

        }

        @Getter
        @EqualsAndHashCode(callSuper = false)
        public class Location {

            @SerializedName("locality_id")

            public String localityId;
            @SerializedName("locality")

            public String locality;
            @SerializedName("location_area")

            public String locationArea;
            @SerializedName("latitude")

            public String latitude;
            @SerializedName("longitude")

            public String longitude;

        }


    }

    @EqualsAndHashCode(callSuper = false)
    static class PayuTransaction {

        @SerializedName("id")

        public String id;
        @SerializedName("created")

        public String created;
        @SerializedName("txnid")

        public String txnid;
        @SerializedName("amount")

        public String amount;
        @SerializedName("status")

        public String status;
        @SerializedName("pg_type")

        public String pgType;
        @SerializedName("payment_type")

        public String paymentType;
        @SerializedName("coupon_id")

        public Object couponId;
        @SerializedName("coupon_amount")

        public Object couponAmount;
        @SerializedName("promo_id")

        public String promoId;
        @SerializedName("promo_amount")

        public String promoAmount;

    }


    @EqualsAndHashCode(callSuper = false)
    static class Tickets {

        @SerializedName("id")

        public String id;
        @SerializedName("start_code")

        public String startCode;
        @SerializedName("end_code")

        public String endCode;

    }


}

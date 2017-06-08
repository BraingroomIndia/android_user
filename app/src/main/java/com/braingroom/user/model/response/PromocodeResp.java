package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class PromocodeResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("id")
        String id;
        @SerializedName("promo_code")
        String promoCode;
        @SerializedName("promotype")
        String promoType;
        @SerializedName("promocap")
        String promoCap;
        @SerializedName("from_date")
        String validFrom;
        @SerializedName("to_date")
        String validTo;
        @SerializedName("use_number")
        String useNumber;
        @SerializedName("times_per_user")
        String timesPerUser;
        @SerializedName("present_number")
        String presentNumber;
        @SerializedName("amount")
        String amount;
        @SerializedName("status")
        String status;

    }
}

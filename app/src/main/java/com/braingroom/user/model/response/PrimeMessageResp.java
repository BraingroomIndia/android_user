package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Created by godara on 15/09/17.
 */

public class PrimeMessageResp extends BaseResp {


    @Getter
    @SerializedName("braingroom")
    List<Snippet> data;

    @Getter
    public static class Snippet {
        @SerializedName("id")
        String id;
        @SerializedName("value")
        String value;
        @SerializedName("created_date")
        String createdDate;
    }
}

package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by himan on 6/7/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class CompetitionStatusResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("status")
        Integer status;

        @SerializedName("banner_url")
        String backgroundUrl;
    }
}

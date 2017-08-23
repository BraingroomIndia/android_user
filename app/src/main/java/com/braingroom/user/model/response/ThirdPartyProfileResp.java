package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class ThirdPartyProfileResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {
        String id;
        @SerializedName("name")
        String name;
        @SerializedName("education_info1")
        String eduInfo1;
        @SerializedName("education_info2")
        String eduInfo2;
        @SerializedName("interest")
        String interest;

        @SerializedName("follower_cnt")
        long follower_count;

        @SerializedName("following_cnt")
        long following_count;

        @SerializedName("post_cnt")
        long post_count;

        @SerializedName("follow_status")
        long followStatus;

    }
}

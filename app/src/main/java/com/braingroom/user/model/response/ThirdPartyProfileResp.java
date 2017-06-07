package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class ThirdPartyProfileResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
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

    }
}

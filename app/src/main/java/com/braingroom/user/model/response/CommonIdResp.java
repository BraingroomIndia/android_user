package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class CommonIdResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {

        @SerializedName("id")
        String id;

        @SerializedName(value = "name", alternate = {"college_name", "activity_name", "school_name", "ngo_name", "segment_name","city_name"})
        String textValue;
    }
}

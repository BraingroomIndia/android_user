package com.braingroom.user.model.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class GroupResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {
        @SerializedName("segment_id")
        Integer id;
        @SerializedName("group_name")
        String name;
        @SerializedName("group_image")
        String image;

        public Integer getId() {
            return id;
        }
    }
}

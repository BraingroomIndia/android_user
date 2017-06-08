package com.braingroom.user.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by Promolta-H on 02-02-2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class UploadPostApiResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        @SerializedName("url")
        String url;

        @SerializedName("success_url")
        String thumb;

    }

}

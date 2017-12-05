package com.braingroom.user.model.response;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by himan on 1/25/2017.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
@EqualsAndHashCode(callSuper = false)
public class SegmentResp extends BaseResp {

    String categoryId;
    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {
        @SerializedName("id")
        Integer id;
        @SerializedName("category_name")
        String segmentName;
        @SerializedName("segment_image")
        String segmentImage;

        public Integer getId() {
            return id;
        }

        public Snippet(String id, String segmentName,String segmentImage){
            this.id=Integer.parseInt(id);
            this.segmentName=segmentName;
            this.segmentImage=segmentImage;
        }
    }
}

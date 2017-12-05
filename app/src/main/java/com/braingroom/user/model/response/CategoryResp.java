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
public class CategoryResp extends BaseResp {

    @SerializedName("braingroom")
    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    @EqualsAndHashCode(callSuper = false)
    public static class Snippet {
        String id;
        @SerializedName("category_name")
        String categoryName;
        @SerializedName("category_image")
        String categoryImage;

        public Integer getId() {
            if (TextUtils.isDigitsOnly(id))
                return Integer.parseInt(id);
            else return null;
        }
    }
}

package com.braingroom.user.model.response;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class CategoryTreeResp {

    long respCode;

    List<Snippet> data;

    @Data
    @AllArgsConstructor(suppressConstructorProperties = true)
    public static class Snippet {
        CategoryResp.Snippet category;
        List<SegmentResp.Snippet> segments;
    }
}

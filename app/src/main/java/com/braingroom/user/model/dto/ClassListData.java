package com.braingroom.user.model.dto;

import java.util.List;

import lombok.Data;

/**
 * Created by agrahari on 09/04/17.
 */
@Data
public class ClassListData {

    int nextPage;
    List<ClassData> classDataList;
}

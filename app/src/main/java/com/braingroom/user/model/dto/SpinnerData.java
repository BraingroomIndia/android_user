package com.braingroom.user.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by agrahari on 01/04/17.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SpinnerData {

    private String id;

    private String textValue;

}

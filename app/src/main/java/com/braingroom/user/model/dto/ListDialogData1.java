package com.braingroom.user.model.dto;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by agrahari on 26/03/17.
 */

public class ListDialogData1 {

    private LinkedHashMap<String, Integer> items;

    public ListDialogData1(LinkedHashMap<String, Integer> items) {
        this.items = items;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(LinkedHashMap<String, Integer> items) {
        this.items = items;
    }

}

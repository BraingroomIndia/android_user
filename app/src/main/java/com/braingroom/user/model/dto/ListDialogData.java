package com.braingroom.user.model.dto;

import java.util.Map;

/**
 * Created by agrahari on 26/03/17.
 */

public class ListDialogData {

    private Integer[] selectedItems;
    private Map<String, String> items;

    public ListDialogData(Integer[] selectedItems, Map<String, String> items) {
        this.selectedItems = selectedItems;
        this.items = items;
    }

    public Integer[] getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(Integer[] selectedItems) {
        this.selectedItems = selectedItems;
    }

    public Map<String, String> getItems() {
        return items;
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }
}

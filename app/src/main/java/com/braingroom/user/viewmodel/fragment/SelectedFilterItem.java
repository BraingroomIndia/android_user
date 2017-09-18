package com.braingroom.user.viewmodel.fragment;


import android.databinding.ObservableField;

import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.viewmodel.ViewModel;


/**
 * Created by godara on 29/08/17.
 */

public class SelectedFilterItem extends ViewModel {
    public final ObservableField<String> selectedItemname = new ObservableField<>("");
    public final String selectedItemId;
    public final MyConsumer<SelectedFilterItem> onRemoveClicked;

    public SelectedFilterItem(String selectedItemname, String selectedItemId,
                              MyConsumer<SelectedFilterItem> onRemoveClicked) {
        this.selectedItemname.set(selectedItemname);
        this.selectedItemId = selectedItemId;
        this.onRemoveClicked = onRemoveClicked;
    }
}

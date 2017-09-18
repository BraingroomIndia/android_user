package com.braingroom.user.view;

import android.support.annotation.LayoutRes;

import com.braingroom.user.viewmodel.CustomDialogViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.List;

/**
 * Created by agrahari on 04/04/17.
 */

public abstract class DialogHelper {

    public ViewModel viewModel;

    public abstract void showDatePicker();

    public abstract void showMultiselectList(String title, List<String> items, Integer[] selectedItems);

    public abstract void showSingleSelectList(String title, List<String> items, Integer[] selectedItems, String positiveText);

    public abstract void showCustomView(@LayoutRes int layoutId, CustomDialogViewModel viewModel, boolean closeClickOutside);


    public void handleDialogOkClick(Integer[] selectedIndices) {
    }

    public void setViewModel(ViewModel vm) {
        this.viewModel = vm;
    }

    public abstract void showListDialog(String title, List<String> items);
}

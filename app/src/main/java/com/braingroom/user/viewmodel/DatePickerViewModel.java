package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.view.DialogHelper;

public class DatePickerViewModel extends DialogViewModel {

    public final ObservableField<String> date;
    public DialogHelper dialogHelper;

    public DatePickerViewModel(@NonNull DialogHelper dialogHelper,String title,String defaultDate) {
        super(dialogHelper, title);
        date = new ObservableField(defaultDate);
        this.dialogHelper = dialogHelper;
    }

    @Override
    public void show() {
        this.dialogHelper.showDatePicker();
    }

    public void reset(){
        date.set("YYYY-MM-DD");
        selectedItemsText.set("select filter values");
    }

}

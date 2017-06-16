package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;

public class QuoteFormViewModel extends ViewModel {

    public final DataItemViewModel organizationName;
    public final DataItemViewModel audienceStrength;
    public final DataItemViewModel hostClassLocation;
    public final DataItemViewModel mobileNumber;
    public final DataItemViewModel classNeedDetail;
    public final DataItemViewModel description;
    public final ListDialogViewModel1 classModeVm;
    public final DatePickerViewModel dateVm;


    public QuoteFormViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator,
                              @NonNull HelperFactory helperFactory) {
        organizationName = new DataItemViewModel("");
        audienceStrength = new DataItemViewModel("");
        hostClassLocation = new DataItemViewModel("");
        mobileNumber = new DataItemViewModel("");
        classNeedDetail = new DataItemViewModel("");
        description = new DataItemViewModel("");
        final LinkedHashMap<String, Integer> classModeData = new LinkedHashMap<>();
        classModeData.put("Workshops", 1);
        classModeData.put("Online classes", 2);
        classModeData.put("Webinars", 3);
        classModeData.put("Classes", 4);
        classModeData.put("Learning events & activities", 5);
        classModeVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Class mode", messageHelper,
                Observable.just(new ListDialogData1(classModeData)), new HashMap<String, Integer>(), false, null);
        dateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "Date", "choose");

    }


}

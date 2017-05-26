package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassData;

import io.reactivex.functions.Action;

public class ClassItemViewModel extends ViewModel {

    @NonNull
    public final ClassData classData;

    public ObservableField<String> fixedClassDate = new ObservableField<>();

    @NonNull
    public final int placeHolder = R.drawable.white_background_drawable;

    public ClassItemViewModel(@NonNull final ClassData classData, @NonNull final Action clickAction) {
        this.classData = classData;
        this.onClicked = clickAction;
        if ("fixed".equalsIgnoreCase(classData.getClassTypeData())) {
            fixedClassDate .set(classData.getSessionTime() + ", " + classData.getSessionDate());
        }

    }

    @Nullable
    public final Action onClicked;

}

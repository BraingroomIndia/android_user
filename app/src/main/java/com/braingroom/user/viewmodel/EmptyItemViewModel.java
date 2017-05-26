package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.functions.Action;

public class EmptyItemViewModel extends ViewModel {

    @NonNull
    public final String image;

    @NonNull
    public final String title;

    @NonNull
    public final int placeHolder;

    public EmptyItemViewModel(@NonNull final int placeHolder, final String icon, @NonNull final String title,  final Action clickAction) {
        this.image = icon;
        this.title = title;
        this.placeHolder = placeHolder;
        this.onClicked = clickAction;
    }

    @Nullable
    public final Action onClicked;

}

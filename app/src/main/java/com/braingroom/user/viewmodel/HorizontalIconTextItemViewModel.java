package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.braingroom.user.R;

import io.reactivex.functions.Action;

public class HorizontalIconTextItemViewModel extends ViewModel {

    @NonNull
    public final String image;

    @NonNull
    public final int imageRes;

    @NonNull
    public final String title;

    @NonNull
    public final int placeHolder = R.drawable.white_background_drawable;

    public HorizontalIconTextItemViewModel(@NonNull final String icon, @NonNull final String title, @NonNull final Action clickAction) {
        this.image = icon;
        this.title = title;
        this.imageRes = 0;
        this.onClicked = clickAction;
    }

    public HorizontalIconTextItemViewModel(@NonNull final int iconRes, @NonNull final String title, @NonNull final Action clickAction) {
        this.imageRes = iconRes;
        this.title = title;
        this.image = null;
        this.onClicked = clickAction;
    }

    @Nullable
    public final Action onClicked;

}

package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.braingroom.user.R;
import com.braingroom.user.utils.MyConsumer;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class IconTextItemViewModel extends ViewModel {

    @NonNull
    public final String image;

    @NonNull
    public final int imageRes;

    @NonNull
    public final String title;

    @NonNull
    public final int placeHolder = R.drawable.white_background_drawable;

    public final ObservableBoolean isSelected = new ObservableBoolean();

    public IconTextItemViewModel(@NonNull final String icon, @NonNull final String title
            , @NonNull final MyConsumer<IconTextItemViewModel> clickAction) {
        this.image = icon;
        this.title = title;
        this.imageRes = 0;
        this.onClicked = clickAction;
    }

    public IconTextItemViewModel(@NonNull final String icon, @NonNull final String title
            , @NonNull final MyConsumer<IconTextItemViewModel> clickAction, boolean selected
            , @NonNull final PublishSubject<IconTextItemViewModel> selectorSubject) {
        this.image = icon;
        this.title = title;
        this.imageRes = 0;
        this.onClicked = clickAction;
        isSelected.set(selected);
        if (selectorSubject != null) {
            selectorSubject.subscribe(new Consumer<IconTextItemViewModel>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel iconTextItemViewModel) throws Exception {
                    if (IconTextItemViewModel.this == iconTextItemViewModel) {
                        isSelected.set(true);
                    } else {
                        isSelected.set(false);
                    }
                }
            });
        }
    }

    public IconTextItemViewModel(@NonNull final int iconRes, @NonNull final String title
            , @NonNull final MyConsumer<IconTextItemViewModel> clickAction) {
        this.imageRes = iconRes;
        this.title = title;
        this.image = null;
        this.onClicked = clickAction;
    }

    @Nullable
    public final MyConsumer<IconTextItemViewModel> onClicked;

}

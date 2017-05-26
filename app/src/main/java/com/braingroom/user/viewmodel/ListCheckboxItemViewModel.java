package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ListCheckboxItemViewModel extends ViewModel {

    @NonNull
    public final ObservableBoolean isChecked;

    @NonNull
    public final ObservableField<String> title;

    public ListCheckboxItemViewModel(@NonNull String title, final boolean checked, final Consumer<Boolean> clickConsumer) {
        this.title = new ObservableField<>(title);
        this.isChecked = new ObservableBoolean(checked);

        onClicked = new Action() {
            @Override
            public void run() throws Exception {
                isChecked.set(!isChecked.get());
                clickConsumer.accept(isChecked.get());
            }
        };
    }

    @Nullable
    public final Action onClicked;

}

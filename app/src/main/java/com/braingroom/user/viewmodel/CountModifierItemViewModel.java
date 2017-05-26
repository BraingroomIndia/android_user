package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class CountModifierItemViewModel extends ViewModel {

    @NonNull
    public final ObservableField<String> countText;

    @Nullable
    public final Action onPlusClicked;
    @Nullable
    public final Action onMinusClicked;

    public final Consumer<Integer> countChangeConsumer;

    public CountModifierItemViewModel(final int count, Consumer<Integer> consumer) {
        this.countText = new ObservableField<>(count + "");
        this.countChangeConsumer = consumer;

        onPlusClicked = new Action() {
            @Override
            public void run() throws Exception {
                int currentCount = Integer.parseInt(countText.get());
                currentCount = currentCount + 1;
                countText.set(currentCount + "");
                countChangeConsumer.accept(currentCount);
            }
        };
        onMinusClicked = new Action() {
            @Override
            public void run() throws Exception {
                int currentCount = Integer.parseInt(countText.get());
                if (currentCount > 1) {
                    currentCount = currentCount - 1;
                    countText.set(currentCount + "");
                    countChangeConsumer.accept(currentCount);
                }
            }
        };
    }


}

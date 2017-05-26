package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.braingroom.user.utils.MyConsumer;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

public class DataItemViewModel extends ViewModel {

    public final ObservableField<String> s_1 = new ObservableField<>();
    public final ObservableBoolean isSelected = new ObservableBoolean();

    @Nullable
    public final MyConsumer<DataItemViewModel> onClicked;

    public DataItemViewModel(@NonNull final String s_1, boolean selected, @NonNull final MyConsumer<DataItemViewModel> clickConsumer
            , PublishSubject<DataItemViewModel> selectorSubject) {
        this.s_1.set(s_1);
        this.isSelected.set(selected);
        this.onClicked = clickConsumer;
        if (selectorSubject != null) {
            selectorSubject.subscribe(new Consumer<DataItemViewModel>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull DataItemViewModel dataItemViewModel) throws Exception {
                    if (DataItemViewModel.this == dataItemViewModel) {
                        isSelected.set(true);
                    } else {
                        isSelected.set(false);
                    }
                }
            });
        }
    }

    public DataItemViewModel(@NonNull final String s_1) {
        this.s_1.set(s_1);
        this.onClicked = null;
    }

    public void toggleSelected() {
        isSelected.set(!isSelected.get());
    }

    public void setSelected(DataItemViewModel viewModel) {
        if (this == viewModel)
            isSelected.set(true);
        else isSelected.set(false);
    }

}

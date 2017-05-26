package com.braingroom.user.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.SpinnerData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import lombok.Getter;

public class SpinnerViewModel extends ViewModel {
    public final ObservableArrayList<String> items = new ObservableArrayList<>();
    @Getter
    private Map<String, String> itemIdMap = new HashMap<>();
    public final ObservableInt selectedIdx = new ObservableInt();
    public final ObservableBoolean selectable = new ObservableBoolean(false);
    public final Observable<List<SpinnerData>> listObservable;
    private Observable<List<SpinnerData>> apiObservable;
    private String defaultSelectedValue;

    public SpinnerViewModel(Observable<List<SpinnerData>> apiObservable, final String defaultValue) {
        this.apiObservable = apiObservable;
        listObservable = this.apiObservable.doOnNext(new Consumer<List<SpinnerData>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull List<SpinnerData> dataList) throws Exception {
                items.clear();
                itemIdMap.clear();
                items.add(defaultValue);
                for (SpinnerData data : dataList) {
                    itemIdMap.put(data.getTextValue(), data.getId());
                    items.add(data.getTextValue());
                }
                if (defaultSelectedValue != null) setDefaultSelectedIndex(defaultSelectedValue);
                selectable.set(true);
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

            }
        });
        listObservable.subscribe();
    }


    public void refreshData(Observable<List<SpinnerData>> apiObservable) {
        this.apiObservable = apiObservable;
        listObservable.subscribe();
    }

    public void refreshData() {
        listObservable.subscribe();
    }

    public void setDefaultSelectedIndex(@NonNull String defaultSelectedValue) {
        this.defaultSelectedValue = defaultSelectedValue;
        if (items.size() > 0) {
            int arrayIdx = items.indexOf(defaultSelectedValue);
            if (arrayIdx >= 0)
                selectedIdx.set(arrayIdx);
        }
    }

    public String getSelectedItemId() {
        String id = itemIdMap.get(items.get(selectedIdx.get()));
        if (id == null) return "-1";
        return id;
    }

}

package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.response.MarkerDataResp;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class MarkerClassListViewModel extends CustomDialogViewModel {

    public final int defaultCount = 2;
    public final Observable<List<ViewModel>> items;
    public final ObservableField<String> title = new ObservableField<>("");

    public MarkerClassListViewModel(@NonNull final String title, @NonNull final Navigator navigator, String latitude, String longitude) {
        this.title.set(title);
        items = getLoadingItems().mergeWith(apiService.getMarkerData(latitude, longitude).map(new Function<MarkerDataResp, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(MarkerDataResp resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();
                for (final MarkerDataResp.Snippet elem : resp.getData()) {
                    results.add(new DataItemViewModel(elem.getClassTopic(), false, new MyConsumer<DataItemViewModel>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull DataItemViewModel var1) {
                            Bundle data = new Bundle();
                            data.putString("id", elem.getClassId());
                            data.putString("origin", ClassListViewModel1.ORIGIN_HOME);
                            navigator.navigateActivity(ClassDetailActivity.class, data);

                        }
                    }, null));
                }
                return results;
            }
        }));
    }


    private Observable<List<ViewModel>> getLoadingItems() {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(defaultCount, new RowShimmerItemViewModel()));
        return Observable.just(result);
    }

}



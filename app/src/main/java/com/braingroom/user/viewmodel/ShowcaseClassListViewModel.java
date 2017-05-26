package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class ShowcaseClassListViewModel extends ViewModel {

    public final int defaultCount = 4;
    public final Observable<List<ViewModel>> items;
    public final ObservableField<String> title = new ObservableField<>("");

    public ShowcaseClassListViewModel(@NonNull final String title, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, Observable<List<ClassData>> apiObservable, final Class<?> destination) {
        this.title.set(title);
        items = getLoadingItems().mergeWith(apiObservable.map(new Function<List<ClassData>, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(List<ClassData> resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();
//                if (resp.size() == 0) resp = getDefaultResponse();
                for (final ClassData elem : resp) {
                    results.add(new ClassItemViewModel(elem, new Action() {
                        @Override
                        public void run() throws Exception {
                            if (!elem.getId().equals("-1")) {
                                Bundle data = new Bundle();
                                data.putString("id", elem.getId());
                                navigator.navigateActivity(destination, data);
                            }
                        }
                    }));
                }
                return results;
            }
        }))
                ;

    }

//    private List<ClassData> getDefaultResponse() {
//        return Collections.nCopies(defaultCount, new ClassData());
//    }
//
    private Observable<List<ViewModel>> getLoadingItems() {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(defaultCount, new TileShimmerItemViewModel()));
        return Observable.just(result);
    }

}

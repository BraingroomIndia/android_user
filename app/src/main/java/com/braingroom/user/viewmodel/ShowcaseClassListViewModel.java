package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class ShowcaseClassListViewModel extends ViewModel {

    public final int defaultCount = 4;
    public final Observable<List<ViewModel>> items;
    private boolean apiSuccessful = false;
    List<ViewModel> results;
    public final ObservableField<String> title = new ObservableField<>("");

    public ShowcaseClassListViewModel(@NonNull final String title, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, final Observable<List<ClassData>> apiObservable, final Class<?> destination) {
        this.title.set(title);
        items = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return !apiSuccessful;
            }
        }).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
            @Override
            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                results = new ArrayList<>();
                return getLoadingItems().mergeWith(apiObservable.map(new Function<List<ClassData>, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(List<ClassData> resp) throws Exception {

                        apiSuccessful = true;
//                if (resp.size() == 0) resp = getDefaultResponse();
                        for (final ClassData elem : resp) {
                            results.add(new ClassItemViewModel(elem, new Action() {
                                @Override
                                public void run() throws Exception {
                                    if (!elem.getId().equals("-1")) {
                                        Bundle data = new Bundle();
                                        data.putString("id", elem.getId());
                                        data.putString("origin", ClassListViewModel1.ORIGIN_HOME);
                                        navigator.navigateActivity(destination, data);
                                    }
                                }
                            }));
                        }
                        return results;
                    }
                }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return results;
                    }
                }));

            }
        });


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

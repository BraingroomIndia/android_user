package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class ClassSimpleListViewModel extends ViewModel {

    public final Observable<List<ViewModel>> classes;

    public ClassSimpleListViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull String listType) {
        Observable<List<ClassData>> apiObservable = null;
        if("wishlist".equalsIgnoreCase(listType)) apiObservable = apiService.getWishList();
        if("bookinghistory".equalsIgnoreCase(listType)) apiObservable = apiService.getBookingHistory();
        classes = Observable.just(getDefaultClasses()).mergeWith(apiObservable)
                .map(new Function<List<ClassData>, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(List<ClassData> resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        if (resp.size() == 0) resp = getDefaultClasses();
                        for (final ClassData elem : resp) {
                            results.add(new ClassItemViewModel(elem, new Action() {
                                @Override
                                public void run() throws Exception {
                                    if (!elem.getId().equals("-1")) {
                                        Bundle data = new Bundle();
                                        data.putString("id", elem.getId());
                                        navigator.navigateActivity(ClassDetailActivity.class, data);
                                    }
                                }
                            }));
                        }
                        return results;
                    }
                });

    }

    private List<ClassData> getDefaultClasses() {
        return Collections.nCopies(0, new ClassData());
    }


}

package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class ClassSimpleListViewModel extends ViewModel {

    private boolean paginationInProgress = false;
    private ObservableField<Integer> nextPage = new ObservableField<>(1);
    private boolean nextPageAvailable =true;
    private final String listType;
    Observable<List<ClassData>> apiObservable = null;

    public Observable<List<ViewModel>> result;
    private List<ViewModel> classes;

    public ClassSimpleListViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull String listType1) {
        this.listType = listType1;
        classes = new ArrayList<>();

        result = FieldUtils.toObservable(nextPage).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
            @Override
            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                paginationInProgress = true;
                if ("wishlist".equalsIgnoreCase(listType))
                    apiObservable = apiService.getWishList(nextPage.get());
                else if ("bookinghistory".equalsIgnoreCase(listType))
                    apiObservable = apiService.getBookingHistory(nextPage.get());

                return apiObservable.map(new Function<List<ClassData>, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(List<ClassData> resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        if (resp.size() == 0)
                            nextPageAvailable =false;
                        for (final ClassData elem : resp) {
                            if (elem.getClassType().equalsIgnoreCase("Online Classes"))
                                elem.setLocality("Online");
                            else if (elem.getClassType().equalsIgnoreCase("Webinars"))
                                elem.setLocality("Webinar");
                            classes.add(new ClassItemViewModel(elem, new Action() {
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
                        paginationInProgress = false;
                        return classes;
                    }
                });

            }
        });

    }

    @Override
    public void paginate() {
        if (nextPageAvailable && !paginationInProgress) {
            nextPage.set((nextPage.get() + 1));
        }
    }


    private List<ClassData> getDefaultClasses() {
        return Collections.nCopies(0, new ClassData());
    }


}

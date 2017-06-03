package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.viewmodel.ClassItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class VendorClassViewModel extends ViewModel {

    public final Observable<List<ViewModel>> items;
    private List<ViewModel> classes;

    private boolean paginationInProgress = false;
    private ObservableField<Integer> nextPage = new ObservableField<>(1);
    private boolean nextPageAvailable = true;

    public VendorClassViewModel(@NonNull final Navigator navigator, final String vendorId) {
        classes =new ArrayList<>();

        items = FieldUtils.toObservable(nextPage).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
            @Override
            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getVendorClassList(nextPage.get(), vendorId).map(new Function<List<ClassData>, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(List<ClassData> resp) throws Exception {
                        if (resp.size() == 0)
                            nextPageAvailable = false;
                        for (final ClassData elem : resp) {
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

        /*apiService.getVendorClassList(vendorId))
                .map(new Function<List<ClassData>, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(List<ClassData> resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        if (resp.size() == 0) resp = getLoadingItems();
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
                });;*/

    public List<ClassData> getLoadingItems() {
        return Collections.nCopies(6, new ClassData());
    }


}

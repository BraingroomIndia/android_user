package com.braingroom.user.viewmodel.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.viewmodel.ClassItemViewModel;
import com.braingroom.user.viewmodel.ClassListViewModel1;
import com.braingroom.user.viewmodel.RowShimmerItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class VendorClassViewModel extends ViewModel {

    public final Observable<List<ViewModel>> items;
    private List<ViewModel> classes;

    private boolean paginationInProgress = false;
    private int nextPage = 1;
    private int currentPage = 0;

    public VendorClassViewModel(@NonNull final Navigator navigator, final String vendorId) {
        classes = new ArrayList<>();

        items = FieldUtils.toObservable(callAgain)/*.filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return false;
            }
        })*/.filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                if (nextPage == 1)
                    paginationInProgress = true;
                return currentPage < nextPage;
            }
        }).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
            @Override
            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getVendorClassList(nextPage, vendorId).map(new Function<List<ClassData>, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(List<ClassData> resp) throws Exception {
                        currentPage = nextPage;
                        if (resp.size() == 0)
                            nextPage = -1;
                        for (final ClassData elem : resp) {
                            classes.add(new ClassItemViewModel(elem, new Action() {
                                @Override
                                public void run() throws Exception {
                                    if (!elem.getId().equals("-1")) {
                                        Bundle data = new Bundle();
                                        data.putString("id", elem.getId());
                                        data.putString("origin", ClassListViewModel1.ORIGIN_HOME);
                                        navigator.navigateActivity(ClassDetailActivity.class, data);
                                    }
                                }
                            }));
                        }
                        paginationInProgress = false;
                        return classes;
                    }
                }).mergeWith(getLoadingItems(3));
            }
        });
    }

    private Observable<List<ViewModel>> getLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(classes);
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
    }

    @Override
    public void paginate() {
        if (!paginationInProgress) {
            nextPage = nextPage + 1;
            paginationInProgress = true;
            callAgain.set(callAgain.get() + 1);
        }
    }

    @Override
    public void retry() {
        callAgain.set(callAgain.get() + 1);
        connectivityViewmodel.isConnected.set(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        connectivityViewmodel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        connectivityViewmodel.onPause();
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

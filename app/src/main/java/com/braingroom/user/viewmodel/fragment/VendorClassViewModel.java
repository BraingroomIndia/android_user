package com.braingroom.user.viewmodel.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.viewmodel.ClassItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class VendorClassViewModel extends ViewModel {

    public final Observable<List<ViewModel>> items;

    public VendorClassViewModel(@NonNull final Navigator navigator,String vendorId) {

        items = Observable.just(getLoadingItems()).mergeWith(apiService.getVendorClassList(vendorId))
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
                });;
    }

    public List<ClassData> getLoadingItems() {
        return Collections.nCopies(6, new ClassData());
    }


}

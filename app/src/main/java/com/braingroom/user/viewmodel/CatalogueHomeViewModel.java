package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.model.response.CatalogueGroupResp;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class CatalogueHomeViewModel extends ViewModel {


    public final Observable<List<ViewModel>> feedItems;

    public CatalogueHomeViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {

        feedItems = getLoadingItems(2).mergeWith(apiService.getCatalogueGroups().map(new Function<CatalogueGroupResp, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(CatalogueGroupResp resp) throws Exception {
                List<ViewModel> results = new ArrayList<>();
                for (final CatalogueGroupResp.Snippet elem : resp.getData()) {
                    results.add(new CatalogueGroupItemViewModel(elem, navigator));
                }
                return results;
            }
        }));

    }

    private Observable<List<ViewModel>> getLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
    }


}

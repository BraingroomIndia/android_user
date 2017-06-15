package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.model.response.GiftcardResp;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class ShowcaseGiftcardListViewModel extends ViewModel {

    public final int defaultCount = 3;
    public final Observable<List<ViewModel>> items;
    public final ObservableField<String> title = new ObservableField<>("");

    public ShowcaseGiftcardListViewModel(@NonNull final String title, @NonNull final MessageHelper messageHelper,
                                         @NonNull final Navigator navigator, final Observable<List<GiftcardResp.DataSnippet>> apiObservable,
                                         final String giftcardType) {
        this.title.set(title);
        items = FieldUtils.toObservable(retries).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
            @Override
            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getLoadingItems().mergeWith(apiObservable.map(new Function<List<GiftcardResp.DataSnippet>, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(List<GiftcardResp.DataSnippet> resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        for (final GiftcardResp.DataSnippet elem : resp) {
                            results.add(new GiftcardItemViewModel(navigator,elem,giftcardType));
                        }
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

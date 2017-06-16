package com.braingroom.user.viewmodel.fragment;

import android.support.annotation.NonNull;

import com.braingroom.user.model.response.VendorReviewResp;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.viewmodel.RowShimmerItemViewModel;
import com.braingroom.user.viewmodel.VendorReviewItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

public class VendorReviewsViewModel extends ViewModel {

    public final Observable<List<ViewModel>> items;

    public VendorReviewsViewModel(@NonNull final MessageHelper messageHelper, final String vendorId) {
//// TODO: 18/04/17 remove hardcoded vendor id
        items = FieldUtils.toObservable(callAgain).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return getLoadingItems(0).mergeWith(apiService.getVendorReviews(vendorId).map(new Function<VendorReviewResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(VendorReviewResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        callAgain = null;
                        for (final VendorReviewResp.Snippet elem : resp.getData()) {
                            results.add(new VendorReviewItemViewModel(messageHelper, elem.getReviewerName(), elem.getReview(), elem.getRating()));
                        }
                        return results;
                    }
                }));

            }
        });
    }
    @Override
    public void retry(){
        callAgain.set(callAgain.get()+1);
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


    private Observable<List<ViewModel>> getLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
    }


}

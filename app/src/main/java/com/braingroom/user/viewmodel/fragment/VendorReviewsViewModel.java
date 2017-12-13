package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.response.ReviewGetResp;
import com.braingroom.user.model.response.VendorReviewResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.viewmodel.EmptyItemViewModel;
import com.braingroom.user.viewmodel.ReviewItemViewModel;
import com.braingroom.user.viewmodel.RowShimmerItemViewModel;
import com.braingroom.user.viewmodel.VendorReviewItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

public class VendorReviewsViewModel extends ViewModel {

    public final Observable<List<ViewModel>> items;
    public final Action onAddReviewClicked;

    public final UiHelper uiHelper;

    private int pageNumber = 0;

    public interface UiHelper {
        void addReviewFragment();
    }

    List<ViewModel> results = new ArrayList<>();

    public VendorReviewsViewModel(@NonNull final MessageHelper messageHelper, @NonNull final UiHelper uiHelper, final String vendorId) {
        this.uiHelper = uiHelper;
        callAgain = new ObservableField<>(0);
        items = FieldUtils.toObservable(callAgain).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                messageHelper.showProgressDialog("", "Loading");
                return apiService.getReview(Constants.vendorReview, vendorId, pageNumber).map(new Function<ReviewGetResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(ReviewGetResp resp) throws Exception {
                        for (final ReviewGetResp.Snippet snippet : resp.getData()) {
                            results.add((new ReviewItemViewModel(snippet.getRating(), snippet.getFirstName(), snippet.getReviewMessage(), snippet.getTimeStamp())));
                        }
                        if (results.isEmpty()) {
                            results.add(new EmptyItemViewModel(R.drawable.ic_no_post_64dp, null, "No review found", null));
                            pageNumber = -1;
                        }
                        messageHelper.dismissActiveProgress();
                        return results;
                    }
                });

            }
        });
        onAddReviewClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (getLoggedIn())
                    uiHelper.addReviewFragment();
                else
                    messageHelper.showLoginRequireDialog("Only logged in user can add review", null);
            }
        };
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

    @Override
    public void paginate() {
        if (pageNumber == -1)
            return;
        else
            pageNumber++;
        super.paginate();
        callAgain.set(callAgain.get() + 1);
    }

    private Observable<List<ViewModel>> getLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
    }


}

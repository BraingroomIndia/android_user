package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.model.response.ThirdPartyProfileResp;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ThirdPartyViewModel extends ViewModel {

    public Observable<List<ViewModel>> classItems;
    public final ConnectivityViewModel connectivityViewmodel;
    public final DataItemViewModel name = new DataItemViewModel("");
    public final DataItemViewModel educationInfo1 = new DataItemViewModel("");
    public final DataItemViewModel educationInfo2 = new DataItemViewModel("");
    public final DataItemViewModel interest = new DataItemViewModel("");


    public ThirdPartyViewModel(@NonNull String userId, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });

        apiService.getThirdPartyProfile(userId).subscribe(new Consumer<ThirdPartyProfileResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ThirdPartyProfileResp resp) throws Exception {
                name.s_1.set(resp.getData().get(0).getName());
                educationInfo1.s_1.set(resp.getData().get(0).getEduInfo1());
                educationInfo2.s_1.set(resp.getData().get(0).getEduInfo2());
                interest.s_1.set(resp.getData().get(0).getInterest());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.e("ThirdPartyProfile", "accept: ", throwable);
            }
        });

//        classItems = (FieldUtils.toObservable(retries).mergeWith(FieldUtils.toObservable(nextPage)).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
//            @Override
//            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
//                paginationInProgress = true;
//                if ("wishlist".equalsIgnoreCase(listType))
//                    apiObservable = apiService.getWishList(nextPage.get());
//                else if ("bookinghistory".equalsIgnoreCase(listType))
//                    apiObservable = apiService.getBookingHistory(nextPage.get());
//
//                return apiObservable
//                        .map(new Function<List<ClassData>, List<ViewModel>>() {
//                            @Override
//                            public List<ViewModel> apply(List<ClassData> resp) throws Exception {
//                                List<ViewModel> results = new ArrayList<>();
//                                if (resp.size() == 0)
//                                    nextPageAvailable = false;
//                                if (resp.size() == 0 && classes.size() == 0) {
//                                    classes.add(new EmptyItemViewModel(R.drawable.empty_board, null, "No classes Available", null));
//                                }
//                                for (final ClassData elem : resp) {
//                                    if (elem.getClassType().equalsIgnoreCase("Online Classes"))
//                                        elem.setLocality("Online");
//                                    else if (elem.getClassType().equalsIgnoreCase("Webinars"))
//                                        elem.setLocality("Webinar");
//                                    classes.add(new ClassItemViewModel(elem, new Action() {
//                                        @Override
//                                        public void run() throws Exception {
//                                            if (!elem.getId().equals("-1")) {
//                                                Bundle data = new Bundle();
//                                                data.putString("id", elem.getId());
//                                                navigator.navigateActivity(ClassDetailActivity.class, data);
//                                            }
//                                        }
//                                    }));
//                                }
//                                paginationInProgress = false;
//                                return classes;
//                            }
//                        }).mergeWith(getLoadingItems(3));

//            }
//        }));

    }


    private Observable<List<ViewModel>> getLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
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

}

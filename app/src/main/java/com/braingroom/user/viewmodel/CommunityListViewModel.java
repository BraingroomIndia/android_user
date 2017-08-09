package com.braingroom.user.viewmodel;

/**
 * Created by godara on 09/08/17.
 */

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.response.CommunityResp;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class CommunityListViewModel extends ViewModel {

    public final int defaultCount = 9;
    public final Observable<List<ViewModel>> gridItems;
    public final ObservableField<String> title = new ObservableField<>("Discover the joy of learning in groups");
    List<ViewModel> results;

    public CommunityListViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {

        gridItems = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return !apiSuccessful;
            }
        }).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
            @Override
            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return Observable.just(getDefaultResponse()).mergeWith(apiService.getCommunity())
                        .map(new Function<CommunityResp, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(CommunityResp resp) throws Exception {
                                results = new ArrayList<>();
                                if (resp.getData().size() == 0) resp = getDefaultResponse();
                                for (final CommunityResp.Snippet snippet : resp.getData()) {
                                    if (snippet.getImage() != null)
                                        snippet.setImage(snippet.getImage().replaceAll("jpg", "png"));
                                    results.add
                                            (new IconTextItemViewModel(snippet.getImage(), snippet.getName(), new MyConsumer<IconTextItemViewModel>() {
                                                @Override
                                                public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                                                    if (!snippet.getId().equals("-1")) {
                                                        apiSuccessful = true;
                                                        Bundle data = new Bundle();
                                                        FilterData filterData = new FilterData();
                                                        HashMap<String, Integer> communityFilterMap = new HashMap<String, Integer>();
                                                        communityFilterMap.put(snippet.getName(), Integer.parseInt(snippet.getId()));
                                                        filterData.setCommunityId(snippet.getId());
                                                        data.putSerializable("community", communityFilterMap);
                                                        data.putSerializable("filterData", filterData);
                                                        data.putString("origin", FilterViewModel.ORIGIN_COMMUNITY);
                                                        navigator.navigateActivity(ClassListActivity.class, data);
                                                    }
                                                }
                                            }));
                                }
                                return results;
                            }
                        }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                return results;
                            }
                        });
            }
        });


    }

    private CommunityResp getDefaultResponse() {
        return new CommunityResp(Collections.nCopies(defaultCount, new CommunityResp.Snippet("-1", "", null)));
    }
}


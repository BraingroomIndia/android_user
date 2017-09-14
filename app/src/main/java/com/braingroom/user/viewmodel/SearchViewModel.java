package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SearchViewModel extends ViewModel {
    public final ObservableBoolean editable = new ObservableBoolean(false);
    public final ObservableField<String> searchQuery = new ObservableField<>("");
    public final Observable<List<ViewModel>> results;
    public final Map<String, String> mainCategories = new HashMap<>();
    public final Action onBackClicked, onSearchClicked;

    public SearchViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {

        apiService.getCategory().subscribe(new Consumer<CategoryResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull CategoryResp categoryResp) throws Exception {
                for (CategoryResp.Snippet snippet : categoryResp.getData()) {
                    mainCategories.put(snippet.getCategoryName(), snippet.getId());
                }
                editable.set(true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

            }
        });

        results = FieldUtils.toObservable(searchQuery)
                .map(new Function<String, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(String s) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        if (s.length() > 0) {
                            final List<String> categoryList = new ArrayList<>(mainCategories.keySet());
                            for (int i = 0; i < categoryList.size(); i++) {
                                results.add(new SearchResultItemViewModel(new SearchResultItem(s, mainCategories.get(categoryList.get(i)) + "", categoryList.get(i)), navigator));
                            }
                        }
                        return results;
                    }
                });

        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.finishActivity();
            }
        };
        onSearchClicked = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                FilterData filterData = new FilterData();
                filterData.setKeywords(searchQuery.get());
                data.putSerializable(Constants.classFilterData, filterData);
                data.putString(Constants.origin, ClassListViewModel1.ORIGIN_HOME);
                navigator.navigateActivity(ClassListActivity.class, data);
            }
        };

    }
}

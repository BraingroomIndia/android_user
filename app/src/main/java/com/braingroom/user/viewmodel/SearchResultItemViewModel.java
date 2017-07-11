package com.braingroom.user.viewmodel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;

import io.reactivex.functions.Action;

public class SearchResultItemViewModel extends ViewModel {

    @NonNull
    public final String result;

    @Nullable
    public final Action onClicked;

    public SearchResultItemViewModel(@NonNull final SearchResultItem item, @NonNull final Navigator navigator) {
        this.result = item.query + " in " + "<font color='#00aced'>"+item.category+"</font>";
        this.onClicked = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                FilterData filterData = new FilterData();
                filterData.setCategoryId(item.categoryId);
                filterData.setKeywords(item.query);
                data.putSerializable("filterData",filterData);
                data.putString("origin", FilterViewModel.ORIGIN_CATEGORY);
                navigator.navigateActivity(ClassListActivity.class, data);
            }
        };
    }


}

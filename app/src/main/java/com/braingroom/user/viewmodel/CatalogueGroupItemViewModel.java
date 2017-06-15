package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.response.CatalogueGroupResp;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;

import io.reactivex.functions.Action;


public class CatalogueGroupItemViewModel extends ViewModel {

    @NonNull
    public final ObservableField<String> image;

    @NonNull
    public final ObservableField<String> title;

    @NonNull
    public final ObservableField<String> description;

    @NonNull
    public final ObservableField<String> id;

    @NonNull
    public final Action openCatalogueListing;

    public CatalogueGroupItemViewModel(final CatalogueGroupResp.Snippet data, final Navigator navigator) {
        this.title = new ObservableField<>(data.getGroupName());
        this.description = new ObservableField<>(data.getDescription());
        this.id = new ObservableField<>(data.getId());
        this.image = new ObservableField<>("".equals(data.getGroupImage()) ? null : data.getGroupImage());

        openCatalogueListing = new Action() {
            @Override
            public void run() throws Exception {
                Bundle bundleData = new Bundle();
                bundleData.putString("catalogId", data.getId());
                navigator.navigateActivity(ClassListActivity.class, bundleData);
            }
        };

    }
}

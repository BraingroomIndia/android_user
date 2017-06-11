package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.model.response.GroupResp;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.ConnectableObservable;

public class GroupSearchViewModel extends ViewModel {

    public final ObservableBoolean editable = new ObservableBoolean(false);
    public final ObservableField<String> searchQuery = new ObservableField<>("");
    public final ConnectableObservable<List<ViewModel>> results;
    public final List<ViewModel> groupsData;
    public final Action onBackClicked, itemClickAction;

    public GroupSearchViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        groupsData = new ArrayList<>();
        apiService.getGroups().subscribe(new Consumer<GroupResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull GroupResp groupResp) throws Exception {
                for (GroupResp.Snippet snippet : groupResp.getData()) {
                    groupsData.add(new HorizontalIconTextItemViewModel(snippet.getImage(), snippet.getName(), itemClickAction));
                }
                results.connect();
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
                            for (ViewModel viewModel : groupsData) {
                                if(((HorizontalIconTextItemViewModel)viewModel).title.contains(s)){
                                    results.add(viewModel);
                                }
                            }
                        }
                        return results;
                    }
                }).publish();

        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.finishActivity();
            }
        };

        itemClickAction = new Action() {
            @Override
            public void run() throws Exception {
                messageHelper.show("clicked");
            }
        };
    }
}

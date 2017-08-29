package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.SearchSelectListItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;

import static com.braingroom.user.FCMInstanceIdService.TAG;

public class SearchSelectListViewModel extends ViewModel {

    public final Action onSaveClicked, onClearClicked, onOpenClicked;
    public final Observable<List<ViewModel>> searchResults;
    public final ObservableField<String> selectedItemsText = new ObservableField<>("select items");
    public final ObservableField<String> searchQuery = new ObservableField<>("");
    public final ObservableField<String> searchHint = new ObservableField<>();
    public final ObservableField<String> title = new ObservableField<>();
    public final Map<String, Pair<String, String>> dataMap = new HashMap<>();
    public final HashMap<String, Pair<String, String>> selectedDataMap = new HashMap<>();
    private final FragmentHelper fragmentHelper;
    private final MessageHelper messageHelper;

    PublishSubject<SearchSelectListItemViewModel> singleSelect = PublishSubject.create();
    PublishSubject<SearchSelectListItemViewModel> multipleSelect = PublishSubject.create();
    PublishSubject<String> selectClear = PublishSubject.create();
    Observable<HashMap<String, Pair<String, String>>> apiObservable;
    final Consumer<HashMap<String, Pair<String, String>>> saveConsumer;

    public SearchSelectListViewModel(final String title, final MessageHelper messageHelper, final Navigator navigator, String searchHint
            , final boolean isMultipleSelect, final Observable<HashMap<String, Pair<String, String>>> apiObservableArg, final String dependencySelectMessage, final Consumer<HashMap<String, Pair<String, String>>> saveConsumer, final FragmentHelper fragmentHelper) {
        this.searchHint.set(searchHint);
        this.title.set(title);
        this.apiObservable = apiObservableArg;
        this.saveConsumer = saveConsumer;
        this.fragmentHelper = fragmentHelper;
        this.messageHelper = messageHelper;
        //refreshDataMap(this.apiObservable);
        searchResults = FieldUtils.toObservable(searchQuery)
                .map(new Function<String, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(String s) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        final List<String> nameList = new ArrayList<>(dataMap.keySet());
                        for (String name : nameList) {
                            if (s.length() > 0 ? name.toLowerCase().contains(s.toLowerCase()) : true) {
                                results.add(new SearchSelectListItemViewModel(dataMap.get(name).second, name, dataMap.get(name).first
                                        , selectedDataMap.containsKey(name), new MyConsumer<SearchSelectListItemViewModel>() {
                                    @Override
                                    public void accept(@NonNull SearchSelectListItemViewModel viewModel) {
                                        if (viewModel.isSelected.get())
                                            selectedDataMap.remove(viewModel.name);
                                        else {
                                            if (!isMultipleSelect) {
                                                selectedDataMap.clear();
                                                selectedDataMap.put(viewModel.name, new Pair<>(viewModel.id, viewModel.icon));
                                            }
                                        }
                                        selectedItemsText.set(TextUtils.join(" , ", selectedDataMap.keySet()));
                                        if (isMultipleSelect)
                                            multipleSelect.onNext(viewModel);
                                        else singleSelect.onNext(viewModel);
                                    }
                                }, singleSelect, multipleSelect, selectClear));
                            }
                        }
                        return results;
                    }
                });

        onOpenClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (apiObservable == null) {
                    messageHelper.show(dependencySelectMessage);
                    return;
                }
                refreshDataMap(apiObservable);

            }
        };

        onSaveClicked = new Action() {
            @Override
            public void run() throws Exception {
                searchQuery.set("");
                if (saveConsumer != null)
                    saveConsumer.accept(selectedDataMap);
                fragmentHelper.remove(title);
            }
        };

        onClearClicked = new Action() {
            @Override
            public void run() throws Exception {
                selectClear.onNext("");
                selectedDataMap.clear();
                searchQuery.set("");
                selectedItemsText.set("select items");
            }
        };


    }

    public void setSelectedValues(HashMap<String, String> selectedValues) {
        for (String key : selectedValues.keySet()) {
            selectedDataMap.clear();
            selectedDataMap.put(key, new Pair<String, String>(selectedValues.get(key), null));
            searchQuery.set("");
            selectedItemsText.set(TextUtils.join(" , ", selectedDataMap.keySet()));
        }
        if (saveConsumer != null)
            try {
                saveConsumer.accept(selectedDataMap);
            } catch (Exception e) {
                Log.d(TAG, "setSelectedValues : " + e.toString());
            }
    }

    public void clearSelectedValue() {
        selectedDataMap.clear();
        searchQuery.set("");
        selectedItemsText.set("select items");
    }
    public void changeDataSource(final Observable<HashMap<String, Pair<String, String>>> dataSource){
        this.apiObservable=dataSource;
    }

    /*populates dataMap*/
    public void refreshDataMap(final Observable<HashMap<String, Pair<String, String>>> dataSource) {
        dataMap.clear();
        selectedDataMap.clear();
        apiObservable = dataSource;
        if (apiObservable == null) return;
        apiObservable.subscribe(new Consumer<HashMap<String, Pair<String, String>>>() {
            @Override
            public void accept(@NonNull HashMap<String, Pair<String, String>> map) throws Exception {
                if (map.isEmpty()) {
                    messageHelper.show("Not available");
                    fragmentHelper.remove(title.get());
                }
                else {
                fragmentHelper.show(title.get());
                dataMap.putAll(map);
                searchQuery.set("");}
            }
        }, new Consumer<Throwable>()

        {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                Log.d("Search select List VM", "accept: " + throwable.getMessage());
            }
        });
    }

}

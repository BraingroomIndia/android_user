package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Pair;

import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.SearchSelectListItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

public class DynamicSearchSelectListViewModel extends ViewModel {

    public final Action onSaveClicked, onClearClicked, onOpenClicked;
    public final Observable<List<ViewModel>> searchResults;
    public final ObservableField<String> selectedItemsText = new ObservableField<>("select items");
    public final ObservableField<String> searchQuery = new ObservableField<>("");
    public final ObservableField<String> searchHint = new ObservableField<>();
    public final ObservableField<String> title = new ObservableField<>();
    public final Map<String, Pair<Integer, String>> dataMap = new HashMap<>();
    public final HashMap<String, Pair<Integer, String>> selectedDataMap = new HashMap<>();
    public static final String FRAGMENT_TITLE_COLLEGE = "College";
    public static final String FRAGMENT_TITLE_SCHOOL = "Schools";
    public static final String FRAGMENT_TITLE_LEARNER = "Post by";
    public static final String FRAGMENT_TITLE_Vendor = "Posted by";

    PublishSubject<SearchSelectListItemViewModel> singleSelect = PublishSubject.create();
    PublishSubject<SearchSelectListItemViewModel> multipleSelect = PublishSubject.create();
    PublishSubject<String> selectClear = PublishSubject.create();
//    Observable<HashMap<String, Pair<String, String>>> apiObservable;

    public DynamicSearchSelectListViewModel(final String title, final MessageHelper messageHelper, final Navigator navigator, String searchHint
            , final boolean isMultipleSelect, final String dependencySelectMessage, final Consumer<HashMap<String, Pair<Integer, String>>> saveConsumer, final FragmentHelper fragmentHelper) {
        this.searchHint.set(searchHint);
        this.title.set(title);
//        this.apiObservable = apiObservableArg;
//        refreshDataMap(this.apiObservable);
        searchResults = FieldUtils.toObservable(searchQuery)
                .debounce(200, TimeUnit.MILLISECONDS)
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(@NonNull String s) throws Exception {
                        return true;
                    }
                }).flatMap(new Function<String, Observable<List<ViewModel>>>() {
                    @Override
                    public Observable<List<ViewModel>> apply(@NonNull String keyword) throws Exception {
                        return requestData(keyword).map(new Function<CommonIdResp, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(@NonNull CommonIdResp resp) throws Exception {
                                HashMap<String, Pair<Integer, String>> resMap = new HashMap<>();
                                for (CommonIdResp.Snippet snippet : resp.getData()) {
                                    resMap.put(snippet.getTextValue(), new Pair<Integer, String>(snippet.getId(), null));
                                }
                                dataMap.clear();
                                dataMap.putAll(resMap);
                                List<ViewModel> results = new ArrayList<>();
                                final List<String> nameList = new ArrayList<>(dataMap.keySet());
                                for (String name : nameList) {
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
                                return results;
                            }
                        });
                    }
                });

        onOpenClicked = new Action() {
            @Override
            public void run() throws Exception {
//                if (apiObservable == null) {
//                    messageHelper.show(dependencySelectMessage);
//                    return;
//                }
                fragmentHelper.show(title);
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

    private Observable<CommonIdResp> requestData(String keyword) {
        if (FRAGMENT_TITLE_COLLEGE.equals(title.get()))
            return apiService.getInstitute(keyword);
        if (FRAGMENT_TITLE_SCHOOL.equalsIgnoreCase(title.get()))
            return apiService.getSchools(keyword);
        if (FRAGMENT_TITLE_LEARNER.equals(title.get()))
            return apiService.getLearner(keyword);
        if (FRAGMENT_TITLE_Vendor.equals(title.get()))
            return apiService.geTutor(keyword).mergeWith(apiService.getLearner(keyword));
        return null;
    }

    public void setSelectedValues(HashMap<String, Integer> selectedValues) {
        for (String key : selectedValues.keySet()) {
            selectedDataMap.clear();
            selectedDataMap.put(key, new Pair<Integer, String>(selectedValues.get(key), null));
            searchQuery.set("");
            selectedItemsText.set(TextUtils.join(" , ", selectedDataMap.keySet()));
        }
    }

    /*populates dataMap*/
//    public void refreshDataMap(final Observable<HashMap<String, Pair<String, String>>> dataSource) {
//        dataMap.clear();
//        selectedDataMap.clear();
//        apiObservable = dataSource;
//        if (apiObservable == null) return;
//        apiObservable.subscribe(new Consumer<HashMap<String, Pair<String, String>>>() {
//            @Override
//            public void accept(@NonNull HashMap<String, Pair<String, String>> map) throws Exception {
//                dataMap.putAll(map);
//                searchQuery.set("");
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@NonNull Throwable throwable) throws Exception {
//                Timber.tag(TAG).d(("Search select List VM", "accept: " + throwable.getMessage());
//            }
//        });
//    }

}

package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.GroupResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.viewmodel.fragment.SearchSelectListViewModel;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ConnectFilterViewModel extends ViewModel {


    public final ObservableField<String> keywords = new ObservableField<>("");
    public final SearchSelectListViewModel categoryVm, segmentsVm, myGroups, allGroups;
    public final Action onBackClicked, onResetClicked, onApplyClicked;
    public Navigator navigator;
    public Consumer<HashMap<String, Pair<String, String>>> categoryConsumer;
    public Consumer<HashMap<String, Pair<String, String>>> myGroupsConsumer;
    private boolean clearFlag = false;
    public Observable<HashMap<String, Pair<String, String>>> segmentsApiObservable, categoryApiObservable, myGroupsApiObservable, allGroupsApiObservable;

    public ConnectFilterViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, FragmentHelper fragmentHelper
    ) {
        this.navigator = navigator;

        categoryConsumer = new Consumer<HashMap<String, Pair<String, String>>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Pair<String, String>> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next().first;
                    segmentsApiObservable = apiService.getSegments(selectedId).map(new Function<SegmentResp, HashMap<String, Pair<String, String>>>() {
                        @Override
                        public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull SegmentResp resp) throws Exception {
                            if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                            HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                            for (SegmentResp.Snippet snippet : resp.getData()) {
                                resMap.put(snippet.getSegmentName(), new Pair<>(snippet.getId(), snippet.getSegmentImage()));
                            }
                            return resMap;
                        }
                    });
                    segmentsVm.refreshDataMap(segmentsApiObservable);
                } else {
                    segmentsVm.refreshDataMap(null);
                }
            }
        };

        categoryApiObservable = apiService.getCategory().map(new Function<CategoryResp, HashMap<String, Pair<String, String>>>() {
            @Override
            public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull CategoryResp resp) throws Exception {
                if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                for (CategoryResp.Snippet snippet : resp.getData()) {
                    resMap.put(snippet.getCategoryName(), new Pair<>(snippet.getId(), snippet.getCategoryImage()));
                }
                return resMap;
            }
        });

        categoryVm = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_CATEGORY, messageHelper, navigator, "search for category", false, categoryApiObservable, "", categoryConsumer, fragmentHelper);

        segmentsVm = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_SEGMENT, messageHelper, navigator, "search for segments", false, segmentsApiObservable, "select a category first", null, fragmentHelper);

        myGroupsApiObservable = apiService.getGroups().map(new Function<GroupResp, HashMap<String, Pair<String, String>>>() {
            @Override
            public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull GroupResp resp) throws Exception {
                if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                for (GroupResp.Snippet snippet : resp.getData()) {
                    resMap.put(snippet.getName(), new Pair<String, String>(snippet.getId(), snippet.getImage()));
                }
                return resMap;
            }
        });
        myGroups = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_MY_GROUPS, messageHelper, navigator, "search for your groups", false, myGroupsApiObservable, "select a city first", null, fragmentHelper);

        allGroupsApiObservable = apiService.getGroups().map(new Function<GroupResp, HashMap<String, Pair<String, String>>>() {
            @Override
            public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull GroupResp resp) throws Exception {
                if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                for (GroupResp.Snippet snippet : resp.getData()) {
                    resMap.put(snippet.getName(), new Pair<String, String>(snippet.getId(), snippet.getImage()));
                }
                return resMap;
            }
        });
        allGroups = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_ALL_GROUPS, messageHelper, navigator, "search for groups", false, allGroupsApiObservable, "select a city first", null, fragmentHelper);

        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.finishActivity();
            }
        };
        onResetClicked = new Action() {
            @Override
            public void run() throws Exception {
                reset();
            }
        };
        onApplyClicked = new Action() {
            @Override
            public void run() throws Exception {
                apply();
            }
        };

    }


    public void reset() {
       /* HashMap<String,String> emptyHashMap= new HashMap<>();
        emptyHashMap.put("","");*/
        keywords.set("");
        categoryVm.refreshDataMap(categoryApiObservable);
        segmentsVm.refreshDataMap(null);
        myGroups.refreshDataMap(myGroupsApiObservable);
        allGroups.refreshDataMap(allGroupsApiObservable);
    }

    private HashMap<String, String> getHashMap(HashMap<String, Pair<String, String>> source) {
        HashMap<String, String> resultMap = new HashMap<>();
        for (String key : source.keySet()) {
            resultMap.put(key, source.get(key).first);
        }
        return resultMap;
    }

    public void apply() {
    }
}

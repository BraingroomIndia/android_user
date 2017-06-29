package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.GroupResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.SignupActivity;
import com.braingroom.user.viewmodel.fragment.DynamicSearchSelectListViewModel;
import com.braingroom.user.viewmodel.fragment.SearchSelectListViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ConnectFilterViewModel extends ViewModel {


    public final ObservableField<String> keywords = new ObservableField<>("");
    public final SearchSelectListViewModel categoryVm, segmentsVm, myGroups, allGroups, countryVm, stateVm, cityVm, localityVM;
    public final DynamicSearchSelectListViewModel instituteVm;
    public final Action onBackClicked, onResetClicked, onApplyClicked;
    public Navigator navigator;
    public Consumer<HashMap<String, Pair<String, String>>> categoryConsumer, countryConsumer, stateConsumer, cityConsumer;
    public Consumer<HashMap<String, Pair<String, String>>> myGroupsConsumer, allGroupsConsumer;
    private boolean clearFlag = false;
    public Observable<HashMap<String, Pair<String, String>>> segmentsApiObservable, categoryApiObservable, myGroupsApiObservable,
            allGroupsApiObservable,countryApiObservable, stateApiObservable, cityApiObservable, localityApiObservable;
    ConnectUiHelper uiHelper;
    FragmentHelper fragmentHelper;
    public final ObservableBoolean isLearnerForum;

    public ConnectFilterViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, FragmentHelper fragmentHelper, ConnectUiHelper uiHelper
    ) {
        this.isLearnerForum = new ObservableBoolean(true);
        this.navigator = navigator;
        this.uiHelper = uiHelper;
        this.fragmentHelper = fragmentHelper;

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
                    segmentsVm.changeDataSource(segmentsApiObservable);
                } else {
                    segmentsVm.changeDataSource(null);
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
                    resMap.put(snippet.getName(), new Pair<>(snippet.getId(), snippet.getImage()));
                }
                return resMap;
            }
        });

        myGroupsConsumer = new Consumer<HashMap<String, Pair<String, String>>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Pair<String, String>> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    allGroups.clearSelectedValue();
                }
            }
        };

        myGroups = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_MY_GROUPS, messageHelper, navigator, "search for your groups", false, myGroupsApiObservable, "", myGroupsConsumer, fragmentHelper);

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

        allGroupsConsumer = new Consumer<HashMap<String, Pair<String, String>>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Pair<String, String>> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    myGroups.clearSelectedValue();
                }
            }
        };
        countryApiObservable = apiService.getCountry().map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
            @Override
            public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    resMap.put(snippet.getTextValue(), new Pair<String, String>(snippet.getId(), null));
                }
                return resMap;
            }
        });

        countryConsumer = new Consumer<HashMap<String, Pair<String, String>>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Pair<String, String>> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next().first;
                    stateApiObservable = apiService.getState(selectedId).map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
                        @Override
                        public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                            if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                            HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                            for (CommonIdResp.Snippet snippet : resp.getData()) {
                                resMap.put(snippet.getTextValue(), new Pair<String, String>(snippet.getId(), null));
                            }
                            return resMap;
                        }
                    });
                    stateVm.clearSelectedValue();
                    cityVm.clearSelectedValue();
                    localityVM.clearSelectedValue();
                    //TODO
                    //signUpSnippet.setCountry(selectedId);
                    stateVm.changeDataSource(stateApiObservable);
                    cityVm.changeDataSource(null);
                } else {
                    stateVm.clearSelectedValue();
                    cityVm.clearSelectedValue();
                    localityVM.clearSelectedValue();
                    stateVm.changeDataSource(null);
                    cityVm.changeDataSource(null);
                }
            }
        };

        stateConsumer = new Consumer<HashMap<String, Pair<String, String>>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Pair<String, String>> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next().first;
                    cityApiObservable = apiService.getCityList(selectedId).map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
                        @Override
                        public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                            if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                            HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                            for (CommonIdResp.Snippet snippet : resp.getData()) {
                                resMap.put(snippet.getTextValue(), new Pair<String, String>(snippet.getId(), null));
                            }
                            return resMap;
                        }
                    });
                    //signUpSnippet.setState(selectedId);
                    cityVm.clearSelectedValue();
                    localityVM.clearSelectedValue();
                    cityVm.changeDataSource(cityApiObservable);
                    localityVM.changeDataSource(null);
                } else {
                    cityVm.clearSelectedValue();
                    localityVM.clearSelectedValue();
                    cityVm.changeDataSource(null);
                    localityVM.changeDataSource(null);
                }
            }
        };


        cityConsumer = new Consumer<HashMap<String, Pair<String, String>>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Pair<String, String>> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next().first;
                    localityApiObservable = apiService.getLocalityList(selectedId).map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
                        @Override
                        public HashMap<String, Pair<String, String>> apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                            if ("0".equals(resp.getResCode())) messageHelper.show(resp.getResMsg());
                            HashMap<String, Pair<String, String>> resMap = new HashMap<>();
                            for (CommonIdResp.Snippet snippet : resp.getData()) {
                                resMap.put(snippet.getTextValue(), new Pair<String, String>(snippet.getId(), null));
                            }
                            return resMap;
                        }
                    });
                    //signUpSnippet.setCityId(selectedId);
                    localityVM.clearSelectedValue();
                    localityVM.changeDataSource(localityApiObservable);
                } else {
                    localityVM.clearSelectedValue();
                    localityVM.changeDataSource(null);
                }
            }
        };


        countryVm = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_COUNTRY, messageHelper, navigator, "search for country", false, countryApiObservable, "", countryConsumer, fragmentHelper);

        stateVm = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_STATE, messageHelper, navigator, "search for state", false, stateApiObservable, "select a country first", stateConsumer, fragmentHelper);

        cityVm = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_CITY, messageHelper, navigator, "search for cities", false, cityApiObservable, "select a state first", cityConsumer, fragmentHelper);


        localityVM = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_LOCALITY, messageHelper, navigator, "search for localities", false, localityApiObservable, "select a city first", null, fragmentHelper);

        instituteVm = new DynamicSearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_COLLEGE, messageHelper, navigator, "search for institutes... ", false, "", null, fragmentHelper);


        allGroups = new SearchSelectListViewModel(ConnectHomeActivity.FRAGMENT_TITLE_ALL_GROUPS, messageHelper, navigator, "search for groups", false, allGroupsApiObservable, "", allGroupsConsumer, fragmentHelper);

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
        categoryVm.clearSelectedValue();
        segmentsVm.changeDataSource(null);
        segmentsVm.clearSelectedValue();
        myGroups.clearSelectedValue();
        allGroups.clearSelectedValue();
    }

    private HashMap<String, String> getHashMap(HashMap<String, Pair<String, String>> source) {
        HashMap<String, String> resultMap = new HashMap<>();
        for (String key : source.keySet()) {
            resultMap.put(key, source.get(key).first);
        }
        return resultMap;
    }

    public void apply() {
        String keyword = keywords.get();
        String categoryId = "";
        if (!categoryVm.selectedDataMap.isEmpty())
            categoryId = categoryVm.selectedDataMap.values().iterator().next().first;

        String segmentId = "";
        if (!segmentsVm.selectedDataMap.isEmpty())
            segmentId = segmentsVm.selectedDataMap.values().iterator().next().first;

        String myGroupId = "";
        if (!myGroups.selectedDataMap.isEmpty())
            myGroupId = myGroups.selectedDataMap.values().iterator().next().first;

        String allGroupId = "";
        if (!allGroups.selectedDataMap.isEmpty())
            allGroupId = allGroups.selectedDataMap.values().iterator().next().first;

        String countryId="";

        String stateId="";

        String cityId ="";

        String localityId="";
        if (!countryVm.selectedDataMap.isEmpty())
        {
            countryId=countryVm.selectedDataMap.values().iterator().next().first;
            if (!stateVm.selectedDataMap.isEmpty()){
                stateId=stateVm.selectedDataMap.values().iterator().next().first;
                if (!cityVm.selectedDataMap.isEmpty())
                {
                    cityId=cityVm.selectedDataMap.values().iterator().next().first;
                    if (!localityVM.selectedDataMap.isEmpty())
                        localityId=localityVM.selectedDataMap.values().iterator().next().first;
                }
            }
        }
        List<String> location= new ArrayList<>();
        location.add(countryId);
        location.add(stateId);
        location.add(cityId);
        location.add(localityId);
        this.uiHelper.setFilterData(keyword, categoryId, segmentId, myGroupId, allGroupId,location);
        uiHelper.popFragment();
    }
}

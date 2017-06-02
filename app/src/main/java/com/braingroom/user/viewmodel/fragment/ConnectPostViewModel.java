package com.braingroom.user.viewmodel.fragment;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.GroupResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.DataItemViewModel;
import com.braingroom.user.viewmodel.DatePickerViewModel;
import com.braingroom.user.viewmodel.ImageUploadViewModel;
import com.braingroom.user.viewmodel.ListDialogViewModel1;
import com.braingroom.user.viewmodel.PostApiImageUploadViewModel;
import com.braingroom.user.viewmodel.PostApiVideoUploadViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static lombok.libs.org.objectweb.asm.commons.GeneratorAdapter.AND;

public class ConnectPostViewModel extends ViewModel {

    private String TAG = getClass().getCanonicalName();

    public static final int POST_TYPE_KNOWLEDGE_NUGGETS = 1;
    public static final int POST_TYPE_BUY_N_SELL = 2;
    public static final int POST_TYPE_LEARNING_PARTNERS = 3;
    public static final int POST_TYPE_DISCUSS_AND_DECIDE = 4;
    public static final int PRIVACY_TYPE_PRIVATE = -1;
    public static final int PRIVACY_TYPE_GROUP = 1;
    public static final int PRIVACY_TYPE_ALL = 2;

    public final HelperFactory helperFactory;


    public final DataItemViewModel title, youtubeAddress, classPageUrl, proposedTime, requestNote;
    public final PostApiImageUploadViewModel imageUploadVm;
    public final PostApiVideoUploadViewModel videoUploadVm;
    public final DatePickerViewModel firstDateVm, secondDateVm;
    public final ObservableField<String> description;
    public final ListDialogViewModel1 postTypeVm, groupVm, countryVm, stateVm, cityVm, localityVm, privacyVm, activityVm, categoryVm, segmentsVm;
    public final Action onSubmitClicked, changeDateText;
    public Navigator navigator;
    private String postType;
    public Consumer<HashMap<String, Integer>> countryConsumer, stateConsumer, cityConsumer, postConsumer, groupConsumer, categoryConsumer;


    public final ObservableBoolean videoField = new ObservableBoolean(true);
    public final ObservableBoolean isRecurring = new ObservableBoolean(false);
    public final ObservableBoolean imageField = new ObservableBoolean(true);
    public final ObservableBoolean classField = new ObservableBoolean(true);
    public final ObservableBoolean dateField = new ObservableBoolean(false);
    public final ObservableBoolean activityField = new ObservableBoolean(false);
    public final ObservableBoolean groupsField = new ObservableBoolean(true);
    public final ObservableBoolean proposedTimeField = new ObservableBoolean(false);
    public final ObservableBoolean categoryField = new ObservableBoolean(false);


    public ConnectPostViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull final HelperFactory helperFactory, final String postType) {
        this.navigator = navigator;
        this.helperFactory = helperFactory;
        this.postType = postType;
        title = new DataItemViewModel("");
        description = new ObservableField<>("");
        youtubeAddress = new DataItemViewModel("");
        classPageUrl = new DataItemViewModel("");
        proposedTime = new DataItemViewModel("");
        requestNote = new DataItemViewModel("");
        firstDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "On", "choose");
        secondDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "To", "choose");
        imageUploadVm = new PostApiImageUploadViewModel(messageHelper, navigator, R.drawable.image_placeholder, null);
        videoUploadVm = new PostApiVideoUploadViewModel(messageHelper, navigator, R.drawable.video_placeholder, null);

        LinkedHashMap<String, Integer> postTypeApiData = new LinkedHashMap<>();

        LinkedHashMap<String, Integer> postTypeLearnerApiData = new LinkedHashMap<>();


        postTypeLearnerApiData.put("Knowledge nuggets", POST_TYPE_KNOWLEDGE_NUGGETS);
        postTypeLearnerApiData.put("Buy & sell", POST_TYPE_BUY_N_SELL);
        postTypeLearnerApiData.put("Find learning partners", POST_TYPE_LEARNING_PARTNERS);

        LinkedHashMap<String, Integer> privacyTypeApiData = new LinkedHashMap<>();
        privacyTypeApiData.put("Privacy", PRIVACY_TYPE_PRIVATE);
        privacyTypeApiData.put("Group", PRIVACY_TYPE_GROUP);
        privacyTypeApiData.put("All", PRIVACY_TYPE_ALL);

        LinkedHashMap<String, Integer> postTypeTutorApiData = new LinkedHashMap<>();
        postTypeTutorApiData.put("Discuss and Decide", POST_TYPE_DISCUSS_AND_DECIDE);

        postTypeApiData = postTypeLearnerApiData;

        HashMap<String, Integer> mSelectedPostType = new HashMap<String, Integer>();
        if ("action_tips_tricks".equalsIgnoreCase(postType))
            mSelectedPostType.put("Knowledge nuggets", POST_TYPE_KNOWLEDGE_NUGGETS);
        else if ("action_buy_sell".equalsIgnoreCase(postType))
            mSelectedPostType.put("Buy & sell", POST_TYPE_BUY_N_SELL);
        else if ("action_find_partners".equalsIgnoreCase(postType))
            mSelectedPostType.put("Find learning partners", POST_TYPE_LEARNING_PARTNERS);
        else if ("action_tutors_article".equalsIgnoreCase(postType)) {
            postTypeApiData = postTypeTutorApiData;
            mSelectedPostType.put("Discuss and Decide", POST_TYPE_DISCUSS_AND_DECIDE);
        } else if ("action_discuss_n_decide".equalsIgnoreCase(postType)) {
            postTypeApiData = postTypeTutorApiData;
            mSelectedPostType.put("Discuss and Decide", POST_TYPE_DISCUSS_AND_DECIDE);
        }
        if (isRecurring.get())


            Log.d(TAG, "postType: " + postType);


        postConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext())
                    switch (selectedMap.values().iterator().next()) {
                        case POST_TYPE_KNOWLEDGE_NUGGETS:
                            videoField.set(true);
                            imageField.set(true);
                            classField.set(true);
                            groupsField.set(true);
                            dateField.set(false);
                            proposedTimeField.set(false);
                            activityField.set(false);
                            categoryField.set(false);
                            break;
                        case POST_TYPE_BUY_N_SELL:
                            imageField.set(true);
                            groupsField.set(true);
                            videoField.set(false);
                            classField.set(false);
                            proposedTimeField.set(false);
                            activityField.set(false);
                            categoryField.set(false);
                            break;
                        case POST_TYPE_LEARNING_PARTNERS:
                            groupsField.set(true);
                            proposedTimeField.set(true);
                            activityField.set(true);
                            imageField.set(false);
                            videoField.set(false);
                            classField.set(false);
                            categoryField.set(false);
                            break;
                        case POST_TYPE_DISCUSS_AND_DECIDE:
                            imageField.set(true);
                            categoryField.set(true);
                            videoField.set(false);
                            classField.set(false);
                            groupsField.set(false);
                            proposedTimeField.set(false);
                            activityField.set(false);
                            break;
                        default:
                            break;


                    }

            }
        };

        try {
            postConsumer.accept(mSelectedPostType);
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        privacyVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Privacy", messageHelper
                , Observable.just(new ListDialogData1(privacyTypeApiData))
                , new HashMap<String, Integer>(), false, null);

        postTypeVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Post type", messageHelper
                , Observable.just(new ListDialogData1(postTypeApiData))
                , mSelectedPostType, false, postConsumer);

        countryConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    stateVm.reInit(getStateApiObservable(selectedId));
                }
            }
        };
        countryVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Country", messageHelper, apiService.getCountry().map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), false, countryConsumer);

        stateConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    cityVm.reInit(getCityApiObservable(selectedId));
                }
            }
        };

        cityConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    localityVm.reInit(getLocalityApiObservable(selectedId));
                }
            }
        };

        stateVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "State", messageHelper, getStateApiObservable("-1"), new HashMap<String, Integer>(), false, stateConsumer);
        cityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "City", messageHelper, getCityApiObservable("-1"), new HashMap<String, Integer>(), false, cityConsumer);
        localityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Localities", messageHelper, getLocalityApiObservable("-1"), new HashMap<String, Integer>(), false, null);


        activityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Activity", messageHelper, getgetGroupActivitiesApiObservable("-1")
                , new HashMap<String, Integer>(), false, null);

        groupConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                String selectedIds = selectedMap.values().toString();
                selectedIds = selectedIds.substring(1, selectedIds.length() - 1);
                activityVm.reInit(getgetGroupActivitiesApiObservable(selectedIds));
                Log.d(TAG, "accept: " + selectedIds);

            }
        };


        groupVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Groups", messageHelper, apiService.getGroups().map(new Function<GroupResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull GroupResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (GroupResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getName(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), true, groupConsumer);

        categoryConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    List<Integer> selectedId = new ArrayList(selectedMap.values());
                    segmentsVm.reInit(getSegmentsApiObservable(selectedId));
                }
            }
        };
        categoryVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Category", messageHelper, apiService.getCategory()
                .map(new Function<CategoryResp, ListDialogData1>() {
                    @Override
                    public ListDialogData1 apply(@io.reactivex.annotations.NonNull CategoryResp categoryResp) throws Exception {
                        LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                        for (CategoryResp.Snippet snippet : categoryResp.getData()) {
                            itemMap.put(snippet.getCategoryName(), Integer.parseInt(snippet.getId()));
                        }
                        // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                        return new ListDialogData1(itemMap);
                    }
                }), new HashMap<String, Integer>(), true, categoryConsumer);


        segmentsVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Segments", messageHelper, getSegmentsApiObservable(new ArrayList<Integer>(0)), new HashMap<String, Integer>(), false, null);


        onSubmitClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (postTypeVm.selectedItemsMap.values().iterator().next() == POST_TYPE_BUY_N_SELL) {

                }
//
//
// uiHelper.next();
            }
        };
        changeDateText = new Action() {
            @Override
            public void run() throws Exception {
                if (isRecurring.get())
                    firstDateVm.setTitle("From");
                else
                    firstDateVm.setTitle("On");

            }
        };

    }

    private Observable<ListDialogData1> getgetGroupActivitiesApiObservable(String groupIds) {
        return apiService.getGroupActivities(groupIds).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                return new ListDialogData1(itemMap);
            }
        });
    }

    private Observable<ListDialogData1> getStateApiObservable(String countryId) {
        return apiService.getState(countryId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        });
    }

    private Observable<ListDialogData1> getLocalityApiObservable(String cityId) {
        return apiService.getLocalityList(cityId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        });
    }

    private Observable<ListDialogData1> getCityApiObservable(String cityId) {
        return apiService.getCityList(cityId).map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        });
    }

    private Observable<ListDialogData1> getSegmentsApiObservable(List<Integer> categoryId) {
        return apiService.getSegmentTree(categoryId).map(new Function<SegmentResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull SegmentResp segmentsResp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (SegmentResp.Snippet snippet : segmentsResp.getData()) {
                    itemMap.put(snippet.getSegmentName(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        });

    }
}

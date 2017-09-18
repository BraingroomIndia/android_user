package com.braingroom.user.viewmodel.fragment;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.request.BuyAndSellPostReq;
import com.braingroom.user.model.request.ConnectPostReq;
import com.braingroom.user.model.request.DecideAndDiscussPostReq;
import com.braingroom.user.model.request.KnowledgeNuggetsPostReq;
import com.braingroom.user.model.request.LearningPartnerPostReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.GroupResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.model.response.UploadResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FileUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.fragment.ConnectPostFragment;
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

import static android.app.Activity.RESULT_OK;
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


    //Request
    private BuyAndSellPostReq.Snippet buyAndSellSnippet;
    private KnowledgeNuggetsPostReq.Snippet knowledgeNuggetsSnippet;
    private LearningPartnerPostReq.Snippet learningPartnerPostSnippet;
    private DecideAndDiscussPostReq.Snippet decideAndDiscussSnippet;


    public final DataItemViewModel title, youtubeAddress, classPageUrl, proposedLocation, proposedTime, requestNote;
    public final PostApiImageUploadViewModel imageUploadVm;
    public final PostApiVideoUploadViewModel videoUploadVm;
    public final DatePickerViewModel firstDateVm, secondDateVm;
    public final ObservableField<String> description;
    public final ListDialogViewModel1 postTypeVm, groupVm, countryVm, stateVm, cityVm, localityVm, privacyVm, activityVm, categoryVm, segmentsVm;
    public final Action onSubmitClicked, changeDateText;
    public Navigator navigator;
    public MessageHelper messageHelper;
    private String postType;
    public Consumer<HashMap<String, Integer>> countryConsumer, stateConsumer, cityConsumer, localityConsumer, postConsumer, groupConsumer, categoryConsumer;


    HashMap<String, Integer> mSelectedPostType = new HashMap<String, Integer>();
    public final ObservableBoolean videoField = new ObservableBoolean(true);
    public final ObservableBoolean isRecurring = new ObservableBoolean(false);
    public final ObservableBoolean imageField = new ObservableBoolean(true);
    public final ObservableBoolean classField = new ObservableBoolean(true);
    public final ObservableBoolean dateField = new ObservableBoolean(false);
    public final ObservableBoolean activityField = new ObservableBoolean(false);
    public final ObservableBoolean groupsField = new ObservableBoolean(true);
    public final ObservableBoolean proposedTimeField = new ObservableBoolean(false);
    public final ObservableBoolean categoryField = new ObservableBoolean(false);

    public ConnectPostViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull final HelperFactory helperFactory,
                                final String postType1, final ConnectPostFragment.UiHelper uiHelper) {
        this.navigator = navigator;
        this.helperFactory = helperFactory;
        this.messageHelper = messageHelper;
        this.postType = postType1;
        title = new DataItemViewModel("");
        description = new ObservableField<>("");
        youtubeAddress = new DataItemViewModel("");
        classPageUrl = new DataItemViewModel("");
        proposedLocation = new DataItemViewModel("");
        proposedTime = new DataItemViewModel("");
        requestNote = new DataItemViewModel("");
        firstDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "On", "choose");
        secondDateVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "To", "choose");
        imageUploadVm = new PostApiImageUploadViewModel(messageHelper, navigator, R.drawable.image_placeholder, null);
        videoUploadVm = new PostApiVideoUploadViewModel(messageHelper, navigator, R.drawable.video_placeholder, null);

        if (postType1.equalsIgnoreCase("action_tips_tricks"))
            showKNN();
        if (postType1.equalsIgnoreCase("action_buy_sell"))
            showBNS();
        if (postType1.equalsIgnoreCase("action_find_partners"))
            showAP();
        if (postType1.equalsIgnoreCase("action_discuss_n_decide"))
            showDND();

        //request
        buyAndSellSnippet = new BuyAndSellPostReq.Snippet();
        knowledgeNuggetsSnippet = new KnowledgeNuggetsPostReq.Snippet();
        learningPartnerPostSnippet = new LearningPartnerPostReq.Snippet();
        decideAndDiscussSnippet = new DecideAndDiscussPostReq.Snippet();

        LinkedHashMap<String, Integer> postTypeApiData;

        LinkedHashMap<String, Integer> postTypeLearnerApiData = new LinkedHashMap<>();


        postTypeLearnerApiData.put("Knowledge nuggets", POST_TYPE_KNOWLEDGE_NUGGETS);
        postTypeLearnerApiData.put("Buy & sell", POST_TYPE_BUY_N_SELL);
        postTypeLearnerApiData.put("Find learning partners", POST_TYPE_LEARNING_PARTNERS);
        postTypeLearnerApiData.put("Discuss and Decide", POST_TYPE_DISCUSS_AND_DECIDE);

        LinkedHashMap<String, Integer> privacyTypeApiData = new LinkedHashMap<>();
        privacyTypeApiData.put("Group", PRIVACY_TYPE_GROUP);
        privacyTypeApiData.put("All", PRIVACY_TYPE_ALL);


        postTypeApiData = postTypeLearnerApiData;

        mSelectedPostType = new HashMap<String, Integer>();
        if ("action_tips_tricks".equalsIgnoreCase(postType1)) {
            mSelectedPostType.put("Knowledge nuggets", POST_TYPE_KNOWLEDGE_NUGGETS);
            showKNN();
        } else if ("action_buy_sell".equalsIgnoreCase(postType1)) {
            mSelectedPostType.put("Buy & sell", POST_TYPE_BUY_N_SELL);
            showBNS();
        } else if ("action_find_partners".equalsIgnoreCase(postType1)) {
            mSelectedPostType.put("Find learning partners", POST_TYPE_LEARNING_PARTNERS);
            showAP();
        } else if ("action_discuss_n_decide".equalsIgnoreCase(postType1)) {
            mSelectedPostType.put("Discuss and Decide", POST_TYPE_DISCUSS_AND_DECIDE);
            showDND();
        }


        Log.d(TAG, "postType: " + postType);


        postConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                imageUploadVm.remoteAddress.set(null);
                videoUploadVm.remoteAddress.set(null);
                videoUploadVm.thumbUrl.set(null);
                if (selectedMap.values().iterator().hasNext())
                    switch (selectedMap.values().iterator().next()) {

                        case POST_TYPE_KNOWLEDGE_NUGGETS:
                            showKNN();
                            break;
                        case POST_TYPE_BUY_N_SELL:
                            showBNS();
                            break;
                        case POST_TYPE_LEARNING_PARTNERS:
                            showAP();
                            break;
                        case POST_TYPE_DISCUSS_AND_DECIDE:
                            showDND();
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
                , new HashMap<String, Integer>(), false, null, "");

        postTypeVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Post type", messageHelper
                , Observable.just(new ListDialogData1(postTypeApiData))
                , mSelectedPostType, false, postConsumer, "");


        //  postTypeVm.setSelectedItemsMap(mSelectedPostType);
        countryConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    setCountry(selectedId);
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
        }), new HashMap<String, Integer>(), false, countryConsumer, "");

        stateConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    setState(selectedId);
                    cityVm.reInit(getCityApiObservable(selectedId));
                }
            }
        };

        cityConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    setCity(selectedId);
                    localityVm.reInit(getLocalityApiObservable(selectedId));
                }
            }
        };
        localityConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    setLocality(selectedId);
                }
            }
        };
        groupConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedIds = android.text.TextUtils.join(",", new ArrayList(selectedMap.values()));
                    setGroup(selectedIds);
                    activityVm.reInit(getgetGroupActivitiesApiObservable(selectedIds));
                    Log.d(TAG, "accept: " + selectedIds);
                }

            }
        };
        categoryConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = android.text.TextUtils.join(",", new ArrayList(selectedMap.values()));
                    setCategory(selectedId);
                    segmentsVm.reInit(getSegmentsApiObservable(selectedId));
                }
            }
        };

        stateVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "State", messageHelper, getStateApiObservable("-1"), new HashMap<String, Integer>(), false, stateConsumer, "select a country first");
        cityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "City", messageHelper, getCityApiObservable("-1"), new HashMap<String, Integer>(), false, cityConsumer, "select a state first");
        localityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Localities", messageHelper, getLocalityApiObservable("-1"), new HashMap<String, Integer>(), false, localityConsumer, "select a city first");


        activityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Activity", messageHelper, getgetGroupActivitiesApiObservable("-1")
                , new HashMap<String, Integer>(), false, null, "select a group first");


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
        }), new HashMap<String, Integer>(), true, groupConsumer, "");

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
                }), new HashMap<String, Integer>(), true, categoryConsumer, "");


        segmentsVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Segments", messageHelper, getSegmentsApiObservable("-1"), new HashMap<String, Integer>(), false, null, "select a category first");


        onSubmitClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (postTypeVm.selectedItemsMap.isEmpty()) {
                    messageHelper.show("Select a post Type ");
                    return;
                }
                if (postTypeVm.selectedItemsMap.values().iterator().next() == POST_TYPE_BUY_N_SELL) {
                    if (!groupVm.selectedItemsMap.values().iterator().hasNext()) {
                        messageHelper.show("Please select a Group");
                        return;
                    }
                    if (title.s_1.get().equals("")) {
                        messageHelper.show("Please enter Post Title");
                        return;
                    }
                    if (description.get().equals("")) {
                        messageHelper.show("Please enter Description");
                        return;
                    }
                    buyAndSellSnippet.setGroupId(android.text.TextUtils.join(",", groupVm.getSelectedItemsId()));
                    buyAndSellSnippet.setUuid(pref.getString(Constants.UUID, ""));
                    buyAndSellSnippet.setPostType("group_post");
                    buyAndSellSnippet.setPostTitle(title.s_1.get());
                    buyAndSellSnippet.setPostSummary(description.get());
                    buyAndSellSnippet.setPostThumbUpload(imageUploadVm.remoteAddress.get());
                    apiService.postBuyAndSell(buyAndSellSnippet).subscribe(new Consumer<BaseResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                            messageHelper.show(baseResp.getResMsg());
                            uiHelper.next();

                        }
                    });

                } else if (postTypeVm.selectedItemsMap.values().iterator().next() == POST_TYPE_KNOWLEDGE_NUGGETS) {
                    if (!groupVm.selectedItemsMap.values().iterator().hasNext()) {
                        messageHelper.show("Please Select a Group");
                        return;
                    }
                    if (title.s_1.get().equals("")) {
                        messageHelper.show("Please enter Post Title");
                        return;
                    }
                    if (description.get().equals("")) {
                        messageHelper.show("Please enter Description");
                        return;
                    }
                    knowledgeNuggetsSnippet.setGroupId(android.text.TextUtils.join(",", groupVm.getSelectedItemsId()));
                    knowledgeNuggetsSnippet.setUuid(pref.getString(Constants.UUID, ""));
                    knowledgeNuggetsSnippet.setPostType("tips_tricks");
                    knowledgeNuggetsSnippet.setPostTitle(title.s_1.get());
                    knowledgeNuggetsSnippet.setPostSummary(description.get());
                    knowledgeNuggetsSnippet.setPostThumbUpload(imageUploadVm.remoteAddress.get());
                    knowledgeNuggetsSnippet.setVideo(videoUploadVm.remoteAddress.get());
                    knowledgeNuggetsSnippet.setYoutubeUrl(youtubeAddress.s_1.get());
                    knowledgeNuggetsSnippet.setClassLink(classPageUrl.s_1.get());
                    knowledgeNuggetsSnippet.setPostThumbUpload(imageUploadVm.remoteAddress.get());
                    knowledgeNuggetsSnippet.setVideo(videoUploadVm.remoteAddress.get());
                    apiService.postKnowledgeNuggets(knowledgeNuggetsSnippet).subscribe(new Consumer<BaseResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                            messageHelper.show(baseResp.getResMsg());
                            uiHelper.next();

                        }
                    });
                } else if (postTypeVm.selectedItemsMap.values().iterator().next() == POST_TYPE_LEARNING_PARTNERS) {
                    if (!groupVm.selectedItemsMap.values().iterator().hasNext()) {
                        messageHelper.show("Please select a Group");
                        return;
                    }
                    if (!activityVm.selectedItemsMap.values().iterator().hasNext()) {
                        messageHelper.show("Please select a Activity");
                        return;
                    }
                    if (title.s_1.get().equals("")) {
                        messageHelper.show("Please enter Post Title");
                        return;
                    }
                    if (description.get().equals("")) {
                        messageHelper.show("Please enter Description");
                        return;
                    }
                    if (proposedLocation.s_1.get().equals("")) {
                        messageHelper.show("Please enter Proposed Location ");
                        return;
                    }
                    learningPartnerPostSnippet.setUuid(pref.getString(Constants.UUID, ""));
                    learningPartnerPostSnippet.setPostType("activity_request");
                    learningPartnerPostSnippet.setGroupId(android.text.TextUtils.join(",", groupVm.getSelectedItemsId()));
                    learningPartnerPostSnippet.setActivityType(android.text.TextUtils.join(",", activityVm.getSelectedItemsId()));
                    learningPartnerPostSnippet.setPostTitle(title.s_1.get());
                    learningPartnerPostSnippet.setPostSummary(description.get());
                    learningPartnerPostSnippet.setRequestNote(requestNote.s_1.get());
                    learningPartnerPostSnippet.setProposedLocation(proposedLocation.s_1.get());
                    learningPartnerPostSnippet.setProposedDateType(isRecurring.get() ? "2" : "1");
                    if (isRecurring.get()) {
                        learningPartnerPostSnippet.setProposedDateFrom(firstDateVm.date.get());
                        learningPartnerPostSnippet.setProposedDateTo(secondDateVm.date.get());
                        learningPartnerPostSnippet.setRecurringRequestTime(proposedTime.s_1.get());
                    } else {
                        learningPartnerPostSnippet.setRequestDate(firstDateVm.date.get());
                        learningPartnerPostSnippet.setRequestTime(proposedTime.s_1.get());
                    }
                    if (privacyVm.selectedItemsMap.values().iterator().hasNext())
                        learningPartnerPostSnippet.setPrivacyType(android.text.TextUtils.join("", privacyVm.getSelectedItemsId()));
                    else
                        learningPartnerPostSnippet.setPrivacyType("-1");
                    apiService.postLearningPartner(learningPartnerPostSnippet).subscribe(new Consumer<BaseResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                            messageHelper.show(baseResp.getResMsg());
                            uiHelper.next();

                        }
                    });

                } else if (postTypeVm.selectedItemsMap.values().iterator().next() == POST_TYPE_DISCUSS_AND_DECIDE) {
                    if (!categoryVm.selectedItemsMap.values().iterator().hasNext()) {
                        messageHelper.show("Please select at least one Category");
                        return;
                    }
                    if (!segmentsVm.selectedItemsMap.values().iterator().hasNext()) {
                        messageHelper.show("Please select at least one  segment");
                        return;
                    }
                    if (title.s_1.get().equals("")) {
                        messageHelper.show("Please enter Post title");
                        return;
                    }
                    if (description.get().equals("")) {
                        messageHelper.show("Please enter description");
                        return;
                    }
                    decideAndDiscussSnippet.setUuid(pref.getString(Constants.UUID, ""));
                    decideAndDiscussSnippet.setPostType("user_post");
                    decideAndDiscussSnippet.setCategoryId(android.text.TextUtils.join(",", categoryVm.getSelectedItemsId()));
                    decideAndDiscussSnippet.setSegmentId(android.text.TextUtils.join(",", segmentsVm.getSelectedItemsId()));
                    decideAndDiscussSnippet.setPostTitle(title.s_1.get());
                    decideAndDiscussSnippet.setPostSummary(description.get());
                    decideAndDiscussSnippet.setPostThumbUpload(imageUploadVm.remoteAddress.get());
                    apiService.postDecideDiscuss(decideAndDiscussSnippet).subscribe(new Consumer<BaseResp>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                            messageHelper.show(baseResp.getResMsg());
                            uiHelper.next();

                        }
                    });

                }

//
//
                //UiHelper.next();
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

    private Observable<ListDialogData1> getSegmentsApiObservable(String categoryId) {
        return apiService.getSegments(categoryId).map(new Function<SegmentResp, ListDialogData1>() {
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

    @Override
    public void handleActivityResult(final int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQ_CODE_CHOOSE_IMAGE)
                && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            if (postType != null)
                imageUploadVm.imageUpload(fileUri, postType);
            else messageHelper.show("Select post type ");
            Log.d(TAG, "fileuri: " + fileUri);

        } else if (((requestCode == REQ_CODE_CHOOSE_VIDEO)
                && resultCode == RESULT_OK && data != null && data.getData() != null)) {
            Uri fileUri = data.getData();
            if (postType != null)
                videoUploadVm.uploadVideo(fileUri, postType);
            else messageHelper.show("Select post type ");
            Log.d(TAG, "fileuri: " + fileUri);

        }

    }

    private void setCountry(String id) {
        buyAndSellSnippet.setCountryId(id);
        knowledgeNuggetsSnippet.setCountryId(id);
        decideAndDiscussSnippet.setCountryId(id);
        learningPartnerPostSnippet.setCountryId(id);
        setState("");
        setCity("");
        setLocality("");
    }

    private void setState(String id) {
        buyAndSellSnippet.setStateId(id);
        knowledgeNuggetsSnippet.setStateId(id);
        decideAndDiscussSnippet.setStateId(id);
        learningPartnerPostSnippet.setStateId(id);
        setCity("");
        setLocality("");

    }

    private void setCity(String id) {
        buyAndSellSnippet.setCityId(id);
        knowledgeNuggetsSnippet.setCityId(id);
        decideAndDiscussSnippet.setCityId(id);
        learningPartnerPostSnippet.setCityId(id);
        setLocality("");
    }

    private void setLocality(String id) {
        buyAndSellSnippet.setLocalityId(id);
        knowledgeNuggetsSnippet.setLocalityId(id);
        decideAndDiscussSnippet.setLocalityId(id);
        learningPartnerPostSnippet.setLocalityId(id);

    }

    private void setGroup(String id) {
        buyAndSellSnippet.setGroupId(id);
        learningPartnerPostSnippet.setGroupId(id);
        knowledgeNuggetsSnippet.setGroupId(id);
        learningPartnerPostSnippet.setActivityType(null);
    }

    private void setCategory(String id) {
        decideAndDiscussSnippet.setCategoryId(id);
        decideAndDiscussSnippet.setSegmentId(null);
    }

    public void showKNN() {
        videoField.set(true);
        postType = "tips_tricks";
        imageField.set(true);
        classField.set(true);
        groupsField.set(true);
        dateField.set(false);
        proposedTimeField.set(false);
        activityField.set(false);
        categoryField.set(false);
    }

    public void showBNS() {
        imageField.set(true);
        postType = "group_post";
        groupsField.set(true);
        videoField.set(false);
        classField.set(false);
        proposedTimeField.set(false);
        activityField.set(false);
        categoryField.set(false);
    }

    public void showAP() {
        groupsField.set(true);
        postType = "activity_request";
        proposedTimeField.set(true);
        activityField.set(true);
        imageField.set(false);
        videoField.set(false);
        classField.set(false);
        categoryField.set(false);
    }

    public void showDND() {
        imageField.set(true);
        postType = "user_post";
        categoryField.set(true);
        videoField.set(false);
        classField.set(false);
        groupsField.set(false);
        proposedTimeField.set(false);
        activityField.set(false);
    }
}

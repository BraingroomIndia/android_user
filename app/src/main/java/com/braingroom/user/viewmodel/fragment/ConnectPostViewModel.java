package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.GroupResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.DataItemViewModel;
import com.braingroom.user.viewmodel.ImageUploadViewModel;
import com.braingroom.user.viewmodel.ListDialogViewModel1;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.HashMap;
import java.util.LinkedHashMap;

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


    public final DataItemViewModel title, youtubeAddress, classPageUrl;
    public final ImageUploadViewModel imageUploadVm, videoUploadVm;
    public final ObservableField<String> description;
    public final ListDialogViewModel1 postTypeVm, groupVm, countryVm, stateVm, cityVm, localityVm;
    public final Action onSubmitClicked;
    public Navigator navigator;
    private String postType;
    public Consumer<HashMap<String, Integer>> countryConsumer, stateConsumer, cityConsumer,postConsumer;


    public ObservableBoolean videoField =new ObservableBoolean(true);
    public ObservableBoolean imageField =new ObservableBoolean(true);
    public ObservableBoolean classField=new ObservableBoolean(true);
    public ObservableBoolean dateField=new ObservableBoolean(false);
    public ObservableBoolean activityField =new ObservableBoolean(false);

    public ConnectPostViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory, String postType) {
        this.navigator = navigator;
        this.postType = postType;
        title = new DataItemViewModel("");
        description = new ObservableField<>("");
        youtubeAddress = new DataItemViewModel("");
        classPageUrl = new DataItemViewModel("");

        imageUploadVm = new ImageUploadViewModel(messageHelper, navigator, R.drawable.image_placeholder, null);
        videoUploadVm = new ImageUploadViewModel(messageHelper, navigator, R.drawable.video_placeholder, null);

        LinkedHashMap<String, Integer> postTypeApiData = new LinkedHashMap<>();

        LinkedHashMap<String, Integer> postTypeLearnerApiData = new LinkedHashMap<>();
        postTypeLearnerApiData.put("Knowledge nuggets", POST_TYPE_KNOWLEDGE_NUGGETS);
        postTypeLearnerApiData.put("Buy & sell", POST_TYPE_BUY_N_SELL);
        postTypeLearnerApiData.put("Find learning partners", POST_TYPE_LEARNING_PARTNERS);

        LinkedHashMap<String, Integer> postTypeTutorApiData = new LinkedHashMap<>();
        postTypeTutorApiData.put("Discuss and Decide", POST_TYPE_DISCUSS_AND_DECIDE);

        postTypeApiData = postTypeLearnerApiData;

        HashMap<String, Integer> mSelectedPostType = new HashMap<String, Integer>();
        if("action_tips_tricks".equalsIgnoreCase(postType)) mSelectedPostType.put("Knowledge nuggets", POST_TYPE_KNOWLEDGE_NUGGETS);
        else if("action_buy_sell".equalsIgnoreCase(postType)) mSelectedPostType.put("Buy & sell", POST_TYPE_BUY_N_SELL);
        else if("action_find_partners".equalsIgnoreCase(postType)) mSelectedPostType.put("Find learning partners", POST_TYPE_LEARNING_PARTNERS);
        else if("action_tutors_article".equalsIgnoreCase(postType)){
            postTypeApiData = postTypeTutorApiData;
            mSelectedPostType.put("Discuss and Decide", POST_TYPE_DISCUSS_AND_DECIDE);
        }
        else if("action_discuss_n_decide".equalsIgnoreCase(postType)){
            postTypeApiData = postTypeTutorApiData;
            mSelectedPostType.put("Discuss and Decide", POST_TYPE_DISCUSS_AND_DECIDE);
        }

        Log.d(TAG,"postType: "+postType);


        postConsumer=new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext())
                    switch (selectedMap.values().iterator().next())
                    {
                        case POST_TYPE_KNOWLEDGE_NUGGETS:
                            videoField.set(true);
                            imageField.set(true);
                            dateField.set(false);
                            break;
                        case POST_TYPE_BUY_N_SELL:
                            imageField.set(true);
                            videoField.set(false);
                            classField.set(false);
                            break;
                        case POST_TYPE_LEARNING_PARTNERS:
                            imageField.set(false);
                            videoField.set(false);
                            classField.set(true);
                            break;
                        default:
                            break;


                    }

            }
        };


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
        }), new HashMap<String, Integer>(), false, null);

        onSubmitClicked = new Action() {
            @Override
            public void run() throws Exception {
//                uiHelper.next();
            }
        };

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

}

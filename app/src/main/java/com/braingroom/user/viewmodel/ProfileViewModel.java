package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.view.Menu;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.dto.ProfileData;
import com.braingroom.user.model.request.ProfileUpdateReq;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ProfileActivity;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class ProfileViewModel extends ViewModel {

    public final ObservableField<Boolean> editable = new ObservableField<>(false);
    public final DataItemViewModel name = new DataItemViewModel("");
    public final DataItemViewModel email = new DataItemViewModel("");
    public final DataItemViewModel contact = new DataItemViewModel("");
    public final ListDialogViewModel1 cityVm;
    public final ListDialogViewModel1 localityVm;
    public final ListDialogViewModel1 categoryVm; //Edited By Vikas Godara
    public final DataItemViewModel interest = new DataItemViewModel("");
    public final DataItemViewModel ugInstitution = new DataItemViewModel("");
    public final DataItemViewModel ugPassoutYear = new DataItemViewModel("");
    public final DataItemViewModel pgInstitution = new DataItemViewModel("");
    public final DataItemViewModel pgPassoutYear = new DataItemViewModel("");
    public final DataItemViewModel dob = new DataItemViewModel("");
    public final DataItemViewModel gender = new DataItemViewModel("");
    public final DataItemViewModel communityClass = new DataItemViewModel("");

    public final ImageUploadViewModel imageUploadVm;

    public final Action onBackClicked;
    public Consumer<HashMap<String, Integer>> cityConsumer;
    public Consumer<HashMap<String, Integer>> categoryConsumer; //Edited By Vikas Goodara

    Observable<ProfileData> getProfileObservable;

    public final ProfileActivity.UiHelper uiHelper;
    public final MessageHelper messageHelper;

    public ProfileViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator
            , @NonNull final HelperFactory helperFactory, @NonNull final ProfileActivity.UiHelper uiHelper) {
        this.uiHelper = uiHelper;
        this.messageHelper = messageHelper;
        imageUploadVm = new ImageUploadViewModel(messageHelper, navigator, R.drawable.avatar_male, null);
       getProfileObservable = apiService.getProfile(pref.getString(Constants.BG_ID, "")).doOnNext(new Consumer<ProfileData>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ProfileData data) throws Exception {
                name.s_1.set(data.getName());
                email.s_1.set(data.getEmail());
                contact.s_1.set(data.getContactNo());
                ugInstitution.s_1.set(data.getUgInstituteName());
                pgInstitution.s_1.set(data.getPgInstituteName());
                ugPassoutYear.s_1.set(data.getUgInstitutePassingYear());
                pgPassoutYear.s_1.set(data.getPgInstitutePassingYear());
                imageUploadVm.remoteAddress.set(data.getProfileImage());
                HashMap<String, Integer> selectedCityMap = new HashMap<>();
                if (!data.getCity().equals("")) {
                    selectedCityMap.put(data.getCity(), Integer.parseInt(data.getCityId()));
                    cityVm.setSelectedItemsMap(selectedCityMap);
                    HashMap<String, Integer> selectedLocalityMap = new HashMap<>();
                    if (!data.getLocality().equals("")) {
                        selectedLocalityMap.put(data.getLocality(), Integer.parseInt(data.getLocalityId()));
                        localityVm.setSelectedItemsMap(selectedLocalityMap);
                    }
                }
                uiHelper.invalidateMenu();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

            }
        });


        cityConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    localityVm.reInit(getLocalityApiObservable(selectedId));
                }
            }
        };
        cityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "City", messageHelper, apiService.getCityList("35").map(new Function<CommonIdResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommonIdResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommonIdResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getTextValue(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), false, cityConsumer);
        //Edited By Vikas Goodara
        categoryVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Interest", messageHelper, apiService.getCategory()//zEdited By Vikas Godara
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
                }), new HashMap<String, Integer>(), false, categoryConsumer);
        //Edited By Vikas Goodara

        localityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Locality", messageHelper, getLocalityApiObservable("-1"), new HashMap<String, Integer>(), false, null);

        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.finishActivity();
            }
        };

        getProfileObservable.subscribe();
    }

    private io.reactivex.Observable<ListDialogData1> getLocalityApiObservable(String cityId) {
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

    public void update() {
        ProfileUpdateReq.Snippet snippet = new ProfileUpdateReq.Snippet();
        // TODO: 23/04/17 change hardcoded uuid
        snippet.setUuid("1132");
        snippet.setFirstName(name.s_1.get());
        snippet.setEmail(email.s_1.get());
        snippet.setMobile(contact.s_1.get());
        snippet.setCityId(cityVm.getSelectedItemsId().size() > 0 ? cityVm.getSelectedItemsId().get(0) : "");
        snippet.setLocalityId(localityVm.getSelectedItemsId().size() > 0 ? localityVm.getSelectedItemsId().get(0) : "");
        snippet.setInstitutionName(ugInstitution.s_1.get());
        snippet.setGender(gender.s_1.get());
        snippet.setExpertiseArea(interest.s_1.get());
        snippet.setCommunityId(communityClass.s_1.get());
        snippet.setProfileImage(imageUploadVm.remoteAddress.get());
        messageHelper.showProgressDialog(null, "Updating your profile...");
        apiService.updateProfile(new ProfileUpdateReq(snippet)).subscribe(new Consumer<CommonIdResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull CommonIdResp commonIdResp) throws Exception {
                messageHelper.dismissActiveProgress();
                messageHelper.show(commonIdResp.getResMsg());
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                messageHelper.dismissActiveProgress();
                messageHelper.show("error : " + throwable.getMessage());
            }
        });
    }

    public void revertData() {
        editable.set(false);
        getProfileObservable.subscribe();
    }

    public void handleMenuStates(Menu menu) {
        menu.findItem(R.id.action_edit).setVisible(!editable.get());
        menu.findItem(R.id.action_done).setVisible(editable.get());
        menu.findItem(R.id.action_discard).setVisible(editable.get());
    }

    public void edit() {
        editable.set(true);
        uiHelper.invalidateMenu();
    }
}

package com.braingroom.user.viewmodel.fragment;

import android.support.annotation.NonNull;
import android.util.Pair;
import android.util.Patterns;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.request.SignUpReq;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.CommunityResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.FragmentHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.HomeActivity;
import com.braingroom.user.view.activity.SignupActivity;
import com.braingroom.user.viewmodel.DataItemViewModel;
import com.braingroom.user.viewmodel.DatePickerViewModel;
import com.braingroom.user.viewmodel.ImageUploadViewModel;
import com.braingroom.user.viewmodel.ListDialogViewModel1;
import com.braingroom.user.viewmodel.ViewModel;

import java.util.HashMap;
import java.util.LinkedHashMap;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class SignupViewModel extends ViewModel {

    public static final int TYPE_MALE = 1;
    public static final int TYPE_FEMALE = 2;

    public final DataItemViewModel fullName, emailId, password, confirmPassword, mobileNumber, referralCodeVm, passoutYear;
    public final ListDialogViewModel1 interestAreaVm;
    public final Action onNextClicked;
    public Navigator navigator;
    public Consumer<HashMap<String, Pair<String, String>>> countryConsumer, stateConsumer, cityConsumer;

    public final ListDialogViewModel1 genderVm, communityClassVm;
    public final DatePickerViewModel dobVm;
    public final ImageUploadViewModel imageUploadVm;

    public final Action onSignupClicked, onBackClicked;

    public final SearchSelectListViewModel countryVm, stateVm, cityVm, localityVM  ;
    public final DynamicSearchSelectListViewModel ugInstituteVm,pgInstituteVm;
    public Observable<HashMap<String, Pair<String, String>>> instituteUGVm, stateApiObservable, cityApiObservable, localityApiObservable, instituteApiObservable;

    private SignUpReq.Snippet signUpSnippet;


    public SignupViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull HelperFactory helperFactory, final SignupActivity.UiHelper uiHelper, FragmentHelper fragmentHelper, FragmentHelper dynamicSearchFragmentHelper) {
        this.navigator = navigator;
        fullName = new DataItemViewModel("");
        emailId = new DataItemViewModel("");
        password = new DataItemViewModel("");
        confirmPassword = new DataItemViewModel("");
        mobileNumber = new DataItemViewModel("");
        referralCodeVm = new DataItemViewModel("");
        passoutYear = new DataItemViewModel("");
        signUpSnippet = new SignUpReq.Snippet();
        signUpSnippet.setLatitude("");
        signUpSnippet.setLongitude("");
        signUpSnippet.setProfileImage("");

        dobVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "D.O.B", "choose");
        imageUploadVm = new ImageUploadViewModel(messageHelper, navigator, R.drawable.avatar_male, null);
        LinkedHashMap<String, Integer> ClassTypeApiData = new LinkedHashMap<>();
        ClassTypeApiData.put("Male", TYPE_MALE);
        ClassTypeApiData.put("Female", TYPE_FEMALE);
        genderVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Choose gender", messageHelper, Observable.just(new ListDialogData1(ClassTypeApiData)), new HashMap<String, Integer>(), false, null);
        communityClassVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Community", messageHelper, apiService.getCommunity().map(new Function<CommunityResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CommunityResp categoryResp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CommunityResp.Snippet snippet : categoryResp.getData()) {
                    itemMap.put(snippet.getName(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), true, null);

        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.back();
            }
        };
        onSignupClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (communityClassVm.selectedItemsMap.isEmpty()) {
                    messageHelper.show("Please select atleast one Community ");
                    return;
                }
                if (dobVm.date.get().equals("choose")) {
                    messageHelper.show("Please enter your Date of Birth");
                    return;
                }
                if (genderVm.selectedItemsMap.isEmpty()) {
                    messageHelper.show("Please select your gender");
                    return;
                }
                signUpSnippet.setCommunityId(android.text.TextUtils.join(",", communityClassVm.getSelectedItemsId()));
                signUpSnippet.setDOB(dobVm.date.get());
                signUpSnippet.setGender(genderVm.selectedItemsText.get());
                signUpSnippet.setReferalCode(referralCodeVm.s_1.get());
                apiService.signUp(new SignUpReq(signUpSnippet)).subscribe(new Consumer<SignUpResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull SignUpResp signUpResp) throws Exception {

                        if (signUpResp.getData().size() > 0) {
                            messageHelper.showAcceptableInfo("Successful", signUpResp.getResMsg(), new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                    navigator.navigateActivity(HomeActivity.class, null);
                                }
                            });
                        } else {
                            messageHelper.show(signUpResp.getResMsg());
                            uiHelper.back();
                        }

                    }
                });
            }
        };

        onNextClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (fullName.s_1.get().equals("")) {
                    messageHelper.show("Please Enter your name");
                    return;
                }
                if (!isValidEmail(emailId.s_1.get())) {
                    messageHelper.show("Please Enter a valid email Id");
                    return;
                }
                if (password.s_1.get().equals("")) {
                    messageHelper.show("Please enter a password");
                    return;
                }
                if (!password.s_1.get().equals(confirmPassword.s_1.get())) {
                    messageHelper.show("Password doesn't match");
                    return;
                }
                if (!isValidPhone(mobileNumber.s_1.get())) {
                    messageHelper.show("Please enter a valid mobile number");
                    return;
                }
                if (signUpSnippet.getCityId() == null) {
                    messageHelper.show("Please select a city");
                    return;
                }
                if (interestAreaVm.selectedItemsMap.isEmpty()) {
                    messageHelper.show("Please select atleast one area of Interest");
                    return;
                }
                if (!ugInstituteVm.selectedDataMap.isEmpty() && !isValidYear(passoutYear.s_1.get())) {
                    messageHelper.show("Please enter valid passout year");
                    return;
                }
                signUpSnippet.setName(fullName.s_1.get());
                signUpSnippet.setEmail(emailId.s_1.get());
                signUpSnippet.setPassword(password.s_1.get());
                signUpSnippet.setMobileNo(mobileNumber.s_1.get());
                signUpSnippet.setCategoryId(android.text.TextUtils.join(",", interestAreaVm.getSelectedItemsId()));
                if (!ugInstituteVm.selectedDataMap.isEmpty() && isValidYear(passoutYear.s_1.get())) {
                    signUpSnippet.setInstituteName1(ugInstituteVm.selectedDataMap.values().iterator().next().first);
                    signUpSnippet.setInstitutePoy1(passoutYear.s_1.get());
                }
                if (localityVM.selectedDataMap.isEmpty())
                    signUpSnippet.setLocality("");
                else
                    signUpSnippet.setLocality("" + localityVM.selectedDataMap.values().iterator().next().first);
                uiHelper.next();
            }
        };

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
                    signUpSnippet.setCountry(selectedId);
                    stateVm.refreshDataMap(stateApiObservable);
                    cityVm.refreshDataMap(null);
                } else {
                    stateVm.refreshDataMap(null);
                    cityVm.refreshDataMap(null);
                }
            }
        };
        instituteUGVm = apiService.getCountry().map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
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
        countryVm = new SearchSelectListViewModel(SignupActivity.FRAGMENT_TITLE_COUNTRY, messageHelper, navigator, "search for country", false, instituteUGVm, "", countryConsumer, fragmentHelper);


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
                    signUpSnippet.setState(selectedId);
                    cityVm.refreshDataMap(cityApiObservable);
                } else {
                    cityVm.refreshDataMap(null);
                }
            }
        };

        stateVm = new SearchSelectListViewModel(SignupActivity.FRAGMENT_TITLE_STATE, messageHelper, navigator, "search for state", false, stateApiObservable, "select a country first", stateConsumer, fragmentHelper);

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
                    signUpSnippet.setCityId(selectedId);
                    localityVM.refreshDataMap(localityApiObservable);
                } else {
                    localityVM.refreshDataMap(null);
                }
            }
        };

        cityVm = new SearchSelectListViewModel(SignupActivity.FRAGMENT_TITLE_CITY, messageHelper, navigator, "search for cities", false, cityApiObservable, "select a state first", cityConsumer, fragmentHelper);


        localityVM = new SearchSelectListViewModel(SignupActivity.FRAGMENT_TITLE_LOCALITY, messageHelper, navigator, "search for localities", false, localityApiObservable, "select a city first", null, fragmentHelper);

        interestAreaVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Interest", messageHelper, apiService.getCategory().map(new Function<CategoryResp, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull CategoryResp resp) throws Exception {
                LinkedHashMap<String, Integer> itemMap = new LinkedHashMap<>();
                for (CategoryResp.Snippet snippet : resp.getData()) {
                    itemMap.put(snippet.getCategoryName(), Integer.parseInt(snippet.getId()));
                }
                // TODO: 05/04/17 use rx zip to get if category already selected like in profile
                return new ListDialogData1(itemMap);
            }
        }), new HashMap<String, Integer>(), true, null);


        instituteApiObservable = apiService.getInstitute("").map(new Function<CommonIdResp, HashMap<String, Pair<String, String>>>() {
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
        ugInstituteVm = new DynamicSearchSelectListViewModel(SignupActivity.FRAGMENT_UG_COLLEGE, messageHelper, navigator, "search for institutes... ", false, "", null, dynamicSearchFragmentHelper);
        pgInstituteVm = new DynamicSearchSelectListViewModel(SignupActivity.FRAGMENT_PG_COLLEGE, messageHelper, navigator, "search for institutes... ", false, "", null, dynamicSearchFragmentHelper);


    }


    public static boolean isValidEmail(String target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPhone(String target) {
        return target != null && Patterns.PHONE.matcher(target).matches();
    }

    public static boolean isValidYear(String traget) {

        return traget.length() == 4 && !traget.contains("[a-zA-Z]+");

    }

}

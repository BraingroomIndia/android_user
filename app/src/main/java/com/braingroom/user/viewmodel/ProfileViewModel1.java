package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.dto.ProfileData;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.ProfileActivity;
import com.braingroom.user.view.adapters.ViewProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;

public class ProfileViewModel1 extends ViewModel {

    public final ImageUploadViewModel imageUploadVm;
    public final FollowButtonViewModel followButtonVm;
    public static final int TYPE_MALE = 1;
    public static final int TYPE_FEMALE = 2;

    public final ObservableBoolean editable = new ObservableBoolean(false);//observableBoolean;
    public final DataItemViewModel email = new DataItemViewModel("");
    public final DataItemViewModel name = new DataItemViewModel("...");
    public final DataItemViewModel contact = new DataItemViewModel("");
    public final DataItemViewModel postCount = new DataItemViewModel("...");
    public final DataItemViewModel followerCount = new DataItemViewModel("...");
    public final DataItemViewModel followingCount = new DataItemViewModel("...");
    public final DataItemViewModel localities = new DataItemViewModel("");
//    public final DataItemViewModel interest = new DataItemViewModel("");
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
    public final ListDialogViewModel1 genderVm;
    public final DatePickerViewModel dobVm;


    public final Action onBackClicked, onEditClicked;
    public Consumer<HashMap<String, Integer>> cityConsumer;
    public Consumer<HashMap<String, Integer>> categoryConsumer; //Edited By Vikas Goodara

    Observable<ProfileData> getProfileObservable;
    public final PublishSubject<List<IconTextItemViewModel>> profileDetailsListVms = PublishSubject.create();
    public List<IconTextItemViewModel> dataList;
    public final ConnectUiHelper uiHelper;
    public final MessageHelper messageHelper;
    private final ObservableBoolean observableBoolean = new ObservableBoolean(false);

    public final Action onFollowerClicked, onFollowingClicked;

    @Getter
    public ConnectFilterData connectFilterData;
    public Observable<List<ViewModel>> feedItems;
    private final Function<ConnectFeedResp, List<ViewModel>> feedDataMapFunction;
    private boolean paginationInProgress = false;
    private int nextPage = 0;
    private int currentPage = -1;

    @Getter
    ViewProvider viewProvider = new ViewProvider() {
        @Override
        public int getView(ViewModel vm) {
            if (vm instanceof ConnectFeedItemViewModel)
                return R.layout.item_connect_feed;
            else if (vm instanceof EmptyItemViewModel)
                return R.layout.item_empty_data;
            else if (vm instanceof RowShimmerItemViewModel)
                return R.layout.item_shimmer_row;
            return 0;
        }
    };

    public final int nameIcon, detailIcon1, detailIcon2, detailIcon3, detailIcon4;

    public ProfileViewModel1(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator
            , @NonNull final HelperFactory helperFactory, @NonNull final ConnectUiHelper uiHelper) {
        followButtonVm = new FollowButtonViewModel(helperFactory, messageHelper, navigator, FollowButtonViewModel.STATE_EDIT);
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });
        this.uiHelper = uiHelper;
        this.messageHelper = messageHelper;

        onFollowerClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.openFollower();
            }
        };

        onFollowingClicked = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.openFollowed();
            }
        };

        this.connectFilterData = new ConnectFilterData();

// TODO add userId to feed
        // connectFilterData.setAuthorId(pref.getString(Constants.BG_ID, ""));
        connectFilterData.setMajorCateg(ConnectHomeActivity.LEARNER_FORUM);
        connectFilterData.setMinorCateg(ConnectHomeActivity.TIPS_TRICKS);
        nameIcon = R.drawable.ic_account_circle_black_24dp;
        detailIcon1 = R.drawable.ic_account_circle_black_24dp;
        detailIcon2 = R.drawable.ic_domain_black_24dp;
        detailIcon3 = R.drawable.ic_domain_black_24dp;
        detailIcon4 = R.drawable.ic_domain_black_24dp;

        onEditClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(ProfileActivity.class, null);
            }
        };
        imageUploadVm = new ImageUploadViewModel(messageHelper, navigator, R.drawable.avatar_male, null);
        dobVm = new DatePickerViewModel(helperFactory.createDialogHelper(), "D.O.B", "choose");
        final LinkedHashMap<String, Integer> GenderTypeApiData = new LinkedHashMap<>();
        GenderTypeApiData.put("Male", TYPE_MALE);
        GenderTypeApiData.put("Female", TYPE_FEMALE);
        genderVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Choose gender", messageHelper, Observable.just(new ListDialogData1(GenderTypeApiData)), new HashMap<String, Integer>(), false, null, "");

        getProfileObservable = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return !apiSuccessful;
            }
        }).flatMap(new Function<Integer, Observable<ProfileData>>() {
            @Override
            public Observable<ProfileData> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getProfile(pref.getString(Constants.BG_ID, "")).onErrorReturn(new Function<Throwable, ProfileData>() {
                    @Override
                    public ProfileData apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return new ProfileData();
                    }
                }).doOnNext(new Consumer<ProfileData>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ProfileData data) throws Exception {
                        if (data.getEmail() == null)
                            return;
                        apiSuccessful = true;
                        HashMap<String, Integer> selectedGender = new HashMap<>();
                        if (!data.getGender().equals("-1"))
                            selectedGender.put(data.getGender().equals("1") ? "Male" : "Female", Integer.valueOf(data.getGender()));

                        name.s_1.set(data.getName());
                        postCount.s_1.set("" + data.getPost_count());
                        followerCount.s_1.set("" + data.getFollower_count());
                        followingCount.s_1.set("" + data.getFollowing_count());
                        email.s_1.set(data.getEmail());
                        contact.s_1.set(data.getContactNo());
                        dobVm.date.set(data.getDob());
                        ugInstitution.s_1.set(data.getUgInstituteName());
                        pgInstitution.s_1.set(data.getPgInstituteName());
                        ugPassoutYear.s_1.set(data.getUgInstitutePassingYear());
                        pgPassoutYear.s_1.set(data.getPgInstitutePassingYear());
                        imageUploadVm.remoteAddress.set(data.getProfileImage());
                        genderVm.setSelectedItemsMap(selectedGender);
                        localities.s_1.set(data.getLocality());
                        if (data.getDob() != null && !data.getDob().equals(""))
                            dobVm.date.set(data.getDob());
                        HashMap<String, Integer> selectedCityMap = new HashMap<>();
                        HashMap<String, Integer> selectedInterestMap = new HashMap<>();
                        if (!data.getCity().equals("")) {
                            selectedCityMap.put(data.getCity(), Integer.parseInt(data.getCityId()));
                            cityVm.setSelectedItemsMap(selectedCityMap);
                            HashMap<String, Integer> selectedLocalityMap = new HashMap<>();
                            if (!data.getLocality().equals("")) {
                                selectedLocalityMap.put(data.getLocality(), Integer.parseInt(data.getLocalityId()));
                                localityVm.setSelectedItemsMap(selectedLocalityMap);
                            }
                        }
                        if (!data.getCategoryName().equals("") && !data.getCategoryId().equals("")) {
                            List<String> categoryName = Arrays.asList(data.getCategoryName().split("\\s*,\\s*"));
                            List<Integer> categoryId = Arrays.asList(stringToIntArray(data.getCategoryId().split("\\s*,\\s*")));
                            for (int i = 0; i < categoryId.size(); i++)
                                selectedInterestMap.put(categoryName.get(i), categoryId.get(i));
                            categoryVm.setSelectedItemsMap(selectedInterestMap);
                        }
                        interest.s_1.set(data.getCategoryName());
                        dataList = new ArrayList<>();
                        addProfileData(R.drawable.ic_email_black_24dp, data.getEmail(), dataList);
                        addProfileData(R.drawable.ic_phone_black_24dp, data.getContactNo(), dataList);
                        addProfileData(R.drawable.ic_cake_black_24dp, data.getDob(), dataList);
                        addProfileData(R.drawable.ic_domain_black_24dp, "".equals(data.getUgInstituteName()) ? "" : data.getUgInstituteName() + " | " + data.getUgInstitutePassingYear(), dataList);
                        addProfileData(R.drawable.ic_school_black_24dp, "".equals(data.getPgInstituteName()) ? "" : data.getPgInstituteName() + " | " + data.getPgInstitutePassingYear(), dataList);
                        addProfileData(R.drawable.ic_domain_black_24dp, data.getCity(), dataList);
                        addProfileData(R.drawable.ic_domain_black_24dp, data.getLocality(), dataList);
                        addProfileData(R.drawable.ic_domain_black_24dp, data.getCategoryName(), dataList);
                        profileDetailsListVms.onNext(dataList);
                        // uiHelper.invalidateMenu();
                    }
                }).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                    }
                });
            }
        });
        feedDataMapFunction = new Function<ConnectFeedResp, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(ConnectFeedResp resp) throws Exception {
                currentPage = nextPage;
                nextPage = resp.getNextPage();
                if (resp.getData().size() == 0 && nextPage < 1) {
                    nonReactiveItems.add(new EmptyItemViewModel(R.drawable.empty_board, null, "No Post Available", null));
                } else {
                    //  Log.d("ConnectFeed", "\napply: nextPage:\t " + nextPage + "\n currentPage:\t" + currentPage);
                    for (final ConnectFeedResp.Snippet elem : resp.getData()) {
                        nonReactiveItems.add(new ConnectFeedItemViewModel(elem, uiHelper, helperFactory, messageHelper, navigator));
                    }
                }

                paginationInProgress = false;
                return nonReactiveItems;
            }
        };
        feedItems = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return currentPage < nextPage;
            }
        }).flatMap(new Function<Integer, Observable<List<ViewModel>>>() {
            @Override
            public Observable<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                paginationInProgress = true;
                return apiService.getConnectFeed(connectFilterData, nextPage)
                        .map(feedDataMapFunction).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
                            @Override
                            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                                throwable.printStackTrace();
                                return nonReactiveItems;
                            }
                        }).mergeWith(getLoadingItems());
            }
        });

        /*apiService.getProfile(pref.getString(Constants.BG_ID, "")).doOnNext(new Consumer<ProfileData>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ProfileData data) throws Exception {
                HashMap<String, Integer> selectedGender = new HashMap<>();
                if (!data.getGender().equals("-1"))
                    selectedGender.put(data.getGender().equals("1") ? "Male" : "Female", Integer.valueOf(data.getGender()));
                name.s_1.set(data.getName());
                email.s_1.set(data.getEmail());
                contact.s_1.set(data.getContactNo());
                dobVm.date.set(data.getDob());
                ugInstitution.s_1.set(data.getUgInstituteName());
                pgInstitution.s_1.set(data.getPgInstituteName());
                ugPassoutYear.s_1.set(data.getUgInstitutePassingYear());
                pgPassoutYear.s_1.set(data.getPgInstitutePassingYear());
                imageUploadVm.remoteAddress.set(data.getProfileImage());
                genderVm.setSelectedItemsMap(selectedGender);
                if (data.getDob() != null && !data.getDob().equals(""))
                    dobVm.date.set(data.getDob());
                HashMap<String, Integer> selectedCityMap = new HashMap<>();
                HashMap<String, Integer> selectedInterestMap = new HashMap<>();
                if (!data.getCity().equals("")) {
                    selectedCityMap.put(data.getCity(), Integer.parseInt(data.getCityId()));
                    cityVm.setSelectedItemsMap(selectedCityMap);
                    HashMap<String, Integer> selectedLocalityMap = new HashMap<>();
                    if (!data.getLocality().equals("")) {
                        selectedLocalityMap.put(data.getLocality(), Integer.parseInt(data.getLocalityId()));
                        localityVm.setSelectedItemsMap(selectedLocalityMap);
                    }
                }
                if (!data.getCategoryName().equals("") && !data.getCategoryId().equals("")) {
                    List<String> categoryName = Arrays.asList(data.getCategoryName().split("\\s*,\\s*"));
                    List<Integer> categoryId = Arrays.asList(stringToIntArray(data.getCategoryId().split("\\s*,\\s*")));
                    for (int i = 0; i < categoryId.size(); i++)
                        selectedInterestMap.put(categoryName.get(i), categoryId.get(i));
                    categoryVm.setSelectedItemsMap(selectedInterestMap);
                }
                uiHelper.invalidateMenu();
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

            }
        });*/


        cityConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {
                if (selectedMap.values().iterator().hasNext()) {
                    String selectedId = "" + selectedMap.values().iterator().next();
                    localityVm.selectedItemsMap.clear();
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
        }).onErrorReturn(new Function<Throwable, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                return new ListDialogData1(new LinkedHashMap<String, Integer>());
            }
        }), new HashMap<String, Integer>(), false, cityConsumer, "");
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
                }).onErrorReturn(new Function<Throwable, ListDialogData1>() {
                    @Override
                    public ListDialogData1 apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return new ListDialogData1(new LinkedHashMap<String, Integer>());
                    }
                }), new HashMap<String, Integer>(), true, categoryConsumer, "");
        //Edited By Vikas Goodara

        localityVm = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Locality", messageHelper, getLocalityApiObservable("-1"), new HashMap<String, Integer>(), false, null, "select a city first");

        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.finishActivity();
            }
        };

        getProfileObservable.subscribe();
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
        }).onErrorReturn(new Function<Throwable, ListDialogData1>() {
            @Override
            public ListDialogData1 apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                return new ListDialogData1(new LinkedHashMap<String, Integer>());
            }
        });
    }

//    public void update() {
//        ProfileUpdateReq.Snippet snippet = new ProfileUpdateReq.Snippet();
///*
//        if (dobVm.date.get().equals("choose")) {
//            messageHelper.show("Please enter your Date of Birth");
//            return;
//        }*/
//        if (genderVm.selectedItemsMap.isEmpty()) {
//            snippet.setGender("");
//        } else
//            snippet.setGender(genderVm.selectedItemsText.get());
//        // TODO: 23/04/17 change hardcoded uuid
//        snippet.setUuid(pref.getString(Constants.UUID, ""));
//        snippet.setFirstName(name.s_1.get());
//        snippet.setEmail(email.s_1.get());
//        snippet.setMobile(contact.s_1.get());
//        snippet.setCityId(cityVm.getSelectedItemsId().size() > 0 ? cityVm.getSelectedItemsId().get(0) : "");
//        snippet.setLocalityId(localityVm.getSelectedItemsId().size() > 0 ? localityVm.getSelectedItemsId().get(0) : "");
//        snippet.setCategoryId(categoryVm.getSelectedItemsId().size() > 0 ? categoryVm.getSelectedItemsId().get(0) : "");
//        snippet.setInstitutionName("");
//        snippet.setUgInstitutePassingYear(ugPassoutYear.s_1.get());
//        snippet.setPgInstituteName(pgInstitution.s_1.get());
//        snippet.setDob(dobVm.date.get());
//        snippet.setGender(genderVm.getSelectedItemsId().size() > 0 ? genderVm.getSelectedItemsId().get(0) : "");
//        snippet.setCategoryId(android.text.TextUtils.join(",", categoryVm.getSelectedItemsId()));
//        snippet.setCommunityId(communityClass.s_1.get());
//        snippet.setProfileImage(imageUploadVm.remoteAddress.get());
//        messageHelper.showProgressDialog(null, "Updating your profile...");
//        apiService.updateProfile(new ProfileUpdateReq(snippet)).subscribe(new Consumer<CommonIdResp>() {
//            @Override
//            public void accept(@io.reactivex.annotations.NonNull CommonIdResp commonIdResp) throws Exception {
//                messageHelper.dismissActiveProgress();
//                messageHelper.show(commonIdResp.getResMsg());
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
//                messageHelper.dismissActiveProgress();
//                messageHelper.show("error : " + throwable.getMessage());
//            }
//        });
//    }

    @Override
    public void paginate() {
        if (nextPage > -1 && !paginationInProgress) {
            nextPage = nextPage + 1;
            callAgain.set(callAgain.get() + 1);

        }
    }

    @Override
    public void retry() {
        callAgain.set(callAgain.get() + 1);
        connectivityViewmodel.isConnected.set(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        connectivityViewmodel.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        connectivityViewmodel.onPause();
    }


    //    public void revertData() {
//        editable.set(false);
//        getProfileObservable.subscribe();
//    }
//
//    public void handleMenuStates(Menu menu) {
//        menu.findItem(R.id.action_done).setVisible(editable.get());
//        menu.findItem(R.id.action_discard).setVisible(editable.get());
//    }
//
//    public void edit() {
//        editable.set(true);
//        uiHelper.invalidateMenu();
//    }

    public void rest() {
        nonReactiveItems = new ArrayList<>();
        nextPage = 0;
        currentPage = -1;
        paginationInProgress = false;
        callAgain.set(callAgain.get() + 1);
    }

    public void setFilterData(ConnectFilterData connectFilterData) {
        this.connectFilterData = connectFilterData;
        rest();
    }

    //
    public static Integer[] stringToIntArray(String[] a) {
        Integer[] b = new Integer[a.length];
        for (int i = 0; i < a.length; i++) {
            b[i] = Integer.parseInt(a[i]);
        }

        return b;
    }

    private void addProfileData(int icon, String name, List<IconTextItemViewModel> dataList) {
        if (!"".equals(name))
            dataList.add(new IconTextItemViewModel(icon, name, null));
    }

    private Observable<List<ViewModel>> getLoadingItems() {
        int count;
        if (nonReactiveItems.isEmpty())
            count = 4;
        else
            count = 2;
        List<ViewModel> result = new ArrayList<>();
        result.addAll(nonReactiveItems);
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
    }
}

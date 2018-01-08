package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.dto.ClassLocationData;
import com.braingroom.user.model.response.CompetitionStatusResp;
import com.braingroom.user.model.response.ExploreResp;
import com.braingroom.user.model.response.NotificationCountResp;
import com.braingroom.user.model.response.UserGeoLocationResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.DialogHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ExploreActivity;
import com.braingroom.user.view.activity.FilterActivity;
import com.braingroom.user.view.activity.HomeActivity;
import com.braingroom.user.view.activity.SearchActivity;
import com.braingroom.user.view.activity.SignUpActivityCompetition;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


public class HomeViewModel extends ViewModel {


    public final Action onSearchClicked, onExploreClicked, onFilterClicked;
    public final ObservableField<Action> onRegister;
    public final ObservableField<String> profileImage = new ObservableField();
    public final ObservableField<String> userName = new ObservableField("Hello Learner!");
    public final ObservableField<String> userEmail = new ObservableField("Sign In.");
    public final GridViewModel categoryVm;
    public final GridViewModel gridViewModel;
    public final ShowcaseClassListViewModel featuredVm, recommendedVm  /*,indigenousVm*/;

    public final ImageUploadViewModel imageUploadViewModel;


    private List<ClassLocationData> locationList = new ArrayList<>();
    private List<MarkerOptions> markerList = new ArrayList<>();

    Observable<ExploreResp> exploreObservable;

    private Disposable notificationDisposable;


    public ObservableBoolean loggedIn;

    public GoogleMap mGoogleMap; //Edited by Vikas Godara
    private Map<String, Integer> pinColorMap = new HashMap<>();
    private DialogHelper dialogHelper;
    Navigator navigator;


    public Observable observable;
    public boolean showCompetitionLink = true;
    public String[] competitionText = {"", ""};
    private Disposable timerDisposable;
    private final HomeActivity.UiHelper uiHelper;

/*
    private final int[] resArray = new int[]{R.drawable.main_category_1,
            R.drawable.main_category_2, //Edited By Vikas Godara
            R.drawable.main_category_3,
            R.drawable.main_category_4,
            R.drawable.main_category_5, //Edited By Vikas Godara
            R.drawable.main_category_6};
*/

    public HomeViewModel(@NonNull final FirebaseAnalytics mFirebaseAnalytics, @NonNull final Tracker mTracker, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator,
                         @NonNull final DialogHelper dialogHelper, @NonNull final HelperFactory helperFactory, @NonNull final HomeActivity.UiHelper uiHelper) {

        this.mFirebaseAnalytics = mFirebaseAnalytics;
        this.mTracker = mTracker;
        this.loggedIn = new ObservableBoolean(getLoggedIn());
        this.uiHelper = uiHelper;

        imageUploadViewModel = new ImageUploadViewModel(R.drawable.avatar_male, pref.getString(Constants.PROFILE_PIC, ""));
//        this.featuredVm = new ShowcaseClassListViewModel("Fast Tracked - Education & Skill Development", messageHelper, navigator, apiService.getFeaturedClass(), ClassDetailActivity.class);
        this.recommendedVm = new ShowcaseClassListViewModel("Recommended - Classes & Activities", messageHelper, navigator, apiService.getRecommendedClass(), ClassDetailActivity.class);
        this.featuredVm = new ShowcaseClassListViewModel("Free - Classes & Activities", messageHelper, navigator, apiService.getFeaturedClass(), ClassDetailActivity.class);
//        this.indigenousVm = new ShowcaseClassListViewModel("Featured - Classes & Activities", messageHelper, navigator, apiService.getIndigeneousClass(), ClassDetailActivity.class);
        this.profileImage.set(pref.getString(Constants.PROFILE_PIC, null));
        this.userName.set(pref.getString(Constants.NAME, "Hello Learner!"));
        this.userEmail.set(pref.getString(Constants.EMAIL, null));
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
                connectivityViewmodel.isConnected.set(true);
                Log.d(TAG, "run internet: " + connectivityViewmodel.isConnected.get());
            }
        });
        this.dialogHelper = dialogHelper;
        this.navigator = navigator;
        Action temp = new Action() {
            @Override
            public void run() throws Exception {
                messageHelper.show("Cliked");
            }
        };
        onRegister = new ObservableField<>(temp);


        if (UserApplication.locationSettingPopup) {
            apiService.getUserGeoLocation().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<UserGeoLocationResp>() {
                @Override
                public void accept(UserGeoLocationResp resp) throws Exception {
                    if (resp.getResCode()) {
                        UserApplication.locationSettingPopup = false;
                        messageHelper.showAcceptDismissInfo(resp.getData().getTitle(), resp.getData().getMessage(), new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialogHelper.showCustomView(R.layout.dialog_location_setting, new LocationSettingViewModel(messageHelper, navigator, helperFactory), false);
                            }
                        });
                    }

                }
            });
        }

        observable = Observable.interval(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread());

        pinColorMap.put("#026510", R.drawable.pin_new_1);
        pinColorMap.put("#ffa0d0", R.drawable.pin_new_2);
        pinColorMap.put("#ff9600", R.drawable.pin_new_3);
        pinColorMap.put("#ab47ff", R.drawable.pin_new_4);
        pinColorMap.put("#a3a3a3", R.drawable.pin_new_5);
        pinColorMap.put("#50bef7", R.drawable.pin_new_6);
        pinColorMap.put("My Location", R.drawable.pin_0);


        this.gridViewModel = new GridViewModel(navigator, GridViewModel.OnlineCommunity, null);


        this.categoryVm = new GridViewModel(navigator, GridViewModel.CATEGORY, null);

        FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws
                    Exception {
                return getLoggedIn();
            }
        }).flatMap(new Function<Integer, Observable<NotificationCountResp>>() {
            @Override
            public Observable<NotificationCountResp> apply
                    (@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getUnreadMessageCount();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<NotificationCountResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull NotificationCountResp resp) throws
                    Exception {
                if (resp != null && resp.getData() != null && !resp.getData().isEmpty()) {
                    messageCount = resp.getData().get(0).getCount();
                    uiHelper.setCount();

                }
            }
        });

        FieldUtils.toObservable(callAgain).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                apiService.getCompetitionStatus().subscribe(new Consumer<CompetitionStatusResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull CompetitionStatusResp resp) throws Exception {

                        if (resp != null && resp.getData() == null && !resp.getData().isEmpty()) {
                            showCompetitionLink = false;
                        } else if (resp.getData().isEmpty()) {
                            showCompetitionLink = false;
                        } else {
                            showCompetitionLink = resp.getData().get(0).displayText.length > 1;
                            competitionText = resp.getData().get(0).displayText;
                        }
                    }
                });
            }
        });

        FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws
                    Exception {
                return getLoggedIn();
            }
        }).flatMap(new Function<Integer, Observable<NotificationCountResp>>() {
            @Override
            public Observable<NotificationCountResp> apply
                    (@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getUnreadNotificationCount();
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<NotificationCountResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull NotificationCountResp resp) throws
                    Exception {
                if (resp != null && resp.getData() != null && !resp.getData().isEmpty()) {
                    notificationCount = resp.getData().get(0).getCount();
                    uiHelper.setCount();
                }
            }
        });

        refreshMapPinsToNewLocation("13.0826802", "80.2707184");

        onSearchClicked = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        navigator.navigateActivity(SearchActivity.class, null);
                    }
                }

        ;

        onExploreClicked = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        navigator.navigateActivity(ExploreActivity.class, null);
                    }
                }

        ;
        onFilterClicked = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        HashMap<String, Integer> filterMap = new HashMap<>();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.categoryFilterMap, filterMap);
                        bundle.putSerializable(Constants.segmentsFilterMap, filterMap);
                        bundle.putSerializable(Constants.cityFilterMap, filterMap);
                        bundle.putSerializable(Constants.localityFilterMap, filterMap);
                        bundle.putSerializable(Constants.communityFilterMap, filterMap);
                        bundle.putSerializable(Constants.classTypeFilterMap, filterMap);
                        bundle.putSerializable(Constants.classScheduleFilterMap, filterMap);
                        bundle.putSerializable(Constants.vendorListFilterMap, filterMap);
                        bundle.putString("keywords", "");
                        bundle.putString("startDate", "");
                        bundle.putString("endDate", "");
                        bundle.putString(Constants.origin, FilterViewModel.ORIGIN_FILTER);
                        navigator.navigateActivity(FilterActivity.class, bundle);
                    }
                }

        ;


    }

    public void refreshMapPinsToNewLocation(String latitude, String longitude) {
        exploreObservable = apiService.getExploreDashboard(latitude, longitude);
        exploreObservable.observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ExploreResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ExploreResp resp) throws Exception {
                ClassLocationData cld;
                locationList.clear();
                if (!isEmpty(resp.getData()))
                    for (final ExploreResp.Snippet snippet : resp.getData()) {
                        cld = new ClassLocationData();
                        cld.setLatitude(snippet.getLatitude());
                        cld.setLongitude(snippet.getLongitude());
                        cld.setClassId(snippet.getClassId());
                        cld.setClassTopic(snippet.getClassTopic());
                        cld.setColorCode(snippet.getColorCode());
                        locationList.add(cld);
                    }
                if (mGoogleMap != null && markerList.size() == 0)
                    populateMarkers(locationList);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.d(TAG, "populateMarkers: " + throwable.toString());

            }
        });
    }


    public void setGoogleMap(@NonNull GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        this.mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String latitude = TextUtils.split((String) marker.getTag(), ",")[0];
                String longitude = TextUtils.split((String) marker.getTag(), ",")[1];
                dialogHelper.showCustomView(R.layout.marker_class_list, new MarkerClassListViewModel("Classes", navigator, latitude, longitude), false);
                return false;
            }
        });
        if (locationList.size() > 0) populateMarkers(locationList);
    }

    private void populateMarkers(@NonNull List<ClassLocationData> locations) {
        LatLng latlng = null;
        MarkerOptions markerOption;
        mGoogleMap.clear();
        markerList.clear();
        double latSum = 0, lngSum = 0;
        int i = 0;
        for (ClassLocationData location : locations) {
            try {
                latlng = new LatLng(Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));

                latSum = latSum + Double.valueOf(location.getLatitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Log.d(TAG, "populateMarkers : " + i + "\n" + location.toString());
            lngSum = lngSum + Double.valueOf(location.getLongitude());
            markerOption = new MarkerOptions().position(latlng).title(location.getLocationArea()).icon(getPinIcon(location));
            markerList.add(markerOption);
            mGoogleMap.addMarker(markerOption).setTag(location.getLatitude() + "," + location.getLongitude());
            i++;
        }
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latSum / locations.size(), lngSum / locations.size()), 11.0f));
    }

    private BitmapDescriptor getPinIcon(ClassLocationData data) {
        return BitmapDescriptorFactory.fromResource(pinColorMap.get(data.getColorCode()));
    }

    private Observable<List<ViewModel>> getGridLoadingItems(int count) {
        List<ViewModel> result = new ArrayList<>();
        result.addAll(Collections.nCopies(count, new TileShimmerItemViewModel()));
        return Observable.just(result);
    }

    @Override
    public void retry() {
        callAgain.set(callAgain.get() + 1);
        connectivityViewmodel.isConnected.set(true);
      /*  featuredVm.retry();*/
        recommendedVm.retry();
      /*  indigenousVm.retry();*/
        categoryVm.retry();
        gridViewModel.retry();
    }

    private void notificationResume() {
        notificationDisposable = UserApplication.getInstance().getNewNotificationBus().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isNewNotification) {
                if (isNewNotification)
                    callAgain.set(callAgain.get() + 1);

                Log.d("Notification", "accept: " + isNewNotification);
            }
        });
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        notificationResume();
        this.profileImage.set(pref.getString(Constants.PROFILE_PIC, null));
        imageUploadViewModel.setRemoteAddress(pref.getString(Constants.PROFILE_PIC, ""));
        userName.set(pref.getString(Constants.NAME, "Hello Learner!"));
        userEmail.set(pref.getString(Constants.EMAIL, null));
        loggedIn.set(getLoggedIn());
        connectivityViewmodel.onResume();
        apiService.getCompetitionStatus().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<CompetitionStatusResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull CompetitionStatusResp resp) throws Exception {
                if (resp.getData() == null) {
                    showCompetitionLink = false;
                } else if (resp.getData().isEmpty()) {
                    showCompetitionLink = false;
                } else {
                    showCompetitionLink = resp.getData().get(0).displayText.length > 1;
                    competitionText = resp.getData().get(0).displayText;
                    final Bundle data = new Bundle();
                    data.putInt(Constants.competitionStatus, resp.getData().get(0).getStatus());
                    Action temp = new Action() {
                        @Override
                        public void run() throws Exception {
                            navigator.navigateActivity(SignUpActivityCompetition.class, data);
                        }
                    };
                    onRegister.set(temp);

                }
                if (!getLoggedIn() && showCompetitionLink) {
                    timerDisposable = observable.subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
                            uiHelper.animate((int) (aLong % competitionText.length));
                        }
                    });
                } else uiHelper.animate(-1);
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
        connectivityViewmodel.onPause();
        safelyDispose(notificationDisposable, timerDisposable);
    }
}

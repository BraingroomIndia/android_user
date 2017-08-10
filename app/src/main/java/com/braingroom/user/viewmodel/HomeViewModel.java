package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.dto.ClassLocationData;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.ExploreResp;
import com.braingroom.user.model.response.NotificationCountResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.DialogHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.CommunityListActivity;
import com.braingroom.user.view.activity.ExploreActivity;
import com.braingroom.user.view.activity.FilterActivity;
import com.braingroom.user.view.activity.HomeActivity;
import com.braingroom.user.view.activity.SearchActivity;
import com.braingroom.user.view.activity.SegmentListActivity;
import com.braingroom.user.view.activity.SignUpActivityCompetition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static android.content.ContentValues.TAG;


public class HomeViewModel extends ViewModel {


    public final Action onSearchClicked, onExploreClicked, onRegister, onFilterClicked;
    public final ObservableField<String> profileImage = new ObservableField();
    public final ObservableField<String> userName = new ObservableField("Hello Learner!");
    public final ObservableField<String> userEmail = new ObservableField("Sign In.");
    public final GridViewModel categoryVm;
    public final IconTextItemViewModel communityVm;
    public final IconTextItemViewModel onlineClassVm;
    public final ShowcaseClassListViewModel featuredVm, trendingVm, indigenousVm;
    public final Observable<List<ViewModel>> categories;

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

/*
    private final int[] resArray = new int[]{R.drawable.main_category_1,
            R.drawable.main_category_2, //Edited By Vikas Godara
            R.drawable.main_category_3,
            R.drawable.main_category_4,
            R.drawable.main_category_5, //Edited By Vikas Godara
            R.drawable.main_category_6};
*/

    public HomeViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator,
                         @NonNull final DialogHelper dialogHelper, @NonNull final HomeActivity.UiHelper uiHelper) {
        this.loggedIn = new ObservableBoolean(getLoggedIn());

        this.featuredVm = new ShowcaseClassListViewModel("Fast Tracked - Education & Skill Development", messageHelper, navigator, apiService.getFeaturedClass(), ClassDetailActivity.class);
        this.trendingVm = new ShowcaseClassListViewModel("People's Choice - Hobbies & Sports", messageHelper, navigator, apiService.getTrendingClass(), ClassDetailActivity.class);
        this.indigenousVm = new ShowcaseClassListViewModel("Featured - Classes & Activities", messageHelper, navigator, apiService.getIndigeneousClass(), ClassDetailActivity.class);
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

        observable = Observable.interval(2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread());

        pinColorMap.put("#026510", R.drawable.pin_new_1);
        pinColorMap.put("#ffa0d0", R.drawable.pin_new_2);
        pinColorMap.put("#ff9600", R.drawable.pin_new_3);
        pinColorMap.put("#ab47ff", R.drawable.pin_new_4);
        pinColorMap.put("#a3a3a3", R.drawable.pin_new_5);
        pinColorMap.put("#50bef7", R.drawable.pin_new_6);
        pinColorMap.put("My Location", R.drawable.pin_0);

        onRegister = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(SignUpActivityCompetition.class, null);
            }
        };
        communityVm = new IconTextItemViewModel(R.drawable.ic_account_circle_black_24dp, "Community Group", new MyConsumer<IconTextItemViewModel>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                navigator.navigateActivity(CommunityListActivity.class, null);
            }
        });
        onlineClassVm = new IconTextItemViewModel(R.drawable.ic_account_circle_black_24dp, "Online Class", new MyConsumer<IconTextItemViewModel>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                FilterData filterData = new FilterData();
                filterData.setClassType(FilterViewModel.CLASS_TYPE_SEMINAR + "");
                Bundle data = new Bundle();
                data.putSerializable("filterData", filterData);
                data.putString("origin", FilterViewModel.ORIGIN_HOME);
                navigator.navigateActivity(ClassListActivity.class, data);
            }
        });
        categories = FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return !apiSuccessful;
            }
        }).flatMap(new Function<Integer, ObservableSource<List<ViewModel>>>() {
            @Override
            public ObservableSource<List<ViewModel>> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getCategory().map(new Function<CategoryResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(@io.reactivex.annotations.NonNull CategoryResp categoryResp) throws Exception {
                        List<CategoryResp.Snippet> snippetList = categoryResp.getData();
                        final List<ViewModel> results = new ArrayList<>();
                        for (final CategoryResp.Snippet snippet : snippetList) {
                            results.add(new IconTextItemViewModel(snippet.getCategoryImage(), snippet.getCategoryName(),
                                    new MyConsumer<IconTextItemViewModel>() {
                                        @Override
                                        public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                                            if (!snippet.getId().equals("-1")) {
                                                Bundle data = new Bundle();
                                                HashMap<String, Integer> categoryMap = new HashMap<String, Integer>();
                                                categoryMap.put(snippet.getCategoryName(), Integer.parseInt(snippet.getId()));
                                                data.putSerializable("categoryMap", categoryMap);
                                                navigator.navigateActivity(SegmentListActivity.class, data);
                                            }
                                        }
                                    }));
                        }
                        return results;
                    }
                });
            }
        }).onErrorReturn(new Function<Throwable, List<ViewModel>>() {
            @Override
            public List<ViewModel> apply(@io.reactivex.annotations.NonNull Throwable
                                                 throwable) throws Exception {
                return new ArrayList<>();
            }
        });

        this.categoryVm = new GridViewModel(categories, "Learn new skills or pick up a new hobby");
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
        }).subscribe(new Consumer<NotificationCountResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull NotificationCountResp resp) throws
                    Exception {
                if (resp != null && resp.getData() != null) {
                    messageCount = resp.getData().get(0).getCount();
                    uiHelper.setCount();

                }
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
        }).subscribe(new Consumer<NotificationCountResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull NotificationCountResp resp) throws
                    Exception {
                if (resp != null && resp.getData() != null) {
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
                        bundle.putSerializable("category", filterMap);
                        bundle.putSerializable("segment", filterMap);
                        bundle.putSerializable("city", filterMap);
                        bundle.putSerializable("locality", filterMap);
                        bundle.putSerializable("community", filterMap);
                        bundle.putSerializable("classType", filterMap);
                        bundle.putSerializable("classSchedule", filterMap);
                        bundle.putSerializable("vendorList", filterMap);
                        bundle.putString("keywords", "");
                        bundle.putString("startDate", "");
                        bundle.putString("endDate", "");
                        bundle.putString("origin", FilterViewModel.ORIGIN_HOME);
                        navigator.navigateActivity(FilterActivity.class, bundle);
                    }
                }

        ;


    }

    public void refreshMapPinsToNewLocation(String latitude, String longitude) {
        exploreObservable = apiService.getExploreDashboard(latitude, longitude);
        exploreObservable.subscribe(new Consumer<ExploreResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ExploreResp resp) throws Exception {
                ClassLocationData cld;
                locationList.clear();
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
                throw e;
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

    @Override
    public void retry() {
        callAgain.set(callAgain.get() + 1);
        connectivityViewmodel.isConnected.set(true);
        featuredVm.retry();
        trendingVm.retry();
        indigenousVm.retry();
        categoryVm.retry();
    }

    public void notificationResume() {
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
        userName.set(pref.getString(Constants.NAME, "Hello Learner!"));
        userEmail.set(pref.getString(Constants.EMAIL, null));
        loggedIn.set(getLoggedIn());
        connectivityViewmodel.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        connectivityViewmodel.onPause();
        safelyDispose(notificationDisposable);
    }
}

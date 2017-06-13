package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassLocationData;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.ExploreResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.DialogHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.ExploreActivity;
import com.braingroom.user.view.activity.SearchActivity;
import com.braingroom.user.view.activity.SignUpActivityCompetition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.rollbar.android.Rollbar.TAG;

public class HomeViewModel extends ViewModel {

    public final Action onSearchClicked, onExploreClicked,onRegister;
    public final ObservableField<String> profileImage = new ObservableField();
    public final ObservableField<String> userName = new ObservableField("Hello Learner!");
    public final ObservableField<String> userEmail = new ObservableField("Sign In.");
    public final CommunityGridViewModel communityVm;
    public final ShowcaseClassListViewModel featuredVm, trendingVm, indigenousVm;
    public final ConnectivityViewModel connectivityViewmodel;
    public final Observable<List<ViewModel>> categories;

    List<ClassLocationData> locationList = new ArrayList<>();
    List<MarkerOptions> markerList = new ArrayList<>();

    Observable<ExploreResp> exploreObservable;
    public GoogleMap mGoogleMap; //Edited by Vikas Godara
    Map<String, Integer> pinColorMap = new HashMap<>();
    DialogHelper dialogHelper;
    Navigator navigator;

    public final int[] resArray = new int[]{R.drawable.main_category_1,
            R.drawable.main_category_2, //Edited By Vikas Godara
            R.drawable.main_category_3,
            R.drawable.main_category_4,
            R.drawable.main_category_5, //Edited By Vikas Godara
            R.drawable.main_category_6};

    public HomeViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull final DialogHelper dialogHelper) {
        this.communityVm = new CommunityGridViewModel(messageHelper, navigator, ClassListActivity.class);
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
            }
        });
        this.dialogHelper = dialogHelper;
        this.navigator = navigator;


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
        categories = apiService.getCategory()
                //Edited By Vikas Godara
                .map(new Function<CategoryResp, List<CategoryResp.Snippet>>() {
                    @Override
                    public List<CategoryResp.Snippet> apply(@io.reactivex.annotations.NonNull CategoryResp categoryResp) throws Exception {
                        List<CategoryResp.Snippet> snippetList = categoryResp.getData();
                        Collections.swap(snippetList, 1, 4);
                        return snippetList;
                    }
                })
                //Edited by Vikas Godara
                .map(new Function<List<CategoryResp.Snippet>, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(List<CategoryResp.Snippet> resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        for (final CategoryResp.Snippet snippet : resp) {
                            results.add(new IconTextItemViewModel(resArray[Integer.parseInt(snippet.getId()) - 1], snippet.getCategoryName(), new MyConsumer<IconTextItemViewModel>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull IconTextItemViewModel var1) {
                                    if (!snippet.getId().equals("-1")) {
                                        Bundle data = new Bundle();
                                        data.putString("categoryId", snippet.getId());
                                        navigator.navigateActivity(ClassListActivity.class, data);
                                    }

                                }
                            }));
                        }
                        return results;
                    }
                });

        refreshMapPinsToNewLocation("13.0826802", "80.2707184");
        onSearchClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(SearchActivity.class, null);
            }
        };

        onExploreClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.navigateActivity(ExploreActivity.class, null);
            }
        };

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
                if (mGoogleMap != null && markerList.size() == 0) populateMarkers(locationList);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

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
                dialogHelper.showCustomView(R.layout.marker_class_list, new MarkerClassListViewModel("Classes", navigator, latitude, longitude));
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
            latlng = new LatLng(Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
            latSum = latSum + Double.valueOf(location.getLatitude());
            Log.d(TAG, "populateMarkers : " + i + "\n" + location.toString());
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
        featuredVm.retry();
        trendingVm.retry();
        indigenousVm.retry();
        communityVm.retry();
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
}

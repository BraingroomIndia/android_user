package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.location.Location;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassLocationData;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.ExploreResp;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.DialogHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.adapters.ViewProvider;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.patloew.rxlocation.RxLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;

public class ExploreViewModel extends ViewModel {

    public final Action onSearchClicked;
    List<ClassLocationData> locationList = new ArrayList<>();
    List<MarkerOptions> markerList = new ArrayList<>();
    public final Observable<List<ViewModel>> categories;

    Observable<ExploreResp> exploreObservable;
    private GoogleMap mGoogleMap;

    private String categoryId = "", radius = "30", latitude = "13.0826802", longitude = "80.2707184";
    public ObservableField<String> locationName = new ObservableField<>();

    PublishSubject<DataItemViewModel> categorySelectorSubject = PublishSubject.create();
    public boolean locationPermission = false;

    @Getter
    ViewProvider viewProvider = new ViewProvider() {
        @Override
        public int getView(ViewModel vm) {
            return R.layout.item_segments_text;
        }
    };
    MessageHelper messageHelper;
    DialogHelper dialogHelper;
    Navigator navigator;

    Map<String, Integer> pinColorMap = new HashMap<>();
    float zoom = 11.0f;

    public ExploreViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, @NonNull final DialogHelper dialogHelper) {
        this.messageHelper = messageHelper;
        this.dialogHelper = dialogHelper;
        this.navigator = navigator;

        pinColorMap.put("#026510", R.drawable.pin_new_1);
        pinColorMap.put("#ffa0d0", R.drawable.pin_new_2);
        pinColorMap.put("#ff9600", R.drawable.pin_new_3);
        pinColorMap.put("#ab47ff", R.drawable.pin_new_4);
        pinColorMap.put("#a3a3a3", R.drawable.pin_new_5);
        pinColorMap.put("#50bef7", R.drawable.pin_new_6);

        categories = Observable.just(getDefaultCategories()).mergeWith(apiService.getCategory())
                .map(new Function<CategoryResp, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(CategoryResp resp) throws Exception {
                        List<ViewModel> results = new ArrayList<>();
                        if (resp.getData().size() == 0) resp = getDefaultCategories();

                        for (final CategoryResp.Snippet elem : resp.getData()) {
                            results.add(new DataItemViewModel(elem.getCategoryName(), false, new MyConsumer<DataItemViewModel>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull DataItemViewModel viewModel) {
                                    categoryId = elem.getId();
//                                    zoom = 11.0f;
                                    refreshMapPinsToNewLocation(null);
                                    categorySelectorSubject.onNext(viewModel);
                                }
                            }, categorySelectorSubject));
                        }
                        return results;
                    }
                });


        refreshMapPinsToNewLocation(null);
        onSearchClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.launchPlaceSearchIntent(PLACE_AUTOCOMPLETE_REQUEST_CODE);
            }
        };

    }

    @SuppressWarnings({"MissingPermission"})
    public void refreshMapPinsToNewLocation(final LatLng zoomLatLng) {
        exploreObservable = apiService.exploreFilter(categoryId, locationName.get() == null ? "" : locationName.get(), radius, latitude, longitude);
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
                if (mGoogleMap != null) {
                    if (locationPermission) mGoogleMap.setMyLocationEnabled(true);
                    populateMarkers(zoomLatLng, locationList);
                }

            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.d("Explore Viewmodel", "accept: ");
            }
        });
    }


    private CategoryResp getDefaultCategories() {
        List<CategoryResp.Snippet> data = new ArrayList<>();
        data.add(new CategoryResp.Snippet("-1", "loading categories...", null));
        return new CategoryResp(data);
    }

    @SuppressWarnings({"MissingPermission"})
    public void setGoogleMap(@NonNull GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        this.mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String latitude = TextUtils.split((String) marker.getTag(), ",")[0];
                String longitude = TextUtils.split((String) marker.getTag(), ",")[1];
                if (!latitude.equalsIgnoreCase("my Location"))
                    dialogHelper.showCustomView(R.layout.marker_class_list, new MarkerClassListViewModel("Classes", navigator, latitude, longitude), false);
                return false;
            }
        });
        if (locationPermission) this.mGoogleMap.setMyLocationEnabled(true);
        if (locationList.size() > 0) populateMarkers(null, locationList);
    }

    public void enableLocationPermission() {
        locationPermission = true;
    }


    private void populateMarkers(LatLng zoonLatLng, @NonNull List<ClassLocationData> locations) {
        if (locations.size() == 0) {
            messageHelper.show("No locations found for this query");
            return;
        }
        LatLng latlng;
        MarkerOptions markerOption;
        markerList.clear();
        mGoogleMap.clear();
        double latSum = 0, lngSum = 0;
        for (ClassLocationData location : locations) {
            latlng = new LatLng(Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
            latSum = latSum + Double.valueOf(location.getLatitude());
            lngSum = lngSum + Double.valueOf(location.getLongitude());
            markerOption = new MarkerOptions().position(latlng).title(location.getLocationArea())
                    .icon(getPinIcon(location));
            markerList.add(markerOption);
            mGoogleMap.addMarker(markerOption).setTag(location.getLatitude() + "," + location.getLongitude());
        }
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(zoonLatLng == null ? new LatLng(latSum / locations.size(), lngSum / locations.size()) : zoonLatLng, zoom));
    }

    @SuppressWarnings({"MissingPermission"})
    public void initLocationFinder(final RxLocation rxLocation, LocationRequest locationRequest) {
        rxLocation.location().updates(locationRequest).subscribe(new Consumer<Location>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Location location) throws Exception {
                /*latitude = "" + location.getLatitude();
                longitude = "" + location.getLongitude();
                refreshMapPinsToNewLocation(null);*/
                LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOption = new MarkerOptions().position(myLocation).title("Your Location").
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_0));
                mGoogleMap.addMarker(markerOption).setTag("my location" + "," + "");
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.d("Location", "accept: ");
            }
        });

    }

    public void setNewAddress(LatLng latLng, String address) {
        locationName.set(address);
        zoom = 13.0f;
        radius = "10";
        refreshMapPinsToNewLocation(latLng);
    }

    private BitmapDescriptor getPinIcon(ClassLocationData data) {
        return BitmapDescriptorFactory.fromResource(pinColorMap.get(data.getColorCode()));
    }

}

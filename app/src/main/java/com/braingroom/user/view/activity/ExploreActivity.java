package com.braingroom.user.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.viewmodel.ExploreViewModel;
import com.braingroom.user.viewmodel.ViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.patloew.rxlocation.RxLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

import static com.braingroom.user.viewmodel.ViewModel.PLACE_AUTOCOMPLETE_REQUEST_CODE;

public class ExploreActivity extends BaseActivity {

    SupportMapFragment mapFragment;
    RxPermissions rxPermissions;
    RxLocation rxLocation;
    LocationRequest locationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                ((ExploreViewModel) vm).setGoogleMap(googleMap);
            }
        });

        rxLocation = new RxLocation(this);
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(2)
                .setInterval(5000);


        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Boolean granted) throws Exception {
                if (granted) {
                    ((ExploreViewModel) vm).enableLocationPermission();
                    ((ExploreViewModel) vm).initLocationFinder(rxLocation, locationRequest);
                } else {
                    getMessageHelper().show("Permission not granted, showing location as chennai");
                }
            }
        });
    }


    @NonNull
    @Override
    protected ViewModel createViewModel() {
        return new ExploreViewModel(getMessageHelper(), getNavigator(),getHelperFactory().createDialogHelper());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_explore;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                ((ExploreViewModel) vm).setNewAddress(place.getLatLng(),place.getName().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i("", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

    }
}

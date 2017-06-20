package com.braingroom.user.viewmodel.fragment;

import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.model.dto.ProfileData;
import com.braingroom.user.model.dto.VendorProfileData;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.FileUtils;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.ConnectivityViewModel;
import com.braingroom.user.viewmodel.DataItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class VendorProfileViewModel extends ViewModel {

    public final DataItemViewModel image = new DataItemViewModel(null);
    public final DataItemViewModel name = new DataItemViewModel(null);
    public final DataItemViewModel locality = new DataItemViewModel(null);
    public final DataItemViewModel city = new DataItemViewModel(null);
    public final DataItemViewModel interest = new DataItemViewModel("");
    public final DataItemViewModel institution = new DataItemViewModel(null);
    public final DataItemViewModel regId = new DataItemViewModel(null);
    public final DataItemViewModel expertise = new DataItemViewModel(null);
    public final DataItemViewModel address = new DataItemViewModel(null);
    public final DataItemViewModel description = new DataItemViewModel(null);
    public final DataItemViewModel experience = new DataItemViewModel(null);
    public final DataItemViewModel gender = new DataItemViewModel(null);


    public VendorProfileViewModel(@NonNull final Navigator navigator, final String vendorId) {
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
            }
        });

        FieldUtils.toObservable(callAgain).flatMap(new Function<Integer, Observable<VendorProfileData>>() {
            @Override
            public Observable<VendorProfileData> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getVendorProfile(vendorId).onErrorReturn(new Function<Throwable, VendorProfileData>() {
                    @Override
                    public VendorProfileData apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return null;
                    }
                });
            }
        }).subscribe(new Consumer<VendorProfileData>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull VendorProfileData data) throws Exception {
               if (data==null)
                   return;
                callAgain=null;
                image.s_1.set(null);
                name.s_1.set(data.getName());
                locality.s_1.set(data.getLocality());
                city.s_1.set(data.getCity());
                interest.s_1.set(data.getCategoryName());
                institution.s_1.set(data.getInstitution());
                regId.s_1.set(null);
                expertise.s_1.set(data.getExpertiseArea());
                experience.s_1.set(null);
                address.s_1.set(data.getAddress());
                description.s_1.set(data.getDescription());
                gender.s_1.set(null);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                Log.d("VendorProfile", "accept: " + throwable.getMessage());
            }
        });

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
    @Override
    public void retry(){
        callAgain.set(callAgain.get()+1);
        connectivityViewmodel.isConnected.set(true);
    }
}

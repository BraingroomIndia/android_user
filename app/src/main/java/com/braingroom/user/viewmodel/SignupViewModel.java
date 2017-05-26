package com.braingroom.user.viewmodel;

import android.support.annotation.NonNull;

import com.braingroom.user.R;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import io.reactivex.functions.Action;

public class SignupViewModel extends ViewModel {

//    public final DataItemViewModel fullName = new DataItemViewModel("");
//    public final DataItemViewModel email = new DataItemViewModel("");
//    public final DataItemViewModel password = new DataItemViewModel("");
//    public final DataItemViewModel confirmPassword = new DataItemViewModel("");
//    public final DataItemViewModel mobileNumber = new DataItemViewModel("");
//    public final DataItemViewModel institute = new DataItemViewModel("");
//    public final DataItemViewModel passoutYear = new DataItemViewModel("");
//    public final DataItemViewModel dob = new DataItemViewModel("");
//    public final DataItemViewModel gender = new DataItemViewModel("");
//    public final DataItemViewModel communityClass = new DataItemViewModel("");
//    public final SpinnerViewModel country;
//    public final SpinnerViewModel locality;
//    public final SpinnerViewModel city;

    public final ImageUploadViewModel imageUploadVm;

    public final Action onBackClicked, onSubmitClicked;

    public SignupViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {

        imageUploadVm = new ImageUploadViewModel(messageHelper, navigator, R.drawable.avatar_male, null);
//        city = new SpinnerViewModel(apiService.getCityList(), "Select a city");
//        city.selectedIdx.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                locality.refreshData(apiService.getLocalityList(city.getSelectedItemId()));
//            }
//        });
//        locality = new SpinnerViewModel(apiService.getLocalityList("-1"), "Select a locality");
//        apiService.getProfile().subscribe(new Consumer<ProfileData>() {
//            @Override
//            public void accept(@io.reactivex.annotations.NonNull ProfileData data) throws Exception {
////                fullName.s_1.set(data.getName());
////                email.s_1.set(data.getEmail());
////                contact.s_1.set(data.getContactNo());
////                ugInstitution.s_1.set(data.getUgInstituteName());
////                pgInstitution.s_1.set(data.getPgInstituteName());
////                ugPassoutYear.s_1.set(data.getUgInstitutePassingYear());
////                pgPassoutYear.s_1.set(data.getPgInstitutePassingYear());
//                imageUploadVm.remoteAddress.set(data.getProfileImage());
//                city.setDefaultSelectedIndex(data.getCity());
//                locality.setDefaultSelectedIndex(data.getLocality());
//            }
//        }, new Consumer<Throwable>() {
//            @Override
//            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
//
//            }
//        });

        onSubmitClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.finishActivity();
            }
        };


        onBackClicked = new Action() {
            @Override
            public void run() throws Exception {
                navigator.finishActivity();
            }
        };

    }
}

package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.dto.ClassLocationData;
import com.braingroom.user.model.response.WishlistResp;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CheckoutActivity;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.VendorProfileActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.ConnectableObservable;
import lombok.Setter;

import static com.rollbar.android.Rollbar.TAG;

public class ClassDetailViewModel extends ViewModel {

    public ClassData mClassData;

    String defaultLink = "https://www.braingroom.com/Vendor/defult_pic.jpg";
    public final ObservableField<String> imagePath = new ObservableField<>(null);
    public final ObservableField<String> rating = new ObservableField<>("");
    public final ObservableField<String> price = new ObservableField<>(null);
    public final ObservableField<String> teacherPic = new ObservableField<>(null);
    public final ObservableField<String> teacherName = new ObservableField<>(null);
    public final ObservableField<String> description = new ObservableField<>(null); //Edited By Vikas Godara
    public final ObservableField<String> sessionDurationInfo = new ObservableField<>(null);
    public final ObservableField<String> videoId = new ObservableField<>(null);
    public final ObservableField<String> classTopic = new ObservableField<>(null);
    public ObservableField<String> fixedClassDate = new ObservableField<>();
    public ObservableBoolean isMapVisible = new ObservableBoolean(true);
    public ObservableBoolean isYouTube = new ObservableBoolean(true);
    public String vendorId;
    public ObservableField<Boolean> isShimmerOn = new ObservableField<>(true);
    public final ConnectableObservable<List<ViewModel>> addresses;
    List<ViewModel> addressList = new ArrayList<>();
    List<ClassLocationData> locationList = new ArrayList<>();
    List<MarkerOptions> markerList = new ArrayList<>();
    public ObservableField<Integer> retry = new ObservableField<>(0);

    private GoogleMap mGoogleMap;
    YouTubePlayer youTubePlayer;
    @NonNull
    final MessageHelper messageHelper;
    @NonNull
    final Navigator navigator;
    //    @NonNull
//    final  HelperFactory helperFactory;
    @Setter
    ClassDetailActivity.UiHelper uiHelper;

    public final Action onBookClicked, onShowDetailAddressClicked, onVendorProfileClicked,
            onGiftClicked, onPeopleNearYou, onConnect, onGetTutor, openConnectTnT, openConnectBnS, openConnectFP;

    public boolean isInWishlist = false;

    public ClassDetailViewModel(@NonNull final HelperFactory helperFactory, final ClassDetailActivity.UiHelper uiHelper, @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator, final String classId) {
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
                Log.d(TAG, "run: " + callAgain.get());
            }
        });
        addresses = Observable.just(addressList).publish();
        this.messageHelper = messageHelper;
        this.navigator = navigator;
//        this.helperFactory=helperFactory;
        this.uiHelper = uiHelper;
        openConnectTnT = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                data.putString("defMinorCateg", "tips_tricks");
                navigator.navigateActivity(ConnectHomeActivity.class, data);
            }
        };
        openConnectBnS = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                data.putString("defMinorCateg", "group_post");
                navigator.navigateActivity(ConnectHomeActivity.class, data);

            }
        };
        openConnectFP = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                data.putString("defMinorCateg", "activity_request");
                navigator.navigateActivity(ConnectHomeActivity.class, data);

            }
        };
        FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return !apiSuccessful;
            }
        }).flatMap(new Function<Integer, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return apiService.getClassDetail(classId).map(new Function<ClassData, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@io.reactivex.annotations.NonNull ClassData classData) throws Exception {

                        mClassData = classData;
                        if (classData.getClassType().equalsIgnoreCase("Online Classes") || classData.getClassType().equalsIgnoreCase("Webinars"))//Edited by Vikas Godara;
                            isMapVisible.set(false);
                        vendorId = classData.getTeacherId();
                        imagePath.set(classData.getImage()); //Edited by Vikas Godara
                        rating.set("" + classData.getRating());
                        price.set(classData.getLevelDetails().get(0).getPrice() == null ?
                                classData.getLevelDetails().get(0).getGroups().get(1).getPrice() : classData.getLevelDetails().get(0).getPrice());
                        teacherPic.set(classData.getTeacherPic());
                        teacherName.set(classData.getTeacher());
                        description.set(classData.getClassSummary().replace("$", "\n•")); //Edited By Vikas Godara
                        sessionDurationInfo.set(classData.getNoOfSession() + " Sessions, " + classData.getClassDuration());
                        classTopic.set(classData.getClassTopic());

                        if ("1".equals(classData.getWishlist()))
                            isInWishlist = true;
                        if ("fixed".equalsIgnoreCase(classData.getClassTypeData())) {
                            fixedClassDate.set(classData.getSessionTime() + ", " + classData.getSessionDate());
                        }
                        if (isMapVisible.get())
                            for (final ClassLocationData classLocationData : classData.getLocation()) {
                                locationList.add(classLocationData);
                                addressList.add(new DataItemViewModel(classLocationData.getLocality(), false, new MyConsumer<DataItemViewModel>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull DataItemViewModel dataItemViewModel) {
                                        messageHelper.showDismissInfo(null, classLocationData.getLocationArea());
                                    }
                                }, null));
                            }
                        addresses.connect();

                        if (classData.getVideoId() != null && !classData.getVideoId().equalsIgnoreCase(defaultLink)) {//Edited By Vikas Godara

                            if (classData.getVideoId().contains("www.youtube.com/embed")) {
                                isYouTube.set(true);
                                videoId.set(classData.getVideoId().substring(classData.getVideoId().lastIndexOf('/') + 1));
                            } else {
                                isYouTube.set(false);
                                videoId.set(classData.getVideoId());
                            }
                        }
                        uiHelper.stopShimmer();
                        isShimmerOn.set(false);
                        apiSuccessful = true;
                        if (isMapVisible.get())
                            if (mGoogleMap != null && markerList.size() == 0)
                                populateMarkers(locationList);
                        uiHelper.invalidateMenu();
                        if (youTubePlayer == null && videoId.get() != null && isYouTube.get()) {
                            uiHelper.initYoutube();
                        }

                        return Observable.empty();

                    }
                }).onErrorReturn(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return Observable.empty();
                    }
                });
            }
        }).subscribe();

                /*  })subscribe(new Consumer<ClassData>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ClassData classData) throws Exception {
                        uiHelper.stopShimmer();
                        isShimmerOn.set(false);
                        mClassData = classData;
                        if (classData.getClassType().equalsIgnoreCase("Online Classes") || classData.getClassType().equalsIgnoreCase("Webinars"))//Edited by Vikas Godara;
                            isMapVisible.set(false);
                        vendorId = classData.getTeacherId();
                        imagePath.set(classData.getImage()); //Edited by Vikas Godara
                        rating.set("" + classData.getRating());
                        price.set(classData.getLevelDetails().get(0).getPrice() == null ?
                                classData.getLevelDetails().get(0).getGroups().get(1).getPrice() : classData.getLevelDetails().get(0).getPrice());
                        teacherPic.set(classData.getTeacherPic());
                        teacherName.set(classData.getTeacher());
                        description.set(classData.getClassSummary().replace("$", "\n•")); //Edited By Vikas Godara
                        sessionDurationInfo.set(classData.getNoOfSession() + " Sessions, " + classData.getClassDuration());
                        classTopic.set(classData.getClassTopic());

                        if ("1".equals(classData.getWishlist())) {
                            isInWishlist = true;
                            uiHelper.invalidateMenu();
                        }
                        if ("fixed".equalsIgnoreCase(classData.getClassTypeData())) {
                            fixedClassDate.set(classData.getSessionTime() + ", " + classData.getSessionDate());
                        }
                        if (isMapVisible.get())
                            for (final ClassLocationData classLocationData : classData.getLocation()) {
                                locationList.add(classLocationData);
                                addressList.add(new DataItemViewModel(classLocationData.getLocality(), false, new MyConsumer<DataItemViewModel>() {
                                    @Override
                                    public void accept(@io.reactivex.annotations.NonNull DataItemViewModel dataItemViewModel) {
                                        messageHelper.showDismissInfo(null, classLocationData.getLocationArea());
                                    }
                                }, null));
                            }
                        addresses.connect();
                        if (isMapVisible.get())
                            if (mGoogleMap != null && markerList.size() == 0) populateMarkers(locationList);

                        if (classData.getVideoId() != null && !classData.getVideoId().equalsIgnoreCase(defaultLink)) {//Edited By Vikas Godara

                            if (classData.getVideoId().contains("www.youtube.com/embed")) {
                                isYouTube.set(true);
                                videoId.set(classData.getVideoId().substring(classData.getVideoId().lastIndexOf('/') + 1));
                            } else {
                                isYouTube.set(false);
                                videoId.set(classData.getVideoId());
                            }
                        }
                        if (youTubePlayer == null && videoId.get() != null && isYouTube.get()) {
                            uiHelper.initYoutube();
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                    }
                });
                return Observable.empty();

            }
        });*/

        onBookClicked = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        if (mClassData != null) {
                            Bundle data = new Bundle();
                            data.putSerializable("classData", mClassData);
                            navigator.navigateActivity(CheckoutActivity.class, data);
                        }
                    }
                }

        ;
        onShowDetailAddressClicked = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        messageHelper.show("coming soon.");
                    }
                }

        ;
        onVendorProfileClicked = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        Bundle data = new Bundle();
                        data.putString("id", vendorId);
                        navigator.navigateActivity(VendorProfileActivity.class, data);
                    }
                }

        ;
        //Edited by Vikas Godara
        onGiftClicked = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        List<String> message = new ArrayList<>();
                        message.add("Gift class feature will be add soon");
                        helperFactory.createDialogHelper().showListDialog("Coming Soon", message);

                    }
                }

        ;
        onPeopleNearYou = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        List<String> message = new ArrayList<>();
                        message.add("People near you feature will be add soon");
                        helperFactory.createDialogHelper().showListDialog("Coming Soon", message);

                    }
                }

        ;
        onConnect = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        navigator.navigateActivity(ConnectHomeActivity.class, Bundle.EMPTY);
                    }
                }

        ;
        onGetTutor = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        helperFactory.createDialogHelper().showCustomView(R.layout.content_contact_admin_dailog, new ContactAdminDialogViewModel(messageHelper, navigator, classId), false);

                    }
                }

        ;
        //Edited By Vikas Godara

    }

    public void setGoogleMap(@NonNull GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        if (locationList.size() > 0) populateMarkers(locationList);
    }

    public void setYoutubePlayer(@NonNull YouTubePlayer player) {
        this.youTubePlayer = player;
        this.youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
        if (videoId.get() != null) player.cueVideo(videoId.get());
    }

    private void populateMarkers(@NonNull List<ClassLocationData> locations) {
        LatLng latlng = null;
        MarkerOptions markerOption;
        for (ClassLocationData location : locations) {
            latlng = new LatLng(Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
            markerOption = new MarkerOptions().position(latlng).title(location.getLocationArea());
            markerList.add(markerOption);
            mGoogleMap.addMarker(markerOption);
        }
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10.0f));
    }

    public void releaseYoutube() {
        if (youTubePlayer != null) {
            try {
                youTubePlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addToWishlist() {
        apiService.addToWishlist(mClassData.getId()).subscribe(new Consumer<WishlistResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull WishlistResp wishlistResp) throws Exception {
                isInWishlist = !isInWishlist;
                uiHelper.invalidateMenu();
                messageHelper.show(wishlistResp.getResMsg());
            }
        });
    }

    public void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this class I found at Braingroom : " + mClassData.getClassWebUrl());
        navigator.navigateActivity(Intent.createChooser(shareIntent, "Share link using"));

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

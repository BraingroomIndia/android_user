package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.braingroom.user.R;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.dto.ClassLocationData;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.dto.ListDialogData1;
import com.braingroom.user.model.request.DecideAndDiscussPostReq;
import com.braingroom.user.model.request.PromoCodeReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.CODOfferDetailResp;
import com.braingroom.user.model.response.CategoryTreeResp;
import com.braingroom.user.model.response.ContactTutorResp;
import com.braingroom.user.model.response.PromoInfo;
import com.braingroom.user.model.response.ReviewGetResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.model.response.WishlistResp;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.FieldUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.utils.MyConsumer;
import com.braingroom.user.utils.MyConsumerString;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.CheckoutActivity;
import com.braingroom.user.view.activity.ClassDetailActivity;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.VendorProfileActivity;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import lombok.Setter;


public class ClassDetailViewModel extends ViewModel {

    public ClassData mClassData;

    private static String PRICE_TYPE_PER_PERSON = "perPerson";
    private static String PRICE_TYPE_GROUP = "Group";

    private String defaultLink = "https://www.braingroom.com/Vendor/defult_pic.jpg";
    public final ObservableField<String> imagePath = new ObservableField<>(null);
    public final ObservableField<String> videoThumb = new ObservableField<>(null);
    public final ObservableField<String> rating = new ObservableField<>("");
    public final ObservableField<Spanned> price = new ObservableField<>(null);
    public final ObservableField<String> teacherPic = new ObservableField<>(null);
    public final ObservableField<String> teacherName = new ObservableField<>(null);
    public final ObservableField<Spanned> description = new ObservableField<>(null); //Edited By Vikas Godara
    public final ObservableField<String> sessionDurationInfo = new ObservableField<>(null);
    private final ObservableField<String> videoId = new ObservableField<>(null);
    public final ObservableField<String> classTopic = new ObservableField<>(null);
    public final ObservableField<String> catalogDescription = new ObservableField<>(null);
    public final ObservableField<String> classProvider = new ObservableField<>(null);
    private final ObservableArrayList<String> catalogLocationList = new ObservableArrayList<>();
    public final ObservableField<String> aboutAcademy = new ObservableField<>("");
    public final ObservableField<Spanned> locationConcat = new ObservableField<>();
    public ObservableField<String> fixedClassDate = new ObservableField<>();
    public ObservableBoolean isMapVisible = new ObservableBoolean(true);
    private ObservableBoolean isYouTube = new ObservableBoolean(true);
    private String vendorId;
    public ObservableBoolean isShimmerOn = new ObservableBoolean(true);
    public final ConnectableObservable<List<ViewModel>> addresses;
    public final ConnectableObservable<List<ViewModel>> reviews;
    List<ViewModel> addressList = new ArrayList<>();
    List<ViewModel> reviewList = new ArrayList<>();
    List<ClassLocationData> locationList = new ArrayList<>();
    List<MarkerOptions> markerList = new ArrayList<>();

    public ObservableField<Integer> retry = new ObservableField<>(0);

    public final DataItemViewModel title;
    public final ObservableField<String> postDescription;

    public final ListDialogViewModel1 phoneNumber;
    private LinkedHashMap<String, Integer> PhoneListApiData = new LinkedHashMap<>();
    private Consumer<HashMap<String, Integer>> callConsumer;

    public boolean isGift = false;


    public final Action callTutor;
    private GoogleMap mGoogleMap;
    YouTubePlayer youTubePlayer;
    @NonNull
    final MessageHelper messageHelper;
    @NonNull
    final Navigator navigator;
    @Setter
    ClassDetailActivity.UiHelper uiHelper;

    public ObservableBoolean hideViewMore = new ObservableBoolean(false);

    public ConnectFilterData connectFilterDataKNN = new ConnectFilterData();
    public ConnectFilterData connectFilterDataBNS = new ConnectFilterData();
    public ConnectFilterData connectFilterDataFP = new ConnectFilterData();
    public ConnectFilterData connectFilterData = new ConnectFilterData();
    public final ImageUploadViewModel imageUploadViewModel;


    public final Action onBookClicked, onShowDetailAddressClicked, onVendorProfileClicked, getQuoteClicked,
            onPayTutorClicked, onPeopleNearYou, onConnect, onGetTutor, onQueryClicked, onSubmitPostClicked, /*openConnectTnT,
            openConnectBnS, openConnectFP,*/
            openCateglogLocationList, playAction, onQueryDismiss, onPostDismiss, onAddReviewClicked;

    public boolean isInWishlist = false;

    public static final String KNK = "Knowledge & Nugget";
    public static final String BNS = "Buy & Sell";
    public static final String AP = "Activity partner";

    public final MyConsumerString expandAction, collapseAction;

    public ClassDetailViewModel(@NonNull final FirebaseAnalytics mFirebaseAnalytics, @NonNull final Tracker mTracker, @NonNull final HelperFactory helperFactory, final ClassDetailActivity.UiHelper uiHelper, @NonNull final MessageHelper messageHelper,
                                @NonNull final Navigator navigator, @NonNull final String classId, final String origin, final String catalogueId, final String promo, final String isIncentive) {

        this.mFirebaseAnalytics = mFirebaseAnalytics;
        this.mTracker = mTracker;
        this.connectivityViewmodel = new ConnectivityViewModel(new Action() {
            @Override
            public void run() throws Exception {
                retry();
                Log.d(TAG, "run: " + callAgain.get());
            }
        });
        if (!isEmpty(promo))
            apiService.getPromoInfo(promo).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<PromoInfo>() {
                @Override
                public void accept(@io.reactivex.annotations.NonNull PromoInfo resp) throws Exception {
                    if (!isEmpty(resp) && !isEmpty(resp.data) && !isEmpty(resp.data.content))
                        messageHelper.showDismissInfo(resp.data.title, CommonUtils.fromHtml(resp.data.content));
                }
            });

        imageUploadViewModel = new ImageUploadViewModel(R.drawable.avatar_male, "");
        connectFilterData.setMajorCateg("learners_forum");
        connectFilterDataKNN.setMajorCateg("learners_forum");
        connectFilterDataBNS.setMajorCateg("learners_forum");
        connectFilterDataFP.setMajorCateg("learners_forum");
        connectFilterDataKNN.setMinorCateg("tips_tricks");
        connectFilterDataBNS.setMinorCateg("group_post");
        connectFilterDataFP.setMinorCateg("activity_request");

        PhoneListApiData.put("044-49507392", 1);
        PhoneListApiData.put("044-65556012", 2);
        PhoneListApiData.put("044-65556013", 3);
        ;
        addresses = Observable.just(addressList).publish();
        reviews = Observable.just(reviewList).publish();
        this.messageHelper = messageHelper;
        this.navigator = navigator;
//        this.helperFactory=helperFactory;
        this.uiHelper = uiHelper;
        isMapVisible.set(!ClassListViewModel1.ORIGIN_CATALOG.equals(origin));
        this.title = new DataItemViewModel("");
        this.postDescription = new ObservableField<>("");

        isGift = ClassListViewModel1.ORIGIN_GIFT.equals(origin);
        expandAction = new MyConsumerString() {
            @Override
            public void accept(String s) {
                uiHelper.expandDemoClass(s);
            }
        };
        collapseAction = new MyConsumerString() {
            @Override
            public void accept(String s) {
                uiHelper.compressDemoClass(s);
            }
        };
        openCateglogLocationList = new Action() {
            @Override
            public void run() throws Exception {
                messageHelper.showDismissInfo("Locations", TextUtils.join("\n", catalogLocationList));

            }
        };
        onQueryClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (getLoggedIn())
                    uiHelper.postQueryForm();
                else {
                    Bundle data = new Bundle();
                    data.putString("backStackActivity", ClassDetailActivity.class.getSimpleName());
                    data.putSerializable("id", classId);
                    data.putSerializable("origin", ClassListViewModel1.ORIGIN_HOME);
                    messageHelper.showLoginRequireDialog("Please login to post a query", data);
                }

            }
        };

        onSubmitPostClicked = new Action() {
            @Override
            public void run() throws Exception {
                DecideAndDiscussPostReq.Snippet decideAndDiscussSnippet = new DecideAndDiscussPostReq.Snippet();
                if (("").equals(postDescription.get())) {
                    messageHelper.show("Please enter description");
                    return;
                }
                decideAndDiscussSnippet.setUuid(pref.getString(Constants.UUID, ""));
                decideAndDiscussSnippet.setPostType("user_post");
                decideAndDiscussSnippet.setSegmentId(mClassData.getSegmentId());
                decideAndDiscussSnippet.setCategoryId(mClassData.getCategoryId());
                decideAndDiscussSnippet.setPostTitle(title.s_1.get());
                decideAndDiscussSnippet.setPostSummary(postDescription.get());
                apiService.postDecideDiscuss(decideAndDiscussSnippet).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                        onPostDismiss.run();
                        messageHelper.show(baseResp.getResMsg());
                        Bundle data = new Bundle();
                        ConnectFilterData connectFilterData = new ConnectFilterData();
                        connectFilterData.setMajorCateg(ConnectHomeActivity.TUTORS_ARTICLE);
                        connectFilterData.setMinorCateg(ConnectHomeActivity.DISCUSS_DECIDE);
                        data.putSerializable(Constants.connectFilterData, connectFilterData);
                        navigator.navigateActivity(ConnectHomeActivity.class, data);

                    }
                });

            }
        };
        onPostDismiss = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.next();
            }
        };
        callTutor = new Action() {
            @Override
            public void run() throws Exception {
                if (getLoggedIn())
                    apiService.contactTutor(classId).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<ContactTutorResp>() {
                                @Override
                                public void accept(@io.reactivex.annotations.NonNull final ContactTutorResp resp) throws Exception {
                                    if (resp.getData().isEmpty())
                                        messageHelper.showDismissInfo("", resp.getResMsg());
                                    else {
                                        messageHelper.showAcceptableInfo("Offer", resp.getData().get(0).getDisplayText(), "Call Tutor", new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
                                                uiHelper.makeACall(resp.getData().get(0).getMobileNumber());
                                            }
                                        });
                                    }
                                }
                            });
                else
                    messageHelper.showLoginRequireDialog("Only logged in user can call tutor", null);
            }
        };
        onAddReviewClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (getLoggedIn())
                    uiHelper.addReview();
                else
                    messageHelper.showLoginRequireDialog("Only logged in user can add review", null);
            }
        };

        callConsumer = new Consumer<HashMap<String, Integer>>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull HashMap<String, Integer> selectedMap) throws Exception {

                String number = null;
                if (selectedMap.values().iterator().hasNext()) {
                    number = android.text.TextUtils.join("", selectedMap.keySet());

                }
                if (number != null) {
                    uiHelper.makeACall(number);

                }
            }

        };
        phoneNumber = new ListDialogViewModel1(helperFactory.createDialogHelper(), "Phone Number", messageHelper, Observable.just((new ListDialogData1(PhoneListApiData))), new HashMap<String, Integer>(), false, callConsumer, "");
        phoneNumber.setPositiveText("Call");
        apiService.getReview(Constants.classReview, classId).subscribe(new Consumer<ReviewGetResp>() {
            @Override
            public void accept(ReviewGetResp reviewGetResp) throws Exception {
                if (reviewGetResp.getResCode()) {
                    for (ReviewGetResp.Snippet snippet : reviewGetResp.getData()) {
                        reviewList.add(new ReviewItemViewModel(snippet.getRating(), snippet.getFirstName(), snippet.getReviewMessage(), snippet.getTimeStamp()));
                    }

                } else
                    reviewList.add(new EmptyItemViewModel(R.drawable.ic_no_post_64dp, null, "No review found", null));
                reviews.connect();
            }
        });
        FieldUtils.toObservable(callAgain).filter(new Predicate<Integer>() {
            @Override
            public boolean test(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                return !apiSuccessful;
            }
        }).flatMap(new Function<Integer, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(@io.reactivex.annotations.NonNull Integer integer) throws Exception {
                final int isCatalogue;
                if (ClassListViewModel1.ORIGIN_CATALOG.equals(origin))
                    isCatalogue = 1;
                else isCatalogue = 0;
                return apiService.getClassDetail(classId, isCatalogue).onErrorReturn(new Function<Throwable, ClassData>() {
                    @Override
                    public ClassData apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        return new ClassData();
                    }
                }).observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassData, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@io.reactivex.annotations.NonNull ClassData classData) throws Exception {
                        if (classData.getId() == null)
                            return Observable.empty();

//                        ZohoSalesIQ.Tracking.setPageTitle(classData.getClassTopic());

                        //FireBase Tracking
                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, classData.getId());
                        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, classData.getClassTopic());
                        if (isCatalogue == 0)
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Normal Class");
                        else
                            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Catalogue Class");
                        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                        //FireBase Tracking
                        connectFilterDataKNN.setCategId(classData.getCategoryId());
                        connectFilterDataKNN.setGroupId(classData.getGroupId());
                        connectFilterDataBNS.setCategId(classData.getCategoryId());
                        //connectFilterDataBNS.setSegId(classData.getSegmentId());
                        connectFilterDataFP.setCategId(classData.getCategoryId());
                        //  connectFilterDataFP.setSegId(classData.getSegmentId());
                        mClassData = classData;
                        if (classData.getClassType().equalsIgnoreCase("Online Classes") || classData.getClassType().equalsIgnoreCase("Webinars"))//Edited by Vikas Godara;
                            isMapVisible.set(false);
                        vendorId = classData.getTeacherId();
                        imagePath.set(classData.getImage()); //Edited by Vikas Godara
                        rating.set("" + classData.getRating());
                        if (classData.getIsCoupleClass() != 1)
                            if (classData.getPricingType().equalsIgnoreCase(PRICE_TYPE_PER_PERSON))
                                price.set(CommonUtils.fromHtml(classData.getPriceSymbolNonSpanned() + classData.getLevelDetails().get(0).getPrice()));
                            else
                                price.set(CommonUtils.fromHtml(classData.getPriceSymbolNonSpanned() + classData.getLevelDetails().get(0).getGroups().get(1).getPrice()));
                        else
                            price.set(CommonUtils.fromHtml(classData.getPriceSymbolNonSpanned() + classData.getLevelDetails().get(0).getGroups().get(0).getPrice()));
                        imageUploadViewModel.setRemoteAddress(classData.getTeacherPic());
                        teacherPic.set(classData.getTeacherPic());
                        teacherName.set(classData.getClassProvider());
                        try {
                            description.set(Html.fromHtml(classData.getClassSummary())); //Edited By Vikas Godara

                        } catch (Exception e) {
                            Log.d(TAG, "description:" + e.toString());
                            // e.printStackTrace();
                        }

                        sessionDurationInfo.set(classData.getNoOfSession() + " Sessions, " + classData.getClassDuration());
                        classTopic.set(classData.getClassTopic());
                        title.s_1.set(classTopic.get() + "\n");

                        if (ClassListViewModel1.ORIGIN_CATALOG.equals(origin)) {


                            aboutAcademy.set(classData.getAboutAcademy());
                            catalogDescription.set(classData.getCatalogDescription());
                            classProvider.set(classData.getClassProvider());
                            catalogLocationList.addAll(classData.getCatalogLocations());
                            locationConcat.set(CommonUtils.fromHtml(TextUtils.join("&emsp;&#9679; ", classData.getCatalogLocations())));

                        }

                        if ("1".equals(classData.getWishlist()))
                            isInWishlist = true;
                        if ("fixed".equalsIgnoreCase(classData.getClassTypeData())) {
                            fixedClassDate.set(classData.getSessionTime() + ", " + classData.getSessionDate());
                        }
                        if (isMapVisible.get() && !isEmpty(classData.getLocation()))
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
                                videoThumb.set(videoId.get() == null ? null : "http://img.youtube.com/vi/" + videoId.get() + "/hqdefault.jpg");
                            } else if (classData.getVideoId() != null && !classData.getVideoId().equals("")) {
                                isYouTube.set(false);
                                videoThumb.set("");
                                videoId.set(classData.getVideoId());
                            }

                        }
                        uiHelper.stopShimmer();
                        hideViewMore.set(uiHelper.isViewEllipsized());
                        isShimmerOn.set(false);
                        apiSuccessful = true;
                        if (isMapVisible.get())
                            if (mGoogleMap != null && markerList.size() == 0)
                                populateMarkers(locationList);
                        uiHelper.invalidateMenu();
                        setScreenName(classData.getClassTopic());
                        return Observable.empty();

                    }
                });
            }
        }).subscribe();

        onBookClicked = new Action() {
            @Override
            public void run() throws Exception {
                if (mClassData != null) {
                    Bundle data = new Bundle();
                    data.putSerializable("classData", mClassData);
                    data.putSerializable("checkoutType", "class");
                    data.putString(Constants.isIncentive, isIncentive);
                    data.putString(Constants.promoCode, promo);
                    navigator.navigateActivity(CheckoutActivity.class, data);
                }
            }
        };


        onShowDetailAddressClicked = new

                Action() {
                    @Override
                    public void run() throws Exception {
                        messageHelper.show("coming soon.");
                    }
                };
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
        onPayTutorClicked = new Action() {
            @Override
            public void run() throws Exception {

                if (mClassData != null) {
                    final Bundle data = new Bundle();
                    data.putSerializable("classData", mClassData);
                    data.putString(Constants.isIncentive, isIncentive);
                    data.putInt(Constants.paymentMode, Constants.paymentOffline);
                    apiService.getCODOfferDetail(new PromoCodeReq.Snippet(mClassData.getId(), promo)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                            subscribe(new Consumer<CODOfferDetailResp>() {
                                @Override
                                public void accept(CODOfferDetailResp resp) throws Exception {
                                    if (resp != null && resp.getData() != null && resp.getData().get(0) != null) {
                                        final CODOfferDetailResp.Snippet snippet = resp.getData().get(0);
                                        messageHelper.showAcceptableInfo(snippet.title, CommonUtils.fromHtml(snippet.getMessage()), new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                data.putFloat(Constants.discountFactor, snippet.getDiscountFactor());
                                                navigator.navigateActivity(CheckoutActivity.class, data);
                                            }
                                        });
                                    } else {
                                        data.putFloat(Constants.discountFactor, 1);
                                        navigator.navigateActivity(CheckoutActivity.class, data);
                                    }

                                }
                            });

                }
            }
        };

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
                };

        onQueryDismiss = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.next();
            }
        };
        getQuoteClicked = new
                Action() {
                    @Override
                    public void run() throws Exception {
                        if (getLoggedIn())
                            uiHelper.showQuoteForm();
                        else {
                            Bundle data = new Bundle();
                            data.putString("backStackActivity", ClassDetailActivity.class.getSimpleName());
                            data.putSerializable("id", classId);
                            data.putSerializable("origin", ClassListViewModel1.ORIGIN_CATALOG);
                            data.putSerializable("catalogueId", catalogueId);
                            messageHelper.showLoginRequireDialog("Please login to post a query", data);
                        }

                    }
                };

        playAction = new Action() {
            @Override
            public void run() throws Exception {
                if (videoId.get() != null && isYouTube.get()) {
                    navigator.openStandaloneYoutube(videoId.get());
                } else if (videoId.get() != null)
                    navigator.openStandaloneVideo(videoId.get());
            }
        };
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
            try {
                latlng = new LatLng(Double.valueOf(location.getLatitude()), Double.valueOf(location.getLongitude()));
                markerOption = new MarkerOptions().position(latlng).title(location.getLocationArea());
                markerList.add(markerOption);
                mGoogleMap.addMarker(markerOption);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 10.0f));
            } catch (Exception e) {
                Log.d(TAG, "populateMarkers: " + e.toString());
            }
        }

    }

    public void releaseYoutube() {
        if (youTubePlayer != null) {
            try {
                youTubePlayer.release();
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }
    }

    public void releaseGoogleMap() {
        if (mGoogleMap != null)
            try {
                mGoogleMap.clear();
            } catch (Exception e) {
                e.printStackTrace();
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
    public void retry() {
        connectivityViewmodel.isConnected.set(true);
        callAgain.set(callAgain.get() + 1);
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

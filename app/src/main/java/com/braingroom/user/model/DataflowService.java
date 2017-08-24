package com.braingroom.user.model;

import android.content.SharedPreferences;
import android.util.Log;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.dto.ClassListData;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.dto.PayUCheckoutData;
import com.braingroom.user.model.dto.ProfileData;
import com.braingroom.user.model.dto.VendorProfileData;
import com.braingroom.user.model.request.ArticleAndVideosPostReq;
import com.braingroom.user.model.request.BuyAndSellPostReq;
import com.braingroom.user.model.request.ChangeNotificationStatusReq;
import com.braingroom.user.model.request.ChangePasswordReq;
import com.braingroom.user.model.request.ChatMessageReq;
import com.braingroom.user.model.request.CityReq;
import com.braingroom.user.model.request.ClassDetailReq;
import com.braingroom.user.model.request.CommentReplyReq;
import com.braingroom.user.model.request.CommentViewReply;
import com.braingroom.user.model.request.CommentViewReplyReq;
import com.braingroom.user.model.request.CommonIdReq;
import com.braingroom.user.model.request.CommonUserIdReq;
import com.braingroom.user.model.request.CommonUuidReq;
import com.braingroom.user.model.request.ConnectDataReq;
import com.braingroom.user.model.request.ConnectFeedReq;
import com.braingroom.user.model.request.ConnectPostByIdReq;
import com.braingroom.user.model.request.ContactAdmin;
import com.braingroom.user.model.request.CouponCodeReq;
import com.braingroom.user.model.request.DecideAndDiscussPostReq;
import com.braingroom.user.model.request.ExploreReq;
import com.braingroom.user.model.request.FirstSocialLoginReq;
import com.braingroom.user.model.request.FollowReq;
import com.braingroom.user.model.request.GeneralFilterReq;
import com.braingroom.user.model.request.GiftCouponReq;
import com.braingroom.user.model.request.GuestUserReq;
import com.braingroom.user.model.request.InstituteReq;
import com.braingroom.user.model.request.KnowledgeNuggetsPostReq;
import com.braingroom.user.model.request.LearningPartnerPostReq;
import com.braingroom.user.model.request.LikeReq;
import com.braingroom.user.model.request.LocalityReq;
import com.braingroom.user.model.request.LoginReq;
import com.braingroom.user.model.request.LogoutReq;
import com.braingroom.user.model.request.MarkerDataReq;
import com.braingroom.user.model.request.MessageListReq;
import com.braingroom.user.model.request.MessageReplyReq;
import com.braingroom.user.model.request.PayUBookingDetailsReq;
import com.braingroom.user.model.request.PayUHashGenReq;
import com.braingroom.user.model.request.PostRelatedReq;
import com.braingroom.user.model.request.ProfileUpdateReq;
import com.braingroom.user.model.request.PromocodeReq;
import com.braingroom.user.model.request.QuoteReq;
import com.braingroom.user.model.request.RazorBuySuccessReq;
import com.braingroom.user.model.request.RazorSuccessReq;
import com.braingroom.user.model.request.ReferralCodeReq;
import com.braingroom.user.model.request.RegisterUserDeviceReq;
import com.braingroom.user.model.request.ReportReq;
import com.braingroom.user.model.request.SaveGiftCouponReq;
import com.braingroom.user.model.request.SearchReq;
import com.braingroom.user.model.request.SegmentReq;
import com.braingroom.user.model.request.SignUpReq;
import com.braingroom.user.model.request.SocialLoginReq;
import com.braingroom.user.model.request.StateReq;
import com.braingroom.user.model.request.SubmitOTPReq;
import com.braingroom.user.model.request.UserListReq;
import com.braingroom.user.model.request.WishlistReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.BookingHistoryResp;
import com.braingroom.user.model.response.CatalogueGroupResp;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.CategoryTreeResp;
import com.braingroom.user.model.response.ChangePasswordResp;
import com.braingroom.user.model.response.ChatListResp;
import com.braingroom.user.model.response.ClassListResp;
import com.braingroom.user.model.response.CommentListResp;
import com.braingroom.user.model.response.CommentReplyResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.CommunityResp;
import com.braingroom.user.model.response.CompetitionStatusResp;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.model.response.ConnectSectionResp;
import com.braingroom.user.model.response.ExploreResp;
import com.braingroom.user.model.response.FirstSocialLoginResp;
import com.braingroom.user.model.response.FollowResp;
import com.braingroom.user.model.response.GiftcardResp;
import com.braingroom.user.model.response.GroupResp;
import com.braingroom.user.model.response.GuestUserResp;
import com.braingroom.user.model.response.LikeResp;
import com.braingroom.user.model.response.LikedUsersListResp;
import com.braingroom.user.model.response.LoginResp;
import com.braingroom.user.model.response.MarkerDataResp;
import com.braingroom.user.model.response.MessageListResp;
import com.braingroom.user.model.response.NotificationCountResp;
import com.braingroom.user.model.response.NotificationListResp;
import com.braingroom.user.model.response.PayUBookingDetailsResp;
import com.braingroom.user.model.response.PayUHashResp;
import com.braingroom.user.model.response.ProfileResp;
import com.braingroom.user.model.response.PromocodeResp;
import com.braingroom.user.model.response.RazorSuccessResp;
import com.braingroom.user.model.response.ReferralCodeResp;
import com.braingroom.user.model.response.ReportResp;
import com.braingroom.user.model.response.SaveGiftCouponResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.model.response.ThirdPartyProfileResp;
import com.braingroom.user.model.response.UploadPostApiResp;
import com.braingroom.user.model.response.UploadResp;
import com.braingroom.user.model.response.VendorProfileResp;
import com.braingroom.user.model.response.VendorReviewResp;
import com.braingroom.user.model.response.WishlistResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.viewmodel.OTPReq;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.content.ContentValues.TAG;

public class DataflowService {

    @Inject
    @Named("api")
    UserApiService api;

    @Inject
    Gson gson;
    SharedPreferences sharedPreferences;

    @Inject
    @Named("defaultPref")
    public SharedPreferences pref;


    private InternetConnection internetConnection = new InternetConnection(UserApplication.getInstance());

    @Inject
    public DataflowService() {

    }

    public Observable<BaseResp> registerUserDevice() {
        return api.registerUserDevice(new RegisterUserDeviceReq(new RegisterUserDeviceReq.Snippet(pref.getString(Constants.FCM_TOKEN, ""))))
                .onErrorReturn(new Function<Throwable, BaseResp>() {
                    @Override
                    public BaseResp apply(@NonNull Throwable throwable) throws Exception {
                        return new BaseResp();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<LoginResp> login(String email, String password, String fcmToken) {
        LoginReq.Snippet snippet = new LoginReq.Snippet();
        snippet.setEmail(email);
        snippet.setPassword(password);
        snippet.setRegId(fcmToken);
        return api.login(new LoginReq(snippet)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LoginResp> socialLogin(final String name, final String profileImage, final String email, final String id, String fcmToken, String mobile) {
        SocialLoginReq.Snippet snippet = new SocialLoginReq.Snippet();
        snippet.setEmail(email);
        snippet.setFirstName(name);
        snippet.setSocialId(id);
        snippet.setPhone(mobile);
        snippet.setRegId(fcmToken);
        return api.socialLogin(new SocialLoginReq(snippet)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<FirstSocialLoginResp> firstSocialLogin(String userId, String emailId, String mobile, String referralCode) {
        FirstSocialLoginReq req = new FirstSocialLoginReq(new FirstSocialLoginReq.Snippet(userId, emailId, mobile, referralCode));
        return api.firstSocialLogin(req).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<LoginResp> forgotPassword(String email) {
        LoginReq.Snippet snippet = new LoginReq.Snippet();
        snippet.setEmail(email);
        return api.forgotPassword(new LoginReq(snippet)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ChangePasswordResp> changePassword(ChangePasswordReq.Snippet snippet) {
        ChangePasswordReq req = new ChangePasswordReq(snippet);
        return api.changePassword(req).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SignUpResp> signUp(SignUpReq signUpReq) {
        return api.BuyerRegistration(signUpReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, SignUpResp>() {
                    @Override
                    public SignUpResp apply(@NonNull Throwable throwable) throws Exception {
                        Log.d("SignUp", "apply: " + throwable.toString());
                        List<SignUpResp.Snippet> snippet = new ArrayList<SignUpResp.Snippet>(0);
                        SignUpResp resp = new SignUpResp(snippet);
                        resp.setResMsg("Some error occurred ");
                        resp.setResCode("0");
                        return resp;
                    }
                });
    }

    public Observable<ReferralCodeResp> checkReferal(String code) {
        return api.checkReferal(new ReferralCodeReq(new ReferralCodeReq.Snippet(code))).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, ReferralCodeResp>() {
            @Override
            public ReferralCodeResp apply(@NonNull Throwable throwable) throws Exception {
                return new ReferralCodeResp();
            }
        });
    }

    public Observable<BaseResp> submitOTP(SubmitOTPReq req) {
        return api.submitOTP(req).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> requestOTP(OTPReq req) {
        return api.sendOTP(req).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> logout() {
        LogoutReq.Snippet snippet = new LogoutReq.Snippet();
        snippet.setUserId(pref.getString(Constants.BG_ID, ""));
        snippet.setDeviceId(pref.getString(Constants.FCM_TOKEN, ""));
        return api.logout(new LogoutReq(snippet)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommunityResp> getCommunity() {
        return api.getCommunity().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CategoryResp> getCategory() {
        return api.getCategories().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<GroupResp> getGroups() {
        return api.getGroups(ConnectDataReq.getGroupRequest()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getGroupActivities(String groupIds) {
        return api.getGroupActivities(new CommonIdReq(new CommonIdReq.Snippet(groupIds))).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<SegmentResp> getSegments(String categoryId) {
        return api.getSegments(new SegmentReq(new SegmentReq.Snippet(categoryId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UploadResp> uploadImage(String filePath, String type) {
        return api.uploadImage("fileUpload/Image", prepareFilePart("uploadedfile", filePath, type)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getCityList(String stateId) {
        return api.getCityList(new CityReq(new CityReq.Snippet(stateId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getCatalogueCities() {
        return api.getCatalogueCities().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getVendors() {
        return api.getVendors().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getLocalityList(String cityId) {
        return api.getLocalities(new LocalityReq(new LocalityReq.Snippet(cityId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WishlistResp> addToWishlist(String classId) {
        return api.addToWishlist(new WishlistReq(new WishlistReq.Snippet(pref.getString(Constants.UUID, ""), classId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<ProfileData> getProfile(String userId) {
        return api.getProfile(new CommonIdReq(new CommonIdReq.Snippet(userId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ProfileResp, ProfileData>() {
                    @Override
                    public ProfileData apply(@NonNull ProfileResp resp) throws Exception {
                        return gson.fromJson(gson.toJson(resp.getData().get(0)), ProfileData.class);

                    }
                });
    }

    public Observable<CommonIdResp> updateProfile(ProfileUpdateReq req) {
        return api.updateProfile(req).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<PromocodeResp> applyPromoCode(PromocodeReq.Snippet snippet) {

        return api.applyPromoCode(new PromocodeReq((snippet))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<PromocodeResp> applyCouponCode(CouponCodeReq.Snippet snippet) {

        return api.applyCouponCode(new CouponCodeReq((snippet))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<ClassData>> classSearch(String categoryId, String keyword) {
        return api.classSearch(new SearchReq(new SearchReq.Snippet(categoryId, keyword))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassListResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull ClassListResp classListResp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>();
                        for (ClassListResp.Snippet snippet : classListResp.getData()) {
                            dataList.add(gson.fromJson(gson.toJson(snippet), ClassData.class));
                        }
                        return dataList;
                    }
                });
    }

    public Observable<ClassListData> generalFilter(GeneralFilterReq req, int pageIndex) {

        return api.generalFilter(pageIndex > 0 ? pageIndex + "" : "", req).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassListResp, ClassListData>() {
                    @Override
                    public ClassListData apply(@NonNull ClassListResp classListResp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>(0);

                        for (ClassListResp.Snippet snippet : classListResp.getData()) {
                            dataList.add(gson.fromJson(gson.toJson(snippet), ClassData.class));
                        }
                        ClassListData dataWrapper = new ClassListData();
                        dataWrapper.setNextPage(classListResp.getNextPage());
                        dataWrapper.setClassDataList(dataList);
                        return dataWrapper;
                    }
                });
    }


    public Observable<List<ClassData>> getWishList(int pageIndex) {
        return api.getWishlist(pageIndex > 1 ? pageIndex + "" : "",
                new CommonUuidReq(new CommonUuidReq.Snippet(pref.getString(Constants.UUID, "")))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassListResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull ClassListResp classListResp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>();
                        //Edited by Vikas Godara
                        int i = 0;
                        for (ClassListResp.Snippet classDetail : classListResp.getData()) {

                            ClassData classData = new ClassData();
                            classData.setImage(classDetail.getPicName());
                            classData.setClassTopic(classDetail.getClassTopic());
                            if (classDetail.getLocation() != null && !classDetail.getLocation().isEmpty())
                                classData.setLocality(classDetail.location.get(0).getLocality());
                            classData.setPricingType(classDetail.getPricingType());
                            if (classDetail.getClassLevels() != null)
                                classData.setPrice(classDetail.classLevels.get(0).getPrice());
                            classData.setNoOfSession(classDetail.getNoOfSession());
                            classData.setClassDuration(classDetail.getClassDuration());
                            classData.setClassType(classDetail.getClassType());
                            classData.setId(classDetail.getId());
                            classData.setClassTypeData(classDetail.getClassTypeData());
                            classData.setTeacher(classDetail.getTeacher());
                            //Edited by Vikas Godara
                            dataList.add(classData);
                            i++;

                        }
                        return dataList;
                    }
                });
    }

    public Observable<VendorProfileData> getVendorProfile(String vendorId) {
        return api.getVendorProfile("", new CommonIdReq(new CommonIdReq.Snippet(vendorId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<VendorProfileResp, VendorProfileData>() {
                    @Override
                    public VendorProfileData apply(@NonNull VendorProfileResp resp) throws Exception {
                        return gson.fromJson(gson.toJson(resp.getData().get(0)), VendorProfileData.class);
                    }
                });
    }

    public Observable<List<ClassData>> getVendorClassList(Integer pageIndex, String vendorId) {
        return api.getVendorProfile(pageIndex > 1 ? pageIndex + "" : "",
                new CommonIdReq(new CommonIdReq.Snippet(vendorId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<VendorProfileResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull VendorProfileResp resp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>();
                        //Edited by Vikas Godara
                        int i = 0;
                        if (resp.getData().get(i).getClasses() != null)
                            for (VendorProfileResp.Snippet.ClassDetail classDetail : resp.getData().get(i).getClasses()) {
                                ClassData classData = new ClassData();
                                classData.setImage(classDetail.photo);
                                classData.setClassTopic(classDetail.classTopic);
                                classData.setLocality(classDetail.location);
                                classData.setPricingType("Single");
                                classData.setPrice(classDetail.price);
                                classData.setNoOfSession(classDetail.noOfSession);
                                classData.setClassDuration(classDetail.classDuration);


                                if (classDetail.classTimingId.equals("1"))
                                    classData.setClassType("Fixed");
                                else
                                    classData.setClassType("Flexible");
                                classData.setId(classDetail.id);
                                //Edited by Vikas Godara
                                dataList.add(gson.fromJson(gson.toJson(classData), ClassData.class));
                                i++;

                            }
                        return dataList;
                    }
                });
    }

    public Observable<List<ClassData>> getBookingHistory(String userId, Integer pageIndex) {
        return api.getBookingHistory(pageIndex > 1 ? pageIndex + "" : "",
                new CommonIdReq(new CommonIdReq.Snippet(userId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<BookingHistoryResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull BookingHistoryResp resp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>();
                        if (!resp.getData().isEmpty())
                            for (BookingHistoryResp.Snippet snippet : resp.getData()) {
                                BookingHistoryResp.ClassDetail classDetail = snippet.getClassDetail();
                                ClassData classData = new ClassData();
                                classData.setImage(classDetail.getPhoto());
                                classData.setClassTopic(classDetail.getClassTopic());
                                if (classDetail.getLocation().size() != 0)
                                    classData.setLocality(classDetail.getLocation().get(0).getLocality());
                                classData.setPricingType(classDetail.getClassType());
                                if (classDetail.getClassLevels().size() != 0)
                                    classData.setPrice(classDetail.classLevels.get(0).getPrice());
                                classData.setNoOfSession(classDetail.getNoOfSession());
                                classData.setClassDuration(classDetail.getClassDuration());
                                classData.setClassType(classDetail.getClassType());
                                classData.setId(classDetail.getId());
                                classData.setClassTypeData(classDetail.getClassTypeData());
                                classData.setTeacher(classDetail.getTeacher());
                                //Edited by Vikas Godara
                                dataList.add(classData);
                            }
                        return dataList;
                    }
                });
    }

    public Observable<ClassData> getClassDetail(final String classId, final int isCatalogue) {

        return api.getClassDetail(new ClassDetailReq(new ClassDetailReq.Snippet(classId, pref.getString(Constants.BG_ID, ""), isCatalogue))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassListResp, ClassData>() {
                    @Override
                    public ClassData apply(@NonNull ClassListResp resp) throws Exception {
                        return gson.fromJson(gson.toJson(resp.getData().get(0)), ClassData.class);
                    }
                });

    }

    public Observable<BaseResp> contactAdmin(ContactAdmin req) {
        return api.contactAdmin(req).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> getQuote(QuoteReq.Snippet snippet) {
        return api.getQuote(new QuoteReq(snippet)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<ClassData>> getIndigeneousClass() {
        return api.getIndianClass().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassListResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull ClassListResp classListResp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>();
                        for (ClassListResp.Snippet snippet : classListResp.getData()) {
                            dataList.add(gson.fromJson(gson.toJson(snippet), ClassData.class));
                        }
                        return dataList;
                    }
                });
    }

    public Observable<List<ClassData>> getFeaturedClass() {
        return api.getFeaturedClass().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassListResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull ClassListResp classListResp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>();
                        for (ClassListResp.Snippet snippet : classListResp.getData()) {
                            try {
                                dataList.add(gson.fromJson(gson.toJson(snippet), ClassData.class));
                            } catch (Exception e) {
                                //  e.printStackTrace();
                                Log.d(TAG, "apply: " + e.toString());
                            }

                        }
                        return dataList;
                    }
                });
    }

    public Observable<List<ClassData>> getTrendingClass() {
        return api.getTrendingClass().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassListResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull ClassListResp classListResp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>();
                        for (ClassListResp.Snippet snippet : classListResp.getData()) {
                            dataList.add(gson.fromJson(gson.toJson(snippet), ClassData.class));
                        }
                        return dataList;
                    }
                });
    }

    @android.support.annotation.NonNull
    private MultipartBody.Part prepareFilePart(String partName, String fileName, String type) {
        File file = null;
        file = new File(fileName);
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(type),
                        file
                );
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    public Observable<ExploreResp> getExploreDashboard(String latitude, String longitude) {

        return api.getExploreDashboard(new ExploreReq(new ExploreReq.Snippet(null, null, null, latitude, longitude))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Observable<ExploreResp> exploreFilter(String categoryId, String location, String distance, String latitude, String longitude) {

        return api.exploreFilter(new ExploreReq(new ExploreReq.Snippet(categoryId, location, distance, latitude, longitude))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MarkerDataResp> getMarkerData(String latitude, String longitude) {
        return api.exploreMarkerData(new MarkerDataReq(new MarkerDataReq.Snippet(latitude, longitude))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<PayUCheckoutData> getPayUCheckoutData(PayUBookingDetailsReq req, final String promoId, final String promoVal) {

        return api.getBookingDetails(req)
                .flatMap(new Function<PayUBookingDetailsResp, ObservableSource<PayUCheckoutData>>() {
                    @Override
                    public ObservableSource<PayUCheckoutData> apply(@NonNull PayUBookingDetailsResp resp) throws Exception {

                        final PayUHashGenReq.Snippet snippet = new PayUHashGenReq.Snippet();
                        snippet.setPromoId(promoId);
                        snippet.setPromoValue(promoVal);
                        snippet.setAmount(resp.getData().get(0).getAmount());
                        snippet.setEmail(resp.getData().get(0).getEmail());
                        snippet.setFirstName(resp.getData().get(0).getFirstname());
                        snippet.setFUrl(resp.getData().get(0).getFurl());
                        snippet.setKey(resp.getData().get(0).getKey());
                        snippet.setPhone(resp.getData().get(0).getPhone());
                        snippet.setProductInfo(resp.getData().get(0).getProductinfo());
                        snippet.setServiceProvider(resp.getData().get(0).getServiceProvider());
                        snippet.setSUrl(resp.getData().get(0).getSurl());
                        snippet.setTxnId(resp.getData().get(0).getTxnid());
                        snippet.setUdf1(resp.getData().get(0).getUdf1());
                        snippet.setUdf2(resp.getData().get(0).getUdf2());
                        snippet.setUdf3(resp.getData().get(0).getUdf3());
                        snippet.setUdf4(resp.getData().get(0).getUdf4());

                        PayUHashGenReq req = new PayUHashGenReq(snippet);
                        return api.generatePayUHash(req).map(new Function<PayUHashResp, PayUCheckoutData>() {
                            @Override
                            public PayUCheckoutData apply(@NonNull PayUHashResp payUHashResp) throws Exception {
                                PayUCheckoutData payUdata = gson.fromJson(gson.toJson(snippet), PayUCheckoutData.class);
                                payUdata.setPaymentHash(payUHashResp.getData().get(0).getPaymentHash());
                                payUdata.setVasMobileSdkHash(payUHashResp.getData().get(0).getVasMobileSdkHash());
                                payUdata.setPaymentMobileSdkHash(payUHashResp.getData().get(0).getPaymentMobileSdkHash());
                                payUdata.setUserCardHash(payUHashResp.getData().get(0).getUserCardHash());
                                return payUdata;
                            }
                        });
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<RazorSuccessResp> postRazorpaySuccess(RazorSuccessReq req) {
        return api.postRazorPaySuccess(req).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<VendorReviewResp> getVendorReviews(String vendorId) {
        return api.getVendorProfile("", new CommonIdReq(new CommonIdReq.Snippet(vendorId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<VendorProfileResp, VendorReviewResp>() {
                    @Override
                    public VendorReviewResp apply(@NonNull VendorProfileResp resp) throws Exception {
                        VendorReviewResp dataList;
                        List<VendorReviewResp.Snippet> snippetList = new ArrayList<VendorReviewResp.Snippet>();
                        int i = 0;
                        for (VendorProfileResp.Snippet.Review review : resp.getData().get(i).getReview()) {
                            VendorReviewResp.Snippet snippet = (new VendorReviewResp.
                                    Snippet(null, null, review.text, review.by, null, null, null, null, null));
                            snippetList.add(gson.fromJson(gson.toJson(snippet), VendorReviewResp.Snippet.class));

                            i++;

                        }
                        dataList = new VendorReviewResp(snippetList);

                        return dataList;
                    }
                });
    }

    public Observable<CommonIdResp> getCountry() {
        return api.getCountry().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getState(String searchKey) {
        return api.getState(new StateReq(new StateReq.Snippet(searchKey))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getInstitute(String keyword) {
        return api.getInstitute(new InstituteReq(new InstituteReq.Snippet(keyword))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getLearner(String keyword) {
        return api.getUser(new UserListReq(new UserListReq.Snippet(keyword, 2, "1"))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> geTutor(String keyword) {
        return api.getUser(new UserListReq(new UserListReq.Snippet(keyword, 1, "1"))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getSchools(String keyword) {
        return api.getSchools(new InstituteReq(new InstituteReq.Snippet(keyword))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CompetitionStatusResp> getCompetitionStatus() {
        return api.getCompetitionStatus().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LikeResp> like(String postId) {
        return api.like(new LikeReq(new LikeReq.Snippet(pref.getString(Constants.BG_ID, ""), postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ReportResp> report(String postId) {
        return api.report(new ReportReq(new ReportReq.Snippet(pref.getString(Constants.BG_ID, ""), postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CategoryTreeResp> getCategoryTree() {
        return api.getCategories()
                .flatMap(new Function<CategoryResp, Observable<CategoryTreeResp>>() {
                             @Override
                             public Observable<CategoryTreeResp> apply(@NonNull CategoryResp resp) throws Exception {
                                 List<Observable<SegmentResp>> segmentObservableList = new ArrayList<>();
                                 for (CategoryResp.Snippet category : resp.getData()) {
                                     segmentObservableList.add(api.getSegments(new SegmentReq(new SegmentReq.Snippet(category.getId()))));
                                 }

                                 final Map<String, CategoryResp.Snippet> categoryMap = new HashMap<>();
                                 for (CategoryResp.Snippet snippet : resp.getData()) {
                                     categoryMap.put(snippet.getId(), snippet);
                                 }

                                 return Observable.zip(segmentObservableList, new Function<Object[], CategoryTreeResp>() {
                                     @Override
                                     public CategoryTreeResp apply(@NonNull Object[] segments) throws Exception {
                                         List<CategoryTreeResp.Snippet> rootSnippetList = new ArrayList<>();
                                         CategoryTreeResp.Snippet rootSnippet;
                                         for (Object segmentObj : segments) {
                                             SegmentResp segment = (SegmentResp) segmentObj;
                                             rootSnippet = new CategoryTreeResp.Snippet(categoryMap.get(segment.getCategoryId()), segment.getData());
                                             rootSnippetList.add(rootSnippet);
                                         }
                                         return new CategoryTreeResp(1, rootSnippetList);
                                     }

                                 });
                             }

                         }
                );
    }

    public Observable<SegmentResp> getSegmentTree(List<Integer> categoryIds) {
        List<Observable<SegmentResp>> segmentObservableList = new ArrayList<>();
        if (categoryIds.size() == 0)
            return Observable.empty();
        for (Integer categoryId : categoryIds) {
            segmentObservableList.add(api.getSegments(new SegmentReq(new SegmentReq.Snippet("" + categoryId))));
        }
        return Observable.zip(segmentObservableList, new Function<Object[], SegmentResp>() {
            @Override
            public SegmentResp apply(@NonNull Object[] segments) throws Exception {
                List<SegmentResp.Snippet> snippets = new ArrayList<SegmentResp.Snippet>();
                for (Object segmentObj : segments) {
                    SegmentResp segment = (SegmentResp) segmentObj;
                    snippets.addAll(segment.getData());
                }
                return new SegmentResp("1", snippets);
            }
        });

    }

    public Observable<ConnectSectionResp> getConnectSections() {
        return api.getConnectSections().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ConnectFeedResp> getConnectFeed(ConnectFilterData connectFilterData, int pageIndex) {
        ConnectFeedReq req = connectFilterData.getFilterReq();
        req.getData().setUserId(pref.getString(Constants.BG_ID, ""));
        return api.getConnectFeedData(pageIndex > 0 ? pageIndex + "" : "", req).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommentListResp> getComments(String postId) {
//        CommentListResp.Snippet snippet = new CommentListResp.Snippet();
//        CommentListResp.Reply reply = new CommentListResp.Reply();
//        snippet.setComment("The menu itself created in main FragmentActivity.
// I want to change this item's icon programmatically depending on the open Fragment and, obviously, have different actions when the user hits this button.
// I tried several things to do that, but nothing worked. The last thing I tried was this code in my Fragment's onCreateView method:");
//        snippet.setUserImage("https://lh5.googleusercontent.com/-n-KJMm8mENs/AAAAAAAAAAI/AAAAAAAAAHY/uG2vXBFifNU/photo.jpg?sz=50");
//        snippet.setUserName("Himanshu Agrahari");
//        snippet.setDate("12 Sept, 4:00 PM");
//        snippet.setNumLikes("200");
//
//        reply.setUserName("Neeraj Mishra");
//        reply.setReply("Programmatically depending on the open Fragment");
//        reply.setUserImage("http://static-collegedunia.com/public/college_data/images/campusimage/14365071078.jpg");
//        reply.setNumLikes("10");
//        reply.setReplyDate("12 Sept, 4:00 PM");
//        reply.setLiked(1);
//        List<CommentListResp.Reply> replyList = new ArrayList<>();
//        replyList.addAll(Collections.nCopies(5, reply));
//
//        snippet.setReplies(replyList);
//        List<CommentListResp.Snippet> result = new ArrayList<>();
//        result.addAll(Collections.nCopies(20, snippet));
//        return Observable.just(new CommentListResp(1, result));

        return api.getComments(new PostRelatedReq(new PostRelatedReq.Snippet(pref.getString(Constants.BG_ID, ""), postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommentViewReply> getReplies(String commentId) {
        return api.getReplies(new CommentViewReplyReq(new CommentViewReplyReq.Snippet(pref.getString(Constants.BG_ID, ""), commentId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<LikedUsersListResp> getLikedUsers(String postId) {
//        LikedUsersListResp.Snippet snippet = new LikedUsersListResp.Snippet();
//        snippet.setUserImage("https://lh5.googleusercontent.com/-n-KJMm8mENs/AAAAAAAAAAI/AAAAAAAAAHY/uG2vXBFifNU/photo.jpg?sz=50");
//        snippet.setUserName("Himanshu Agrahari");
//        List<LikedUsersListResp.Snippet> likesList = new ArrayList<>();
//        likesList.addAll(Collections.nCopies(5, snippet));
//        return Observable.just(new LikedUsersListResp(likesList));

        return api.getPostLikes(new PostRelatedReq(new PostRelatedReq.Snippet(pref.getString(Constants.BG_ID, ""), postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LikedUsersListResp> getAcceptedUsers(String postId) {
        return api.getAcceptedUsers(new PostRelatedReq(new PostRelatedReq.Snippet(null, postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<BaseResp> addAccept(String postId) {

        return api.addAccept(new PostRelatedReq(new PostRelatedReq.Snippet(pref.getString(Constants.BG_ID, ""), postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<BaseResp> postArticleVideos(ArticleAndVideosPostReq.Snippet snippet) {
        return api.postArticleVideos(new ArticleAndVideosPostReq(snippet)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> postDecideDiscuss(DecideAndDiscussPostReq.Snippet snippet) {
        return api.postDecideDiscuss(new DecideAndDiscussPostReq(snippet)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> postKnowledgeNuggets(KnowledgeNuggetsPostReq.Snippet snippet) {
        return api.postKnowledgeNuggets(new KnowledgeNuggetsPostReq(snippet)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> postBuyAndSell(BuyAndSellPostReq.Snippet snippet) {
        return api.postBuyAndSell(new BuyAndSellPostReq(snippet)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> postLearningPartner(LearningPartnerPostReq.Snippet snippet) {
        return api.postLearningPartner(new LearningPartnerPostReq(snippet)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommentReplyResp> commentReply(String postId, String replyId, String text) {
        return api.commentReply(new CommentReplyReq(new CommentReplyReq.Snippet(pref.getString(Constants.BG_ID, ""), postId, replyId, text))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<GuestUserResp> addGuestUser(String name, String email, String number) {
        return api.addGuestUser(new GuestUserReq(new GuestUserReq.Snippet(name, email, number))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NotificationListResp> getNotifications() {
        return api.getUserNotifications(new CommonUserIdReq(new CommonUserIdReq.Snippet(pref.getString(Constants.BG_ID, "")))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, NotificationListResp>() {
                    @Override
                    public NotificationListResp apply(@NonNull Throwable throwable) throws Exception {
                        List<NotificationListResp.Snippet> snippets = new ArrayList<>();
                        return new NotificationListResp(snippets);
                    }
                });
    }

    public Observable<BaseResp> changeNotificationStatus(String notificationId) {
        return api.changeNotificationStatus(new ChangeNotificationStatusReq(new ChangeNotificationStatusReq.Snippet(pref.getString(Constants.BG_ID, ""), notificationId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NotificationCountResp> getUnreadNotificationCount() {
        return api.getUnreadNotificationCount(new CommonUserIdReq(new CommonUserIdReq.Snippet(pref.getString(Constants.BG_ID, "")))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, NotificationCountResp>() {
                    @Override
                    public NotificationCountResp apply(@NonNull Throwable throwable) throws Exception {
                        return new NotificationCountResp();
                    }
                });
    }

    public Observable<BaseResp> changeMessageThreadStatus(String senderId) {
        return api.changeMessageThreadStatus(new ChatMessageReq(new ChatMessageReq.Snippet(pref.getString(Constants.BG_ID, ""), senderId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<NotificationCountResp> getUnreadMessageCount() {
        return api.getUnreadMessageCount(new CommonUserIdReq(new CommonUserIdReq.Snippet(pref.getString(Constants.BG_ID, "")))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, NotificationCountResp>() {
                    @Override
                    public NotificationCountResp apply(@NonNull Throwable throwable) throws Exception {
                        return new NotificationCountResp();
                    }
                });
    }

    public Observable<ConnectFeedResp> getFeedsByPostID(String postId) {
        return api.getFeedsByPostID(new ConnectPostByIdReq(new ConnectPostByIdReq.Snippet(pref.getString(Constants.BG_ID, ""), postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThirdPartyProfileResp> getThirdPartyProfile(String userId) {
        return api.getThirdPartyProfile(new CommonIdReq(new CommonIdReq.Snippet(userId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessageListResp> getMessages() {

//        List<MessageListResp.Snippet> dataList = new ArrayList<>();
//        MessageListResp.Snippet snippet = new MessageListResp.Snippet();
//        snippet.setSenderId("0");
//        snippet.setRecieverId("1");
//        MessageListResp.Message message = new MessageListResp.Message();
//        message.setAddDate("12 sept");
//        message.setMessage("test message himanshu test message test test himanshu test message");
//        message.setId("1");
//        message.setModifyDate("12 sept");
//        snippet.setMessage(message);
//        snippet.setSenderName("Himanshu Agrahari");
//        snippet.setSenderPic("https://www.braingroom.com/img/Buyer/profile/201705100651530709511001494399113.jpg");
//        dataList.addAll(Collections.nCopies(20, snippet));
//
//        MessageListResp resp = new MessageListResp(dataList);
//        return Observable.just(resp);
        return api.getMessages(new MessageListReq(new MessageListReq.Snippet(pref.getString(Constants.BG_ID, "")))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ChatListResp> getChatMessages(String senderId) {

//        List<ChatListResp.Snippet> dataList = new ArrayList<>();
//        ChatListResp.Snippet chat1 = new ChatListResp.Snippet();
//        chat1.setText("From sender msg 1");
//        chat1.setUserId("0");
//        chat1.setTime("1495524123");
//
//        ChatListResp.Snippet chat2 = new ChatListResp.Snippet();
//        chat2.setText("From sender msg 2");
//        chat2.setUserId("0");
//        chat2.setTime("1495524123");
//
//        ChatListResp.Snippet chat3 = new ChatListResp.Snippet();
//        chat3.setText("From sender msg 3");
//        chat3.setUserId("0");
//        chat3.setTime("1495524123");
//
//        ChatListResp.Snippet chat4 = new ChatListResp.Snippet();
//        chat4.setText("From me msg 1");
//        chat4.setUserId("1");
//        chat4.setTime("1495524123");
//
//        dataList.add(chat1);
//        dataList.add(chat2);
//        dataList.add(chat3);
//        dataList.add(chat4);
//
//        ChatListResp resp = new ChatListResp(dataList);
//        return Observable.just(resp);
        return api.getChatMessages(new ChatMessageReq(new ChatMessageReq.Snippet(senderId, pref.getString(Constants.BG_ID, "")))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> postMessage(MessageReplyReq.Snippet snippet) {
        return api.postMessage(new MessageReplyReq(snippet)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<UploadPostApiResp> uploadPostApiImage(String filePath, String type, String post_type) {
        return api.postApiUpload("uploadPostImage"
                , prepareFilePart("image", filePath, type)
                , RequestBody.create(MediaType.parse("text/plain"), post_type)
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<UploadPostApiResp> uploadPostApiVideo(String filePath, String type, String post_type) {
        return api.postApiUpload("uploadPostVideo"
                , prepareFilePart("video", filePath, type)
                , RequestBody.create(MediaType.parse("text/plain"), post_type)
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CatalogueGroupResp> getCatalogueGroups() {
        return api.getCatalogueGroup().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<GiftcardResp.DataSnippet>> getIndividualGiftcards() {
        return api.getGiftcards().map(new Function<GiftcardResp, List<GiftcardResp.DataSnippet>>() {
            @Override
            public List<GiftcardResp.DataSnippet> apply(@NonNull GiftcardResp resp) throws Exception {
                return resp.getData().get(0).getIndividual();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> getGiftCoupon(GiftCouponReq req) {
        return api.saveGiftCoupon(req).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<GiftcardResp.DataSnippet>> getCorporateGiftcards() {
        return api.getGiftcards().map(new Function<GiftcardResp, List<GiftcardResp.DataSnippet>>() {
            @Override
            public List<GiftcardResp.DataSnippet> apply(@NonNull GiftcardResp resp) throws Exception {
                return resp.getData().get(0).getCorporate();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<GiftcardResp.DataSnippet>> getNgoGiftcards() {
        return api.getGiftcards().map(new Function<GiftcardResp, List<GiftcardResp.DataSnippet>>() {
            @Override
            public List<GiftcardResp.DataSnippet> apply(@NonNull GiftcardResp resp) throws Exception {
                return resp.getData().get(0).getNgo();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getNgoList(String giftcardId) {
        return api.getNgoList(new CommonIdReq(new CommonIdReq.Snippet(giftcardId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getNgoCategories(String giftcardId) {
        return api.getNgoSegments(new CommonIdReq(new CommonIdReq.Snippet(giftcardId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SaveGiftCouponResp> saveGiftCoupon(SaveGiftCouponReq req) {
        return api.saveGiftCoupon(req).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SaveGiftCouponResp> saveGiftClass(SaveGiftCouponReq req) {
        return api.saveGiftCoupon(req).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> updateCouponPaymentSuccess(RazorBuySuccessReq req) {
        return api.updateCouponPaymentSuccess(req).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<FollowResp> follow(String userId) {
        return api.follow(new FollowReq(new FollowReq.Snippet(userId, pref.getString(Constants.BG_ID, ""))))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}

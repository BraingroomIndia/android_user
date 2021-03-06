package com.braingroom.user.model;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.braingroom.user.UserApplication;
import com.braingroom.user.model.dto.ClassData;
import com.braingroom.user.model.dto.ClassListData;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.dto.PayUCheckoutData;
import com.braingroom.user.model.dto.ProfileData;
import com.braingroom.user.model.dto.VendorProfileData;
import com.braingroom.user.model.request.ArticleAndVideosPostReq;
import com.braingroom.user.model.request.BookedSessionReq;
import com.braingroom.user.model.request.BuyAndSellPostReq;
import com.braingroom.user.model.request.CdnUrlReq;
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
import com.braingroom.user.model.request.ContactTutorReq;
import com.braingroom.user.model.request.CountryReq;
import com.braingroom.user.model.request.CouponCodeReq;
import com.braingroom.user.model.request.DecideAndDiscussPostReq;
import com.braingroom.user.model.request.DeepLinkDataReq;
import com.braingroom.user.model.request.ExploreReq;
import com.braingroom.user.model.request.FirstSocialLoginReq;
import com.braingroom.user.model.request.FollowReq;
import com.braingroom.user.model.request.GeneralFilterReq;
import com.braingroom.user.model.request.GetBookingDetailsReq;
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
import com.braingroom.user.model.request.OTPReq;
import com.braingroom.user.model.request.OnlineBookinHistoryReq;
import com.braingroom.user.model.request.PostRelatedReq;
import com.braingroom.user.model.request.ProfileUpdateReq;
import com.braingroom.user.model.request.PromoCodeReq;
import com.braingroom.user.model.request.QuoteDetailsReq;
import com.braingroom.user.model.request.QuoteReq;
import com.braingroom.user.model.request.RazorBuySuccessReq;
import com.braingroom.user.model.request.RazorSuccessReq;
import com.braingroom.user.model.request.ReferralCodeReq;
import com.braingroom.user.model.request.RegisterUserDeviceReq;
import com.braingroom.user.model.request.ReportReq;
import com.braingroom.user.model.request.ReviewAddReq;
import com.braingroom.user.model.request.ReviewGetReq;
import com.braingroom.user.model.request.SaveGiftCouponReq;
import com.braingroom.user.model.request.SearchReq;
import com.braingroom.user.model.request.SegmentReq;
import com.braingroom.user.model.request.SignUpReq;
import com.braingroom.user.model.request.SocialLoginReq;
import com.braingroom.user.model.request.StateReq;
import com.braingroom.user.model.request.SubmitOTPReq;
import com.braingroom.user.model.request.ThirdPartyProfileReq;
import com.braingroom.user.model.request.UserGeoLocationReq;
import com.braingroom.user.model.request.UserListReq;
import com.braingroom.user.model.request.WishlistReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.BookedSessionResp;
import com.braingroom.user.model.response.BookingHistoryResp;
import com.braingroom.user.model.response.CODOfferDetailResp;
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
import com.braingroom.user.model.response.ContactTutorResp;
import com.braingroom.user.model.response.DeepLinkDataResp;
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
import com.braingroom.user.model.response.PrimeMessageResp;
import com.braingroom.user.model.response.ProfileResp;
import com.braingroom.user.model.response.PromoInfo;
import com.braingroom.user.model.response.PromocodeResp;
import com.braingroom.user.model.response.QuoteDetailsResp;
import com.braingroom.user.model.response.RazorSuccessResp;
import com.braingroom.user.model.response.ReferralCodeResp;
import com.braingroom.user.model.response.ReportResp;
import com.braingroom.user.model.response.ReviewAddResp;
import com.braingroom.user.model.response.ReviewGetResp;
import com.braingroom.user.model.response.SaveGiftCouponResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.model.response.ThirdPartyProfileResp;
import com.braingroom.user.model.response.UploadPostApiResp;
import com.braingroom.user.model.response.UploadResp;
import com.braingroom.user.model.response.UserGeoLocationResp;
import com.braingroom.user.model.response.VendorProfileResp;
import com.braingroom.user.model.response.VendorReviewResp;
import com.braingroom.user.model.response.WinnerResp;
import com.braingroom.user.model.response.WishlistResp;
import com.braingroom.user.utils.Constants;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import timber.log.Timber;

import static android.content.ContentValues.TAG;
import static com.braingroom.user.model.DataflowService.FilterType.Category;
import static com.braingroom.user.model.DataflowService.FilterType.City;
import static com.braingroom.user.model.DataflowService.FilterType.Community;
import static com.braingroom.user.model.DataflowService.FilterType.Locality;
import static com.braingroom.user.model.DataflowService.FilterType.Segment;


public class DataflowService {

    @Inject
    @Named("defaultPref")
    public SharedPreferences pref;
    @Inject
    @Named("api")
    UserApiService api;
    @Inject
    Gson gson;
    SharedPreferences sharedPreferences;


    @Inject
    public DataflowService() {

    }

    public void registerUserDevice() {
        final RegisterUserDeviceReq req = new RegisterUserDeviceReq(new RegisterUserDeviceReq.Snippet(pref.getString(Constants.FCM_TOKEN, ""), pref.getString(Constants.BG_ID, null)));
        api.registerUserDevice(req)
                .onErrorReturn(new Function<Throwable, BaseResp>() {
                    @Override
                    public BaseResp apply(@NonNull Throwable throwable) throws Exception {
                        Timber.tag(TAG).e(throwable, "request payload\n" + gson.toJson(req));
                        return new BaseResp();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public void registerUserDevice(String fcm) {
        final RegisterUserDeviceReq req = new RegisterUserDeviceReq(new RegisterUserDeviceReq.Snippet(fcm, pref.getString(Constants.BG_ID, null)));
        api.registerUserDevice(req)
                .onErrorReturn(new Function<Throwable, BaseResp>() {
                    @Override
                    public BaseResp apply(@NonNull Throwable throwable) throws Exception {
                        Timber.tag(TAG).e(throwable, "request payload\n" + gson.toJson(req));
                        return new BaseResp();
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
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
        final SocialLoginReq req = new SocialLoginReq(snippet);
        return api.socialLogin(req).subscribeOn(Schedulers.io()).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Timber.tag(TAG).e(throwable, "request payload\n" + gson.toJson(req));
            }
        }).observeOn(AndroidSchedulers.mainThread());

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

    public Observable<SignUpResp> signUp(final SignUpReq signUpReq) {

        return api.BuyerRegistration(signUpReq).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, SignUpResp>() {
                    @Override
                    public SignUpResp apply(@NonNull Throwable throwable) throws Exception {
                        Timber.tag(TAG).e(throwable, "request payload\n" + gson.toJson(signUpReq));
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
        return api.logout(new LogoutReq(snippet)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, BaseResp>() {
            @Override
            public BaseResp apply(@NonNull Throwable throwable) throws Exception {
                return new BaseResp();
            }
        });
    }

    public Observable<CommunityResp> getCommunity() {

        return api.getCommunity().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getAllClassTypes() {

        return api.getAllClassTypes().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getAllDurations() {

        return api.getAllDurations().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CategoryResp> getCategory() {

        return api.getCategories().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<GroupResp> getGroups(String categoryId) {

        return api.getGroups(new ConnectDataReq(new ConnectDataReq.Snippet(categoryId))).subscribeOn(Schedulers.io())
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

    public Observable<CommonIdResp> getMajorCity(String countryId) {
        return api.getCityList(new CityReq(new CityReq.Snippet(true, countryId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<CommonIdResp> getCityList() {

        return api.getCityList(new CityReq(new CityReq.Snippet(true))).subscribeOn(Schedulers.io())
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

    public Observable<PromocodeResp> applyPromoCode(PromoCodeReq.Snippet snippet) {

        return api.applyPromoCode(new PromoCodeReq((snippet))).subscribeOn(Schedulers.io())
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
                            classData.setRating(classDetail.getRating());
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
                            classData.setPriceSymbol(classDetail.getPriceSymbol());
                            classData.setPriceCode(classDetail.getPriceCode());
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

    public Observable<List<ClassData>> getVendorClassList(Integer pageIndex, Integer vendorId) {


        FilterData filterData = new FilterData();
        filterData.setVendorId("xyz", vendorId);

        return api.generalFilter(pageIndex > 0 ? pageIndex + "" : "", filterData.getFilterReq()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassListResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull ClassListResp classListResp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>(0);

                        for (ClassListResp.Snippet snippet : classListResp.getData()) {
                            dataList.add(gson.fromJson(gson.toJson(snippet), ClassData.class));
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
                                classData.setRating(classDetail.getRating());
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
                                classData.setPriceSymbol(classDetail.getPriceSymbol());
                                classData.setPriceCode(classDetail.getPriceCode());
                                //Edited by Vikas Godara
                                dataList.add(classData);
                            }
                        return dataList;
                    }
                });
    }

    public Observable<List<ClassData>> getBookingOnlineHistory(String userId, Integer pageIndex) {

        return api.getOnlineBookingHistory(pageIndex > 1 ? pageIndex + "" : "",
                new OnlineBookinHistoryReq(new OnlineBookinHistoryReq.Snippet(userId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<BookingHistoryResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull BookingHistoryResp resp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>();
                        if (!resp.getData().isEmpty())
                            for (BookingHistoryResp.Snippet snippet : resp.getData()) {
                                BookingHistoryResp.ClassDetail classDetail = snippet.getClassDetail();
                                ClassData classData = new ClassData();
                                classData.setRating(classDetail.getRating());
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
                                classData.setPriceSymbol(classDetail.getPriceSymbol());
                                classData.setPriceCode(classDetail.getPriceCode());
                                classData.setTxnId(snippet.getPayuTransaction().getTxnid());
                                //Edited by Vikas Godara
                                dataList.add(classData);
                            }
                        return dataList;
                    }
                });
    }

    public Observable<List<BookedSessionResp.Session>> getOnlineVideoList(String classId) {
        return api.getOnlineVideoList(new BookedSessionReq(new BookedSessionReq.Snippet(classId, pref.getString(Constants.BG_ID, "")))).map(BookedSessionResp::getSession).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<String> getCDNVideoUrl(String txnId, String sessionId) {
        return api.getVideoUrl(new CdnUrlReq(new CdnUrlReq.Snippet(txnId, pref.getString(Constants.BG_ID, ""), sessionId))).map(resp -> resp.getData().get(0).getTextValue()).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                InputStream M3U8 = null;

                try {
                    M3U8 = (InputStream) url.getContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(M3U8));
                for (int i = 0; i < 2; ++i)
                    try {
                        br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                String target = null; //this parses the third line of the playlist
                try {
                    target = br.readLine();
                    br.close();
                    url = new URL(target);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return url.toString();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturn((throwable) -> "");
    }

    public Observable<ClassData> getClassDetail(final String classId, final int isCatalogue) {

        return api.getClassDetail(new ClassDetailReq(new ClassDetailReq.Snippet(classId, pref.getString(Constants.BG_ID, ""), isCatalogue))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(resp -> gson.fromJson(gson.toJson(resp.getData().get(0)), ClassData.class));

    }

    public Observable<BaseResp> contactAdmin(ContactAdmin req) {

        return api.contactAdmin(req).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResp> getQuote(QuoteReq.Snippet snippet) {

        return api.getQuote(new QuoteReq(snippet)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<ClassData>> getIndigeneousClass() {

        return api.getIndianClass().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(classListResp -> {
                    List<ClassData> dataList = new ArrayList<>();
                    for (ClassListResp.Snippet snippet : classListResp.getData()) {
                        dataList.add(gson.fromJson(gson.toJson(snippet), ClassData.class));
                    }
                    return dataList;
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
                                Timber.tag(TAG).d("apply: " + e);
                            }

                        }
                        return dataList;
                    }
                });
    }

    public Observable<List<ClassData>> getRecommendedClass() {

        return api.getRecommendedClass().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<ClassListResp, List<ClassData>>() {
                    @Override
                    public List<ClassData> apply(@NonNull ClassListResp classListResp) throws Exception {
                        List<ClassData> dataList = new ArrayList<>();
                        for (ClassListResp.Snippet snippet : classListResp.getData()) {
                            try {
                                dataList.add(gson.fromJson(gson.toJson(snippet), ClassData.class));
                            } catch (Exception e) {
                                //  e.printStackTrace();
                                Timber.tag(TAG).d("apply: " + e.toString());
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
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturn(throwable -> new ExploreResp(new ArrayList<>()));

    }

    public Observable<ExploreResp> exploreFilter(String categoryId, String location, String distance, String latitude, String longitude) {

        return api.exploreFilter(new ExploreReq(new ExploreReq.Snippet(categoryId, location, distance, latitude, longitude))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MarkerDataResp> getMarkerData(String latitude, String longitude) {

        return api.exploreMarkerData(new MarkerDataReq(new MarkerDataReq.Snippet(latitude, longitude))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<PayUCheckoutData> getBookingDetails(final GetBookingDetailsReq req) {

        return api.getBookingDetails(req).map(new Function<PayUBookingDetailsResp, PayUCheckoutData>() {
            @Override
            public PayUCheckoutData apply(@NonNull PayUBookingDetailsResp resp) throws Exception {
                PayUBookingDetailsResp.Snippet data = resp.getData().get(0);
                PayUCheckoutData checkoutData = new PayUCheckoutData();
                checkoutData.setBGtransactionid(data.getTxnid());
                checkoutData.setClassId(data.getClassId());
                checkoutData.setAmount(data.getAmount());
                checkoutData.setFirstname(data.getFirstname());
                checkoutData.setEmail(data.getEmail());
                checkoutData.setPhone(data.getPhone());
                checkoutData.setUdf1(data.getUdf1());
                checkoutData.setUdf2(data.getUdf2());
                checkoutData.setUdf3(data.getUdf3());
                checkoutData.setUdf4(data.getUdf4());
                return checkoutData;
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Timber.tag(TAG).e(throwable, "request payload\n" + gson.toJson(req));
            }
        })
                /*.flatMap(new Function<PayUBookingDetailsResp, ObservableSource<PayUCheckoutData>>() {
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

                        PayUHashGenReq braingroom = new PayUHashGenReq(snippet);
                        return api.generatePayUHash(braingroom).map(new Function<PayUHashResp, PayUCheckoutData>() {
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
                })*/.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<RazorSuccessResp> postRazorpaySuccess(final RazorSuccessReq req) {
        Timber.tag(TAG).e("request payload\n" + gson.toJson(req));
        return api.postRazorPaySuccess(req).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Timber.tag(TAG).e(throwable, "request payload\n" + gson.toJson(req));
                    }
                });
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

    public Observable<CommonIdResp> getMajorCountry() {

        return api.getMajorCountry(new CountryReq()).subscribeOn(Schedulers.io())
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
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, CompetitionStatusResp>() {
                    @Override
                    public CompetitionStatusResp apply(@NonNull Throwable throwable) throws Exception {
                        return new CompetitionStatusResp(new ArrayList<CompetitionStatusResp.Snippet>());
                    }
                });
    }

    public Observable<LikeResp> like(String postId) {

        return api.like(new LikeReq(new LikeReq.Snippet(pref.getString(Constants.BG_ID, ""), postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ReportResp> report(String postId) {

        return api.report(new ReportReq(new ReportReq.Snippet(pref.getString(Constants.BG_ID, ""), postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ReviewAddResp> addReview(int reviewType, String vendorIdorClassId, String review, String rating) {
        return api.reviewAdd(new ReviewAddReq(new ReviewAddReq.Snippet(pref.getString(Constants.BG_ID, ""), reviewType, vendorIdorClassId, review, rating))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturnItem(new ReviewAddResp());
    }

    public Observable<ReviewAddResp> addReview(int reviewType, String vendorIdorClassId, String review, String rating, String userId) {
        if (!isEmpty(userId))
            return api.reviewAdd(new ReviewAddReq(new ReviewAddReq.Snippet(userId, reviewType, vendorIdorClassId, review, rating))).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).onErrorReturnItem(new ReviewAddResp());
        else return addReview(reviewType, vendorIdorClassId, review, rating);
    }

    public Observable<ReviewGetResp> getReview(int reviewType, String id, int pageNumber) {
        return api.reviewGet((pageNumber > 1 ? pageNumber : "") + "", new ReviewGetReq(new ReviewGetReq.Snippet(pref.getString(Constants.BG_ID, ""), reviewType, id))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).onErrorReturnItem(new ReviewGetResp());

    }

    public Observable<CategoryTreeResp> getCategoryTree() {

        return api.getCategories()
                .flatMap(new Function<CategoryResp, Observable<CategoryTreeResp>>() {
                             @Override
                             public Observable<CategoryTreeResp> apply(@NonNull CategoryResp resp) throws Exception {
                                 List<Observable<SegmentResp>> segmentObservableList = new ArrayList<>();
                                 for (CategoryResp.Snippet category : resp.getData()) {
                                     segmentObservableList.add(api.getSegments(new SegmentReq(new SegmentReq.Snippet(category.getId() + ""))));
                                 }

                                 final Map<String, CategoryResp.Snippet> categoryMap = new HashMap<>();
                                 for (CategoryResp.Snippet snippet : resp.getData()) {
                                     categoryMap.put(snippet.getId() + "", snippet);
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

    public Observable<NameIdPair> getCategoryName(final Integer id) {
        return getCategory().map(new Function<CategoryResp, NameIdPair>() {
            @Override
            public NameIdPair apply(CategoryResp categoryResp) throws Exception {
                for (CategoryResp.Snippet snippet : categoryResp.getData())
                    if (snippet.getId().equals(id))
                        return new NameIdPair(Category, snippet.getCategoryName(), snippet.getId());

                return new NameIdPair(Category, null, null);
            }
        });
    }

    public Observable<NameIdPair> getCategoryName(final String id) {
        return getCategory().map(new Function<CategoryResp, NameIdPair>() {
            @Override
            public NameIdPair apply(CategoryResp categoryResp) throws Exception {
                for (CategoryResp.Snippet snippet : categoryResp.getData())
                    if (snippet.getId().equals(Integer.parseInt(id)))
                        return new NameIdPair(Category, snippet.getCategoryName(), snippet.getId());

                return new NameIdPair(Category, null, null);
            }
        });
    }

    private Observable<NameIdPair> getSegmentName(Integer categoryId, final Integer segmentId) {

        return getSegments(categoryId + "").map(new Function<SegmentResp, NameIdPair>() {
            @Override
            public NameIdPair apply(SegmentResp segmentResp) throws Exception {
                for (SegmentResp.Snippet snippet : segmentResp.getData())
                    if (snippet.getId().equals(segmentId))
                        return new NameIdPair(Segment, snippet.getSegmentName(), snippet.getId());
                return new NameIdPair(Segment, null, null);
            }
        });

    }

    private Observable<NameIdPair> getCityName(final Integer cityId) {
        return getCityList().map(new Function<CommonIdResp, NameIdPair>() {
            @Override
            public NameIdPair apply(CommonIdResp commonIdResp) throws Exception {
                for (CommonIdResp.Snippet snippet : commonIdResp.getData())
                    if (snippet.getId().equals(cityId))
                        return new NameIdPair(City, snippet.getTextValue(), snippet.getId());
                return new NameIdPair(City, null, null);
            }
        });
    }

    private Observable<NameIdPair> getLocalityName(Integer cityId, final Integer localityId) {
        return getLocalityList(cityId + "").map(new Function<CommonIdResp, NameIdPair>() {
            @Override
            public NameIdPair apply(CommonIdResp commonIdResp) throws Exception {
                for (CommonIdResp.Snippet snippet : commonIdResp.getData())
                    if (snippet.getId().equals(localityId))
                        return new NameIdPair(Locality, snippet.getTextValue(), snippet.getId());
                return new NameIdPair(Locality, null, null);
            }
        });
    }

    private Observable<NameIdPair> getCommunityName(final Integer id) {
        return getCommunity().map(new Function<CommunityResp, NameIdPair>() {
            @Override
            public NameIdPair apply(CommunityResp communityResp) throws Exception {
                for (CommunityResp.Snippet snippet : communityResp.getData())
                    if ((Integer.parseInt(snippet.getId())) == id)
                        return new NameIdPair(Community, snippet.getName(), Integer.parseInt(snippet.getId()));
                return new NameIdPair(Community, null, null);
            }
        });
    }

    private Observable<NameIdPair> getClassType(final Integer id) {
        return Observable.just(id).map(new Function<Integer, NameIdPair>() {
            @Override
            public NameIdPair apply(Integer integer) throws Exception {
                for (com.braingroom.user.utils.ClassType classType : com.braingroom.user.utils.ClassType.values())
                    if (classType.id == integer)
                        return new NameIdPair(FilterType.ClassType, classType.name, classType.id);
                return new NameIdPair(FilterType.ClassType, null, null);

            }
        });
    }

    private Observable<NameIdPair> getClassSchedule(final Integer id) {
        return Observable.just(id).map(new Function<Integer, NameIdPair>() {
            @Override
            public NameIdPair apply(Integer integer) throws Exception {
                for (com.braingroom.user.utils.ClassSchedule classSchedule : com.braingroom.user.utils.ClassSchedule.values())
                    if (classSchedule.id == integer)
                        return new NameIdPair(FilterType.ClassSchedule, classSchedule.name, classSchedule.id);
                return new NameIdPair(FilterType.ClassType, null, null);

            }
        });
    }

    private Observable<NameIdPair> getVendorName(final Integer id) {
        return getVendors().map(new Function<CommonIdResp, NameIdPair>() {
            @Override
            public NameIdPair apply(CommonIdResp commonIdResp) throws Exception {
                for (CommonIdResp.Snippet snippet : commonIdResp.getData())
                    if (snippet.getId().equals(id))
                        return new NameIdPair(FilterType.VendorList, snippet.getTextValue(), snippet.getId());
                return new NameIdPair(FilterType.VendorList, null, null);
            }
        });
    }

    public Observable<FilterData> getFilterData(final GeneralFilterReq.Snippet data) {

        final FilterData filterData = new FilterData();
        filterData.setStartDate(data.getStartDate());
        filterData.setEndDate(data.getEndDate());
        filterData.setKeywords(data.getKeywords());
        List<Observable<NameIdPair>> filterReqName = new ArrayList<>();
        final GeneralFilterReq payLoad = new GeneralFilterReq(data);
//        1
        try {
            if (!isEmpty(data.getCategoryId()))
                filterReqName.add(getCategoryName(Integer.parseInt(data.getCategoryId())));
//        2
            try {
                if (!isEmpty(data.getSegmentId()))
                    filterReqName.add(getSegmentName(Integer.parseInt(data.getCategoryId()), Integer.parseInt(data.getSegmentId())));
            } catch (Exception e) {
                Timber.tag(TAG).e(e, "Qr code issue\nrequest Payload\n" + gson.toJson(payLoad));
            }

        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Qr code issue\nrequest Payload\n" + gson.toJson(payLoad));
        }
//        3 city
        try {
            if (!isEmpty(data.getCityId()))
                filterReqName.add(getCityName(Integer.parseInt(data.getCityId())));
//        4 locality
            try {
                if (!isEmpty(data.getLocalityId()))
                    filterReqName.add(getLocalityName(Integer.parseInt(data.getCityId()), Integer.parseInt(data.getLocalityId())));
            } catch (Exception e) {
                Timber.tag(TAG).e(e, "Qr code issue\nrequest Payload\n" + gson.toJson(payLoad));
            }

        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Qr code issue\nrequest Payload\n" + gson.toJson(payLoad));
        }

//        5 community
        try {
            if (!isEmpty(data.getCommunityId()))
                filterReqName.add(getCommunityName(Integer.parseInt(data.getCommunityId())));
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Qr code issue\nrequest Payload\n" + gson.toJson(payLoad));
        }

//        6 class Type
        try {
            if (!isEmpty(data.getClassType()))
                filterReqName.add(getClassType(Integer.parseInt(data.getClassType())));
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Qr code issue\nrequest Payload\n" + gson.toJson(payLoad));
        }

//        7  class schedule
        try {
            if (!isEmpty(data.getClassSchedule()))
                filterReqName.add(getClassSchedule(Integer.parseInt(data.getClassSchedule())));
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Qr code issue\nrequest Payload\n" + gson.toJson(payLoad));
        }

//        8  vendor
        try {
            if (!isEmpty(data.getClassProvider()))
                filterReqName.add(getVendorName(Integer.parseInt(data.getClassProvider())));
        } catch (Exception e) {
            Timber.tag(TAG).e(e, "Qr code issue\nrequest Payload\n" + gson.toJson(payLoad));
        }

        if (filterReqName.isEmpty())
            return Observable.just(filterData);
        else
            return Observable.zip(filterReqName, new Function<Object[], FilterData>() {
                @Override
                public FilterData apply(Object[] objects) throws Exception {

                    for (Object nameIdPair : objects)
                        if (nameIdPair instanceof NameIdPair) {
                            NameIdPair object = (NameIdPair) nameIdPair;
                            switch (object.type) {
                                case Category:
                                    filterData.setCategoryId(object.name, object.id);
                                    break;
                                case Segment:
                                    filterData.setSegmentId(object.name, object.id);
                                    break;
                                case City:
                                    filterData.setCityId(object.name, object.id);
                                    break;
                                case Locality:
                                    filterData.setLocalityId(object.name, object.id);
                                    break;
                                case Community:
                                    filterData.setCommunityId(object.name, object.id);
                                    break;
                                case ClassType:
                                    filterData.setClassTypeId(object.name, object.id);
                                    break;
                                case ClassSchedule:
                                    filterData.setClassScheduleId(object.name, object.id);
                                    break;
                                case VendorList:
                                    filterData.setVendorId(object.name, object.id);
                                    break;
                            }
                        }
                    return filterData;
                }
            }).doOnError(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    Timber.tag(TAG).e(throwable, "Qr code issue\nrequest Payload\n" + gson.toJson(payLoad));
                }
            }).onErrorReturn(new Function<Throwable, FilterData>() {
                @Override
                public FilterData apply(Throwable throwable) throws Exception {
                    return filterData;
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
                        return new NotificationCountResp(new ArrayList<NotificationCountResp.Snippet>());
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
                        return new NotificationCountResp(new ArrayList<NotificationCountResp.Snippet>());
                    }
                });


    }

    public Observable<ConnectFeedResp> getFeedsByPostID(String postId) {

        return api.getFeedsByPostID(new ConnectPostByIdReq(new ConnectPostByIdReq.Snippet(pref.getString(Constants.BG_ID, ""), postId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ThirdPartyProfileResp> getThirdPartyProfile(String userId) {

        return api.getThirdPartyProfile(new ThirdPartyProfileReq(new ThirdPartyProfileReq.Snippet(pref.getString(Constants.BG_ID, ""), userId))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<MessageListResp> getMessages() {

        return api.getMessages(new MessageListReq(new MessageListReq.Snippet(pref.getString(Constants.BG_ID, "")))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ChatListResp> getChatMessages(String senderId) {

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

    public Observable<LikedUsersListResp> getFollowers() {

        return api.getFollowers(new CommonUserIdReq(new CommonUserIdReq.Snippet(pref.getString(Constants.BG_ID, "")))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LikedUsersListResp> getFollowers(String userId) {

        return api.getFollowers(new CommonUserIdReq(new CommonUserIdReq.Snippet(userId))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LikedUsersListResp> getFollowing() {

        return api.getFollowingUsers(new CommonUserIdReq(new CommonUserIdReq.Snippet(pref.getString(Constants.BG_ID, "")))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<LikedUsersListResp> getFollowing(String userId) {

        return api.getFollowingUsers(new CommonUserIdReq(new CommonUserIdReq.Snippet(userId))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ContactTutorResp> contactTutor(String classID) {

        return api.contactTutor(new ContactTutorReq(new ContactTutorReq.Snippet(pref.getString(Constants.BG_ID, ""), classID, ""))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<QuoteDetailsResp> getQuoteDetails(String quoteId) {

        return api.getQuoteDetails(new QuoteDetailsReq(new QuoteDetailsReq.Snippet(quoteId))).
                subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<WinnerResp> getWeeklyWinners() {

        return api.getWeeklyPerformers().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, WinnerResp>() {
            @Override
            public WinnerResp apply(@NonNull Throwable throwable) throws Exception {
                return new WinnerResp(new ArrayList<WinnerResp.Snippet>());
            }
        });
    }

    public Observable<PrimeMessageResp> getPrimeTimeMessage() {

        return api.getPrimeMessage().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, PrimeMessageResp>() {
            @Override
            public PrimeMessageResp apply(@NonNull Throwable throwable) throws Exception {
                return new PrimeMessageResp(new ArrayList<PrimeMessageResp.Snippet>());
            }
        });
    }

    public Observable<Boolean> forceUpdate() {

        return api.getLearnerAppMinVersion().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, CommonIdResp>() {
            @Override
            public CommonIdResp apply(@NonNull Throwable throwable) throws Exception {
                return new CommonIdResp(new ArrayList<CommonIdResp.Snippet>());
            }
        }).map(new Function<CommonIdResp, Boolean>() {

            @Override
            public Boolean apply(@NonNull CommonIdResp resp) throws Exception {
                if (resp == null)
                    return false;
                else if (resp.getData() == null)
                    return false;
                else if (resp.getData().isEmpty())
                    return false;
                else if (resp.getData().get(0) == null)
                    return false;
                else if (resp.getData().get(0).getTextValue() == null)
                    return false;
                try {
                    return Float.parseFloat(resp.getData().get(0).getTextValue()) > UserApplication.versionCode;
                } catch (Exception e) {
                    return false;
                }

            }
        });
    }

    public Observable<PromoInfo> getPromoInfo(String promoCode) {

        return api.getPromoInfo(new PromoCodeReq(new PromoCodeReq.Snippet(promoCode))).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).onErrorReturn(new Function<Throwable, PromoInfo>() {
            @Override
            public PromoInfo apply(@NonNull Throwable throwable) throws Exception {
                return new PromoInfo();
            }
        });
    }

    private Observable<String> getGeoDetail(int cityId) {
        if (cityId == -1)
            return api.getGeoDetail().map(new Function<CommonIdResp, String>() {
                @Override
                public String apply(@NonNull CommonIdResp resp) throws Exception {
                    return resp == null || resp.getData() == null || resp.getData().isEmpty() || resp.getData().get(0) == null || resp.getData().get(0).getTextValue() == null ? "" : resp.getData().get(0).getTextValue();
                }
            }).onErrorReturn(new Function<Throwable, String>() {
                @Override
                public String apply(@NonNull Throwable throwable) throws Exception {
                    return "";
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        else
            return api.getGeoDetailByCity(new LocalityReq(new LocalityReq.Snippet(cityId + ""))).map(new Function<CommonIdResp, String>() {
                @Override
                public String apply(@NonNull CommonIdResp resp) throws Exception {
                    return resp == null || resp.getData() == null || resp.getData().isEmpty() || resp.getData().get(0) == null || resp.getData().get(0).getTextValue() == null ? "" : resp.getData().get(0).getTextValue();
                }
            }).onErrorReturn(new Function<Throwable, String>() {
                @Override
                public String apply(@NonNull Throwable throwable) throws Exception {
                    return "";
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());


    }

    public void checkGeoDetail() {
        if (TextUtils.isEmpty(Constants.GEO_TAG))
            getGeoDetail(pref.getInt(Constants.SAVED_CITY_ID, -1)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                @Override
                public void accept(@NonNull String s) throws Exception {
                    Constants.GEO_TAG = s;
                    registerUserDevice();
                }
            });
    }

    public void checkGeoDetail(final Runnable action) {
        if (TextUtils.isEmpty(Constants.GEO_TAG))
            getGeoDetail(pref.getInt(Constants.SAVED_CITY_ID, -1)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
                @Override
                public void accept(@NonNull String s) throws Exception {
                    Constants.GEO_TAG = s;
                    registerUserDevice();
                    try {
                        action.run();
                    } catch (Exception e) {
                    }
                }
            });
    }

    public void checkGeoDetail(final String fcm) {
        getGeoDetail(pref.getInt(Constants.SAVED_CITY_ID, -1)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                Constants.GEO_TAG = s;
                registerUserDevice(fcm);
            }
        });
    }

    private boolean isDigitsOnly(String text) {
        return !isEmpty(text) && text.matches("[0-9]+");
    }

    public boolean isEmpty(@Nullable String text) {
        return text == null || TextUtils.isEmpty(text.trim());
    }

    public Observable<CODOfferDetailResp> getCODOfferDetail(PromoCodeReq.Snippet snippet) {
        return api.getCODOfferDetail(new PromoCodeReq(snippet)).onErrorReturnItem(new CODOfferDetailResp()).subscribeOn(Schedulers.io());
    }

    public Observable<UserGeoLocationResp> getUserGeoLocation() {
        return api.getUserGeoLocation(new UserGeoLocationReq()).onErrorReturnItem(new UserGeoLocationResp()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DeepLinkDataResp> getDeepLinkData(String url) {
        return api.getDeepLinkData(new DeepLinkDataReq(url)).subscribeOn(Schedulers.io()).observeOn(Schedulers.computation());
    }

    public static enum FilterType {
        Category, Segment, City, Locality, Community, ClassType, ClassSchedule, VendorList
    }

    public class NameIdPair {
        public String name;
        public Integer id;
        FilterType type;

        public NameIdPair(FilterType type, String name, Integer id) {
            this.name = name;
            this.type = type;
            this.id = id;
        }
    }
}

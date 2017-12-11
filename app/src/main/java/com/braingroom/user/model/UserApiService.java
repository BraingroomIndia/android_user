package com.braingroom.user.model;


import com.braingroom.user.model.request.*;
import com.braingroom.user.model.response.*;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface UserApiService {


    @POST("getCatalogueCities")
    Observable<CommonIdResp> getCatalogueCities();

    @POST("getVendor")
    Observable<CommonIdResp> getVendors();

    @POST("getLocality")
    Observable<CommonIdResp> getLocalities(@Body LocalityReq req);

    @POST("viewIndianClass")
    Observable<ClassListResp> getIndianClass();

    @POST("viewClassDetail")
    Observable<ClassListResp> getClassDetail(@Body ClassDetailReq req);

    @POST("contactAdmin")
    Observable<BaseResp> contactAdmin(@Body ContactAdmin req);

    @POST("getQuote")
    Observable<BaseResp> getQuote(@Body QuoteReq req);

    @POST("getProfile")
    Observable<ProfileResp> getProfile(@Body CommonIdReq req);

    @Multipart
    @POST
    Observable<UploadResp> uploadImage(@Url String url, @Part MultipartBody.Part file);

    @Multipart
    @POST
    Observable<UploadPostApiResp> postApiUpload(
            @Url String url
            , @Part MultipartBody.Part file
            , @Part("post_type") RequestBody post_type
    );

    @POST("getSegment")
    Observable<SegmentResp> getSegments(@Body SegmentReq req);

    @POST("getCategoryClassSearch")
    Observable<ClassListResp> classSearch(@Body SearchReq req);

    @POST("generalFilter/{pageIndex}")
    Observable<ClassListResp> generalFilter(@Path("pageIndex") String pageIndex, @Body GeneralFilterReq req);

    @POST("getAllWishList/{pageIndex}")
    Observable<ClassListResp> getWishlist(@Path("pageIndex") String pageIndex, @Body CommonUuidReq req);

    @POST("bookingHistory/{pageIndex}")
    Observable<BookingHistoryResp> getBookingHistory(@Path("pageIndex") String pageIndex, @Body CommonIdReq req);

    @POST("VendorProfile/{pageIndex}")
    Observable<VendorProfileResp> getVendorProfile(@Path("pageIndex") String pageIndex, @Body CommonIdReq req);


    @POST("exploreDashboard")
    Observable<ExploreResp> getExploreDashboard(@Body ExploreReq exploreReq);

    @POST("exploreFilter")
    Observable<ExploreResp> exploreFilter(@Body ExploreReq exploreReq);

    @POST("verifyPromoCode")
    Observable<PromocodeResp> applyPromoCode(@Body PromoCodeReq req);

    @POST("applyCoupon")
    Observable<PromocodeResp> applyCouponCode(@Body CouponCodeReq req);

    @POST("generateHashPayu")
    Observable<PayUHashResp> generatePayUHash(@Body PayUHashGenReq req);

    @POST("getBookNowPageDetails")
    Observable<PayUBookingDetailsResp> getBookingDetails(@Body GetBookingDetailsReq req);

    @POST("saveMandatoryData")
    Observable<FirstSocialLoginResp> firstSocialLogin(@Body FirstSocialLoginReq req);

    @POST("razorPaySuccess")
    Observable<RazorSuccessResp> postRazorPaySuccess(@Body RazorSuccessReq req);

//    @POST("getGroups")
//    @Headers("X-App-Type: BGUSR01")
//    Observable<GroupResp> getGroups();

    @POST("VendorReviews")
    Observable<VendorReviewResp> getVendorReviews(@Body VendorReviewReq req);

    @POST("exploreMarkerData")
    Observable<MarkerDataResp> exploreMarkerData(@Body MarkerDataReq req);

    @POST("addWishList")
    Observable<WishlistResp> addToWishlist(@Body WishlistReq req);

    @POST("updateProfile")
    Observable<CommonIdResp> updateProfile(@Body ProfileUpdateReq req);

    @POST("getCountry")
    Observable<CommonIdResp> getCountry();

    @POST("getCountry")
    Observable<CommonIdResp> getMajorCountry(@Body CountryReq req);

    @POST("getState")
    Observable<CommonIdResp> getState(@Body StateReq req);


    @POST("getInstitions")
    Observable<CommonIdResp> getInstitute(@Body InstituteReq req);

    @POST("getUsers")
    Observable<CommonIdResp> getUser(@Body UserListReq req);


    @POST("getSchools")
    Observable<CommonIdResp> getSchools(@Body InstituteReq req);

    @POST("getCompetitionStatus")
    Observable<CompetitionStatusResp> getCompetitionStatus();

    @POST("getComments")
    Observable<CommentListResp> getComments(@Body PostRelatedReq postRelatedReq);

    @POST("getReplies")
    Observable<CommentViewReply> getReplies(@Body CommentViewReplyReq req);

    @POST("getPostLikes")
    Observable<LikedUsersListResp> getPostLikes(@Body PostRelatedReq postRelatedReq);

    @POST("getPostAcceptedUsers")
    Observable<LikedUsersListResp> getAcceptedUsers(@Body PostRelatedReq postRelatedReq);

    @POST("addAccept")
    Observable<BaseResp> addAccept(@Body PostRelatedReq postRelatedReq);

    @POST("getConnectData")
    Observable<GroupResp> getGroups(@Body ConnectDataReq req);

    @POST("getGroupActivities")
    Observable<CommonIdResp> getGroupActivities(@Body CommonIdReq req);


    @POST("getConnectFeedsData/{pageIndex}")
    Observable<ConnectFeedResp> getConnectFeedData(@Path("pageIndex") String pageIndex, @Body ConnectFeedReq req);

    @POST("likeunlike")
    Observable<LikeResp> like(@Body LikeReq req);

    @POST("report")
    Observable<ReportResp> report(@Body ReportReq req);

    @POST("addReview")
    Observable<ReviewAddResp> reviewAdd(@Body ReviewAddReq reviewAddReq);

    @POST("getReviews")
    Observable<ReviewGetResp> reviewGet(@Body ReviewGetReq reviewGetReq);

    @POST("commentReply")
    Observable<CommentReplyResp> commentReply(@Body CommentReplyReq req);


    @POST("addPost")
    Observable<BaseResp> postArticleVideos(@Body ArticleAndVideosPostReq req);

    @POST("addPost")
    Observable<BaseResp> postDecideDiscuss(@Body DecideAndDiscussPostReq req);

    @POST("addPost")
    Observable<BaseResp> postKnowledgeNuggets(@Body KnowledgeNuggetsPostReq req);

    @POST("addPost")
    Observable<BaseResp> postBuyAndSell(@Body BuyAndSellPostReq req);

    @POST("addPost")
    Observable<BaseResp> postLearningPartner(@Body LearningPartnerPostReq req);

    @POST("addGuestUser")
    Observable<GuestUserResp> addGuestUser(@Body GuestUserReq req);

    @POST("getMessage")
    Observable<MessageListResp> getMessages(@Body MessageListReq req);

    @POST("getChatMessages")
    Observable<ChatListResp> getChatMessages(@Body ChatMessageReq req);

    @POST("postMessage")
    Observable<BaseResp> postMessage(@Body MessageReplyReq req);

    @POST("getUserNotifications")
    Observable<NotificationListResp> getUserNotifications(@Body CommonUserIdReq req);

    @POST("changeNotificationStatus")
    Observable<BaseResp> changeNotificationStatus(@Body ChangeNotificationStatusReq req);

    @POST("getUnreadNotificationCount")
    Observable<NotificationCountResp> getUnreadNotificationCount(@Body CommonUserIdReq req);

    @POST("getUnreadMessageCount")
    Observable<NotificationCountResp> getUnreadMessageCount(@Body CommonUserIdReq req);

    @POST("changeMessageThreadStatus")
    Observable<BaseResp> changeMessageThreadStatus(@Body ChatMessageReq req);

    @POST("getFeedsByPostID")
    Observable<ConnectFeedResp> getFeedsByPostID(@Body ConnectPostByIdReq req);

    @POST("viewThirdPartProfile")
    Observable<ThirdPartyProfileResp> getThirdPartyProfile(@Body ThirdPartyProfileReq req);

    @POST("getConnectGroup")
    Observable<CatalogueGroupResp> getCatalogueGroup();

    @POST("getGiftCatagories")
    Observable<GiftcardResp> getGiftcards();

    @POST("saveGiftCoupon")
    Observable<BaseResp> saveGiftCoupon(@Body GiftCouponReq req);

//    @POST("getCorporateGiftcards")
//    @Headers("X-App-Type: BGUSR01")
//    Observable<GiftcardResp> getCorporateGiftcards();
//
//    @POST("getNgoGiftcards")
//    @Headers("X-App-Type: BGUSR01")
//    Observable<GiftcardResp> getNgoGiftcards();

    @POST("getNGOListByNGOCatagory")
    Observable<CommonIdResp> getNgoList(@Body CommonIdReq req);

    @POST("getCategoriesByNGOCatagory")
    Observable<CommonIdResp> getNgoSegments(@Body CommonIdReq req);

    @POST("saveGiftCoupon")
    Observable<SaveGiftCouponResp> saveGiftCoupon(@Body SaveGiftCouponReq req);

    @POST("razorBuySuccess")
    Observable<BaseResp> updateCouponPaymentSuccess(@Body RazorBuySuccessReq req);

    @POST("sendOTP")
    Observable<BaseResp> sendOTP(@Body OTPReq req);


    @POST("verifyOTP")
    Observable<BaseResp> submitOTP(@Body SubmitOTPReq req);

    @POST("followUser")
    Observable<FollowResp> follow(@Body FollowReq req);

    @POST("getFollowers")
    Observable<LikedUsersListResp> getFollowers(@Body CommonUserIdReq req);

    @POST("getFollowingUsers")
    Observable<LikedUsersListResp> getFollowingUsers(@Body CommonUserIdReq req);

    @POST("getConnectSections")
    Observable<ConnectSectionResp> getConnectSections();

    @POST("getQuoteDetails")
    Observable<QuoteDetailsResp> getQuoteDetails(@Body QuoteDetailsReq req);


    @POST("registerUserDevice")
    Observable<BaseResp> registerUserDevice(@Body RegisterUserDeviceReq req);

    @POST("login")
    Observable<LoginResp> login(@Body LoginReq req);

    @POST("socialLogin")
    Observable<LoginResp> socialLogin(@Body SocialLoginReq req);

    @POST("forgotPassword")
    Observable<LoginResp> forgotPassword(@Body LoginReq req);

    @POST("changePassword")
    Observable<ChangePasswordResp> changePassword(@Body ChangePasswordReq req);

    @POST("BuyerRegistration")
    Observable<SignUpResp> BuyerRegistration(@Body SignUpReq req);

    @POST("checkReferal")
    Observable<ReferralCodeResp> checkReferal(@Body ReferralCodeReq req);

    @POST("logout")
    Observable<BaseResp> logout(@Body LogoutReq req);

    @POST("getCommunity")
    Observable<CommunityResp> getCommunity();

    @POST("getCategory")
    Observable<CategoryResp> getCategories();

    @POST("viewFeaturedClass")
    Observable<ClassListResp> getFeaturedClass();

    @POST("viewRecommendedClass")
    Observable<ClassListResp> getRecommendedClass();

    @POST("viewTrendingClass")
    Observable<ClassListResp> getTrendingClass();

    @POST("getCity")
    Observable<CommonIdResp> getCityList(@Body CityReq req);

    @POST("contactTutor")
    Observable<ContactTutorResp> contactTutor(@Body ContactTutorReq req);

    @POST("getPrimeMessage")
    Observable<PrimeMessageResp> getPrimeMessage();

    @GET("getWeeklyPerformers")
    Observable<WinnerResp> getWeeklyPerformers();

    @POST("getLearnerAppMinVersion")
    Observable<CommonIdResp> getLearnerAppMinVersion();

    @POST("getPromoPopup")
    Observable<PromoInfo> getPromoInfo(@Body PromoCodeReq req);

    @GET("getGeoDetail")
    Observable<CommonIdResp> getGeoDetail();

    @POST("getGeoDetailByCity")
    Observable<CommonIdResp> getGeoDetailByCity(@Body LocalityReq req);

    @POST("getCODOfferDetail")
    Observable<CODOfferDetailResp> getCODOfferDetail(@Body PromoCodeReq req);


}

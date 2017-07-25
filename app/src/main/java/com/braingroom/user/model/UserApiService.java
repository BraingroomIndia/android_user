package com.braingroom.user.model;


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
import com.braingroom.user.model.request.ReportReq;
import com.braingroom.user.model.request.SaveGiftCouponReq;
import com.braingroom.user.model.request.SearchReq;
import com.braingroom.user.model.request.SegmentReq;
import com.braingroom.user.model.request.SignUpReq;
import com.braingroom.user.model.request.SocialLoginReq;
import com.braingroom.user.model.request.StateReq;
import com.braingroom.user.model.request.SubmitOTPReq;
import com.braingroom.user.model.request.UserListReq;
import com.braingroom.user.model.request.VendorReviewReq;
import com.braingroom.user.model.request.WishlistReq;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.BookingHistoryResp;
import com.braingroom.user.model.response.CatalogueGroupResp;
import com.braingroom.user.model.response.CategoryResp;
import com.braingroom.user.model.response.ChangePasswordResp;
import com.braingroom.user.model.response.ChatListResp;
import com.braingroom.user.model.response.ClassListResp;
import com.braingroom.user.model.response.CommentListResp;
import com.braingroom.user.model.response.CommentReplyResp;
import com.braingroom.user.model.response.CommonIdResp;
import com.braingroom.user.model.response.CommunityResp;
import com.braingroom.user.model.response.CompetitionStatusResp;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.model.response.ExploreResp;
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
import com.braingroom.user.viewmodel.OTPReq;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface UserApiService {
    @Headers("X-App-Type: BGUSR01")


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

    @POST("logout")
    Observable<BaseResp> logout(@Body LogoutReq req);

    @POST("getCommunity")
    Observable<CommunityResp> getCommunity();

    @POST("getCategory")
    Observable<CategoryResp> getCategories();

    @POST("viewFeaturedClass")
    Observable<ClassListResp> getFeaturedClass();

    @POST("viewTrendingClass")
    Observable<ClassListResp> getTrendingClass();

    @POST("getCity")
    Observable<CommonIdResp> getCityList(@Body CityReq req);

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
    Observable<PromocodeResp> applyPromoCode(@Body PromocodeReq req);

    @POST("applyCoupon")
    Observable<PromocodeResp> applyCouponCode(@Body CouponCodeReq req);

    @POST("generateHashPayu")
    Observable<PayUHashResp> generatePayUHash(@Body PayUHashGenReq req);

    @POST("getBookNowPageDetails")
    Observable<PayUBookingDetailsResp> getBookingDetails(@Body PayUBookingDetailsReq req);

    @POST("saveMandatoryData")
    Observable<BaseResp> firstSocialLogin(@Body FirstSocialLoginReq req);

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
    Observable<ThirdPartyProfileResp> getThirdPartyProfile(@Body CommonIdReq req);

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
    Observable<BaseResp>submitOTP(@Body SubmitOTPReq req);

}

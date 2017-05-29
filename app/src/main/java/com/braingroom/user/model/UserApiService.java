package com.braingroom.user.model;



import com.braingroom.user.model.request.*;
import com.braingroom.user.model.response.*;


import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface UserApiService {

    @POST("login")
    @Headers("X-App-Type: BGUSR01")
    Observable<LoginResp> login(@Body LoginReq req);

    @POST("socialLogin")
    @Headers("X-App-Type: BGUSR01")
    Observable<LoginResp> socialLogin(@Body SocialLoginReq req);

    @POST("forgotPassword")
    @Headers("X-App-Type: BGUSR01")
    Observable<LoginResp> forgotPassword(@Body LoginReq req);

    @POST("changePassword")
    @Headers("X-App-Type: BGUSR01")
    Observable<ChangePasswordResp> changePassword(@Body ChangePasswordReq req);

    @POST("BuyerRegistration")
    @Headers("X-App-Type: BGUSR01")
    Observable<SignUpResp> BuyerRegistration(@Body SignUpReq req);

    @POST("getCommunity")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommunityResp> getCommunity();

    @POST("getCategory")
    @Headers("X-App-Type: BGUSR01")
    Observable<CategoryResp> getCategories();

    @POST("viewFeaturedClass")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> getFeaturedClass();

    @POST("viewTrendingClass")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> getTrendingClass();

    @POST("getCity")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getCityList(@Body CityReq req);

    @POST("getVendor")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getVendors();

    @POST("getLocality")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getLocalities(@Body LocalityReq req);

    @POST("viewIndianClass")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> getIndianClass();

    @POST("viewClassDetail")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> getClassDetail(@Body ClassDetailReq req);

    @POST("getProfile")
    @Headers("X-App-Type: BGUSR01")
    Observable<ProfileResp> getProfile(@Body CommonIdReq req);

    @Multipart
    @POST
    @Headers("X-App-Type: BGUSR01")
    Observable<UploadResp> uploadImage(@Url String url, @Part MultipartBody.Part file);

    @POST("getSegment")
    @Headers("X-App-Type: BGUSR01")
    Observable<SegmentResp> getSegments(@Body SegmentReq req);

    @POST("getCategoryClassSearch")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> classSearch(@Body SearchReq req);

    @POST("generalFilter/{pageIndex}")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> generalFilter(@Path("pageIndex") String pageIndex, @Body GeneralFilterReq req);

    @POST("getAllWishList")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> getWishlist(@Body CommonUuidReq req);

    @POST("VendorProfile")
    @Headers("X-App-Type: BGUSR01")
    Observable<VendorProfileResp> getVendorProfile(@Body CommonIdReq req);

    @POST("bookingHistory")
    @Headers("X-App-Type: BGUSR01")
    Observable<BookingHistoryResp> getBookingHistory(@Body CommonIdReq req);

    @POST("exploreDashboard")
    @Headers("X-App-Type: BGUSR01")
    Observable<ExploreResp> getExploreDashboard(@Body ExploreReq exploreReq);

    @POST("exploreFilter")
    @Headers("X-App-Type: BGUSR01")
    Observable<ExploreResp> exploreFilter(@Body ExploreReq exploreReq);

    @POST("verifyPromoCode")
    @Headers("X-App-Type: BGUSR01")
    Observable<PromocodeResp> applyPromoCode(@Body PromocodeReq req);

    @POST("generateHashPayu")
    @Headers("X-App-Type: BGUSR01")
    Observable<PayUHashResp> generatePayUHash(@Body PayUHashGenReq req);

    @POST("getBookNowPageDetails")
    @Headers("X-App-Type: BGUSR01")
    Observable<PayUBookingDetailsResp> getBookingDetails(@Body PayUBookingDetailsReq req);

    @POST("saveMandatoryData")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> firstSocialLogin(@Body FirstSocialLoginReq req);

    @POST("razorPaySuccess")
    @Headers("X-App-Type: BGUSR01")
    Observable<RazorSuccessResp> postRazorPaySuccess(@Body RazorSuccessReq req);

//    @POST("getGroups")
//    @Headers("X-App-Type: BGUSR01")
//    Observable<GroupResp> getGroups();

    @POST("VendorReviews")
    @Headers("X-App-Type: BGUSR01")
    Observable<VendorReviewResp> getVendorReviews(@Body VendorReviewReq req);

    @POST("exploreMarkerData")
    @Headers("X-App-Type: BGUSR01")
    Observable<MarkerDataResp> exploreMarkerData(@Body MarkerDataReq req);

    @POST("addWishList")
    @Headers("X-App-Type: BGUSR01")
    Observable<WishlistResp> addToWishlist(@Body WishlistReq req);

    @POST("updateProfile")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> updateProfile(@Body ProfileUpdateReq req);

    @POST("getCountry")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getCountry();

    @POST("getState")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getState(@Body StateReq req);



    @POST("getInstitions")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getInstitute(@Body InstituteReq req);

    @POST("getComments")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommentListResp> getComments(@Body PostRelatedReq postRelatedReq);

    @POST("getReplies")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommentViewReply> getReplies(@Body CommentViewReplyReq req);

    @POST("getPostLikes")
    @Headers("X-App-Type: BGUSR01")
    Observable<LikedUsersListResp> getPostLikes(@Body PostRelatedReq postRelatedReq);

    @POST("getConnectData")
    @Headers("X-App-Type: BGUSR01")
    Observable<GroupResp> getGroups(@Body ConnectDataReq req);

    @POST("getConnectFeedsData/{pageIndex}")
    @Headers("X-App-Type: BGUSR01")
    Observable<ConnectFeedResp> getConnectFeedData(@Path("pageIndex") String pageIndex, @Body ConnectFeedReq req);

    @POST("likeunlike")
    @Headers("X-App-Type: BGUSR01")
    Observable<LikeResp> like(@Body LikeReq req);

    @POST("report")
    @Headers("X-App-Type: BGUSR01")
    Observable<ReportResp> report(@Body ReportReq req);

    @POST("commentReply")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommentReplyResp> commentReply(@Body CommentReplyReq req);


    @POST("addPost")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postArticleVideos(@Body ConnectPostReq.ArticleAndVideos req);

    @POST("addPost")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postDecideDiscuss(@Body ConnectPostReq.DecideAndDiscuss req);

    @POST("addPost")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postKnowledgeNuggets(@Body ConnectPostReq.KnowledgeNuggets req);

    @POST("addPost")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postBuyAndSell(@Body ConnectPostReq.BuyAndSell req);

    @POST("addPost")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postLearningPartner(@Body ConnectPostReq.LearningPartner req);

    @POST("addGuestUser")
    @Headers("X-App-Type: BGUSR01")
    Observable<GuestUserResp> addGuestUser(@Body GuestUserReq req);

    @POST("getMessage")
    @Headers("X-App-Type: BGUSR01")
    Observable<MessageListResp> getMessages(@Body MessageListReq req);

    @POST("postMessage")
    @Headers("X-App-Type: BGUSR01")
    Observable<MessageListResp> postMessage(@Body MessageListReq req);

    @POST("getUserNotifications")
    @Headers("X-App-Type: BGUSR01")
    Observable<NotificationListResp> getUserNotifications(@Body CommonUserIdReq req);

    @POST("getFeedsByPostID")
    @Headers("X-App-Type: BGUSR01")
    Observable<ConnectFeedResp> getFeedsByPostID(@Body ConnectPostByIdReq req);

}

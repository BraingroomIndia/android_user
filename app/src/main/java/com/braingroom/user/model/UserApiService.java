package com.braingroom.user.model;


import com.braingroom.user.model.request.ArticleAndVideosPostReq;
import com.braingroom.user.model.request.BuyAndSellPostReq;
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
import com.braingroom.user.model.request.DecideAndDiscussPostReq;
import com.braingroom.user.model.request.ExploreReq;
import com.braingroom.user.model.request.FirstSocialLoginReq;
import com.braingroom.user.model.request.GeneralFilterReq;
import com.braingroom.user.model.request.GuestUserReq;
import com.braingroom.user.model.request.InstituteReq;
import com.braingroom.user.model.request.KnowledgeNuggetsPostReq;
import com.braingroom.user.model.request.LearningPartnerPostReq;
import com.braingroom.user.model.request.LikeReq;
import com.braingroom.user.model.request.LocalityReq;
import com.braingroom.user.model.request.LoginReq;
import com.braingroom.user.model.request.MarkerDataReq;
import com.braingroom.user.model.request.MessageListReq;
import com.braingroom.user.model.request.MessageReplyReq;
import com.braingroom.user.model.request.PayUBookingDetailsReq;
import com.braingroom.user.model.request.PayUHashGenReq;
import com.braingroom.user.model.request.PostRelatedReq;
import com.braingroom.user.model.request.ProfileUpdateReq;
import com.braingroom.user.model.request.PromocodeReq;
import com.braingroom.user.model.request.RazorSuccessReq;
import com.braingroom.user.model.request.ReportReq;
import com.braingroom.user.model.request.SearchReq;
import com.braingroom.user.model.request.SegmentReq;
import com.braingroom.user.model.request.SignUpReq;
import com.braingroom.user.model.request.SocialLoginReq;
import com.braingroom.user.model.request.StateReq;
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
import com.braingroom.user.model.response.NotificationListResp;
import com.braingroom.user.model.response.PayUBookingDetailsResp;
import com.braingroom.user.model.response.PayUHashResp;
import com.braingroom.user.model.response.ProfileResp;
import com.braingroom.user.model.response.PromocodeResp;
import com.braingroom.user.model.response.RazorSuccessResp;
import com.braingroom.user.model.response.ReportResp;
import com.braingroom.user.model.response.SegmentResp;
import com.braingroom.user.model.response.SignUpResp;
import com.braingroom.user.model.response.ThirdPartyProfileResp;
import com.braingroom.user.model.response.UploadPostApiResp;
import com.braingroom.user.model.response.UploadResp;
import com.braingroom.user.model.response.VendorProfileResp;
import com.braingroom.user.model.response.VendorReviewResp;
import com.braingroom.user.model.response.WishlistResp;

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

    @POST("contactAdmin")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> contactAdmin(@Body ContactAdmin req);

    @POST("getProfile")
    @Headers("X-App-Type: BGUSR01")
    Observable<ProfileResp> getProfile(@Body CommonIdReq req);

    @Multipart
    @POST
    @Headers("X-App-Type: BGUSR01")
    Observable<UploadResp> uploadImage(@Url String url, @Part MultipartBody.Part file);

    @Multipart
    @POST
    @Headers("X-App-Type: BGUSR01")
    Observable<UploadPostApiResp> postApiUpload(
            @Url String url
            , @Part MultipartBody.Part file
            , @Part("post_type") RequestBody post_type
    );

    @POST("getSegment")
    @Headers("X-App-Type: BGUSR01")
    Observable<SegmentResp> getSegments(@Body SegmentReq req);

    @POST("getCategoryClassSearch")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> classSearch(@Body SearchReq req);

    @POST("generalFilter/{pageIndex}")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> generalFilter(@Path("pageIndex") String pageIndex, @Body GeneralFilterReq req);

    @POST("getAllWishList/{pageIndex}")
    @Headers("X-App-Type: BGUSR01")
    Observable<ClassListResp> getWishlist(@Path("pageIndex") String pageIndex, @Body CommonUuidReq req);

    @POST("bookingHistory/{pageIndex}")
    @Headers("X-App-Type: BGUSR01")
    Observable<BookingHistoryResp> getBookingHistory(@Path("pageIndex") String pageIndex, @Body CommonIdReq req);

    @POST("VendorProfile/{pageIndex}")
    @Headers("X-App-Type: BGUSR01")
    Observable<VendorProfileResp> getVendorProfile(@Path("pageIndex") String pageIndex, @Body CommonIdReq req);


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

    @POST("getSchools")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getSchools(@Body InstituteReq req);

    @POST("getCompetitionStatus")
    @Headers("X-App-Type: BGUSR01")
    Observable<CompetitionStatusResp> getCompetitionStatus();

    @POST("getComments")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommentListResp> getComments(@Body PostRelatedReq postRelatedReq);

    @POST("getReplies")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommentViewReply> getReplies(@Body CommentViewReplyReq req);

    @POST("getPostLikes")
    @Headers("X-App-Type: BGUSR01")
    Observable<LikedUsersListResp> getPostLikes(@Body PostRelatedReq postRelatedReq);

    @POST("getPostAcceptedUsers")
    @Headers("X-App-Type: BGUSR01")
    Observable<LikedUsersListResp> getAcceptedUsers(@Body PostRelatedReq postRelatedReq);

    @POST("addAccept")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> addAccept(@Body PostRelatedReq postRelatedReq);

    @POST("getConnectData")
    @Headers("X-App-Type: BGUSR01")
    Observable<GroupResp> getGroups(@Body ConnectDataReq req);

    @POST("getGroupActivities")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getGroupActivities(@Body CommonIdReq req);


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
    Observable<BaseResp> postArticleVideos(@Body ArticleAndVideosPostReq req);

    @POST("addPost")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postDecideDiscuss(@Body DecideAndDiscussPostReq req);

    @POST("addPost")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postKnowledgeNuggets(@Body KnowledgeNuggetsPostReq req);

    @POST("addPost")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postBuyAndSell(@Body BuyAndSellPostReq req);

    @POST("addPost")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postLearningPartner(@Body LearningPartnerPostReq req);

    @POST("addGuestUser")
    @Headers("X-App-Type: BGUSR01")
    Observable<GuestUserResp> addGuestUser(@Body GuestUserReq req);

    @POST("getMessage")
    @Headers("X-App-Type: BGUSR01")
    Observable<MessageListResp> getMessages(@Body MessageListReq req);

    @POST("getChatMessages")
    @Headers("X-App-Type: BGUSR01")
    Observable<ChatListResp> getChatMessages(@Body ChatMessageReq req);

    @POST("postMessage")
    @Headers("X-App-Type: BGUSR01")
    Observable<BaseResp> postMessage(@Body MessageReplyReq req);

    @POST("getUserNotifications")
    @Headers("X-App-Type: BGUSR01")
    Observable<NotificationListResp> getUserNotifications(@Body CommonUserIdReq req);

    @POST("getFeedsByPostID")
    @Headers("X-App-Type: BGUSR01")
    Observable<ConnectFeedResp> getFeedsByPostID(@Body ConnectPostByIdReq req);

    @POST("viewThirdPartProfile")
    @Headers("X-App-Type: BGUSR01")
    Observable<ThirdPartyProfileResp> getThirdPartyProfile(@Body CommonIdReq req);

    @POST("getConnectGroup")
    @Headers("X-App-Type: BGUSR01")
    Observable<CatalogueGroupResp> getCatalogueGroup();

    @POST("getGiftCatagories")
    @Headers("X-App-Type: BGUSR01")
    Observable<GiftcardResp> getGiftcards();

//    @POST("getCorporateGiftcards")
//    @Headers("X-App-Type: BGUSR01")
//    Observable<GiftcardResp> getCorporateGiftcards();
//
//    @POST("getNgoGiftcards")
//    @Headers("X-App-Type: BGUSR01")
//    Observable<GiftcardResp> getNgoGiftcards();

    @POST("getNGOListByNGOCatagory")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getNgoList(@Body CommonIdReq req);

    @POST("getCategoriesByNGOCatagory")
    @Headers("X-App-Type: BGUSR01")
    Observable<CommonIdResp> getNgoSegments(@Body CommonIdReq req);

}

package com.braingroom.user.viewmodel;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.FilterData;
import com.braingroom.user.model.response.BaseResp;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.model.response.LikeResp;
import com.braingroom.user.model.response.ReportResp;
import com.braingroom.user.utils.Constants;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ClassListActivity;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.MessagesThreadActivity;
import com.braingroom.user.view.activity.PostDetailActivity;
import com.braingroom.user.view.activity.ThirdPartyViewActivity;
import com.braingroom.user.view.activity.VendorProfileActivity;


import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class ConnectFeedItemViewModel extends ViewModel {

    public static final String TAG = ConnectHomeActivity.class.getSimpleName();

    @NonNull
    public final ObservableField<String> vendorImage;

    @NonNull
    public final ObservableField<String> vendorName;

    @NonNull
    public final ObservableField<String> vendorCollege;

    @NonNull
    public final ObservableField<String> date;

    @NonNull
    public final ObservableField<String> segment;

    @NonNull
    public final ObservableField<String> title;

    @NonNull
    public final ObservableField<String> description;

    @NonNull
    public final ObservableInt numLikes, numAccepts;

    @NonNull
    public final ObservableField<String> numComments;

    @NonNull
    public final ObservableField<String> image;

    @NonNull
    public final ObservableField<String> video;

    @NonNull
    public final ObservableField<String> videoThumb;

    @NonNull
    public final ObservableBoolean liked, reported, isPostOwner, isMediaAvailable;

    @NonNull
    public final ObservableBoolean accepted;

    @NonNull
    public final ObservableBoolean isSegmentAvailable = new ObservableBoolean(true);

    @NonNull
    public final Action likeAction, commentAction, reportAction,
            likedUsersAction, playAction, detailShowAction, acceptAction, shareAction, showAcceptedUsers, showthirdpartyProfile, onMessageClick, openSegment;

    @NonNull
    public final Navigator navigator;

    public final String postType;

    public final boolean isActivityRequest;
    public final ObservableBoolean isVendor = new ObservableBoolean(false);

    public ObservableInt categoryImg = new ObservableInt();

    public final int[] resArray = new int[]{R.drawable.main_category_1,
            R.drawable.main_category_5, //Edited By Vikas Godara
            R.drawable.main_category_3,
            R.drawable.main_category_4,
            R.drawable.main_category_2, //Edited By Vikas Godara
            R.drawable.main_category_6};

    public final FollowButtonViewModel followButtonVm;

    public ConnectFeedItemViewModel(@NonNull final ConnectFeedResp.Snippet data, @NonNull final ConnectUiHelper uiHelper, @NonNull final HelperFactory helperFactory
            , @NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        followButtonVm = new FollowButtonViewModel(helperFactory, messageHelper, navigator,
                data.getFollowStatus() == 0 ? FollowButtonViewModel.STATE_FOLLOW : FollowButtonViewModel.STATE_FOLLOWED);
        if (data.getCategoryId() == null && data.getSegId() == null)
            isSegmentAvailable.set(false);
        if (data.getCategoryId() != null)
            categoryImg.set(resArray[Integer.parseInt(data.getCategoryId()) - 1]);
        this.navigator = navigator;
        this.vendorImage = new ObservableField<>(data.getVendorImage());
        this.date = new ObservableField<>(getHumanDate(data.getDate()));
        this.segment = new ObservableField<>(data.getSegName());
        this.title = new ObservableField<>(data.getTitle());
        this.description = new ObservableField<>(data.getDescription());
        this.numLikes = new ObservableInt("".equals(data.getNumLikes()) ? 0 : Integer.parseInt(data.getNumLikes()));
        this.numComments = new ObservableField<>(data.getNumComments());
        this.vendorName = new ObservableField<>(data.getVendorName());
        if (data.getInstituteName().equals(""))
            this.vendorCollege = null;
        else
            this.vendorCollege = new ObservableField<>(data.getInstituteName());

        this.image = new ObservableField<>(TextUtils.isEmpty(data.getImage()) ? null : data.getImage());
        this.video = new ObservableField<>(getVideoId(data.getVideo()));
        this.videoThumb = new ObservableField<>(TextUtils.isEmpty(video.get()) ? null : "http://img.youtube.com/vi/" + video.get() + "/hqdefault.jpg");
        this.liked = new ObservableBoolean(data.getLiked() == 0 ? false : true);
        // Log.d(TAG, "ConnectFeedItemViewModel: \n videoUrl \t " + video.get() + "\n videoThumb \t" + videoThumb.get() + "\n");
        this.reported = new ObservableBoolean(data.getReported() == 0 ? false : true);
        this.postType = data.getPostType();
        this.isActivityRequest = "activity_request".equalsIgnoreCase(postType);
        this.isVendor.set("vendor_article".equalsIgnoreCase(postType));
        this.accepted = new ObservableBoolean(data.getIsAccepted() == 1);
        this.numAccepts = new ObservableInt(data.getNumAccepted());
        this.isPostOwner = new ObservableBoolean(pref.getString(Constants.BG_ID, "").equals(data.getPostOwner()));
        this.isMediaAvailable = new ObservableBoolean(data.getVideo() != null || !data.getImage().equals(""));


        openSegment = new Action() {
            @Override
            public void run() throws Exception {
                if (!data.getCategoryId().equals("-1")) {
                    Bundle bundle = new Bundle();
                    FilterData filterData = new FilterData();
                    filterData.setCategoryId(data.getCategoryId());
                    filterData.setSegmentId(data.getSegId());
                    bundle.putSerializable("filterData", filterData);
                    bundle.putString("origin", ClassListViewModel1.ORIGIN_HOME);
                /*    try {
                        ZohoSalesIQ.Tracking.setCustomAction("Find relevant classes clicked from connect page categoryId \t" +data.getCategoryId() + "segmentId\t" +data.getSegId() );
                    } catch (PEXException e) {
                        e.printStackTrace();
                    }*/
                    navigator.navigateActivity(ClassListActivity.class, bundle);
                    return;
                }
                return;
            }
        };


        detailShowAction = new Action() {
            @Override
            public void run() throws Exception {
                Bundle bundleData = new Bundle();
                bundleData.putString("postId", data.getId());
                navigator.navigateActivity(PostDetailActivity.class, bundleData);
            }
        };

        onMessageClick = new Action() {
            @Override
            public void run() throws Exception {
                if (!getLoggedIn()) {

                    Bundle data = new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("Only logged in users can send a message", data);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("sender_id", data.getPostOwner());
                bundle.putString("sender_name", data.getVendorName());
             /*   try {
                    ZohoSalesIQ.Tracking.setCustomAction("Message Icon clicked from connect page");
                } catch (PEXException e) {
                    e.printStackTrace();
                }*/

                navigator.navigateActivity(MessagesThreadActivity.class, bundle);

            }
        };


        showthirdpartyProfile = new Action() {
            @Override
            public void run() throws Exception {
             /*   try {
                    ZohoSalesIQ.Tracking.setCustomAction("Third party clicked from connect page");
                } catch (PEXException e) {
                    e.printStackTrace();
                }*/
                Bundle bundleData = new Bundle();
                if (data.getPostType().equalsIgnoreCase("vendor_article")) {
                    bundleData.putString("id", data.getPostOwner());
                    navigator.navigateActivity(VendorProfileActivity.class, bundleData);
                } else {
                    bundleData.putString("userId", data.getPostOwner());
                    navigator.navigateActivity(ThirdPartyViewActivity.class, bundleData);
                }

            }
        };


        showAcceptedUsers = new Action() {
            @Override
            public void run() throws Exception {
                if (isPostOwner.get()) {
                    uiHelper.openAcceptedUsersFragment(data.getId());
                }
            }
        };
        likeAction = new Action() {
            @Override
            public void run() throws Exception {

                if (!getLoggedIn()) {

                    Bundle data = new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("Only logged in users can like a post", data);
                    return;
                }

                apiService.like(data.getId()).subscribe(new Consumer<LikeResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull LikeResp likeResp) throws Exception {
                        if (likeResp.getData().get(0).getLiked() == 0) {
                            liked.set(false);
                            numLikes.set(numLikes.get() > 0 ? numLikes.get() - 1 : 0);
                        } else {
                            liked.set(true);
                            numLikes.set(numLikes.get() + 1);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        liked.set(!liked.get());
//                        numLikes.set(numLikes.get() - 1);
                    }
                });
                liked.set(!liked.get());
//                numLikes.set(numLikes.get() + 1);
            }
        };
        commentAction = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.openCommentsFragment(data.getId());
            }
        };
        likedUsersAction = new Action() {
            @Override
            public void run() throws Exception {
                uiHelper.openLikesFragment(data.getId(), null, null);
            }
        };
        playAction = new Action() {
            @Override
            public void run() throws Exception {
                navigator.openStandaloneYoutube(video.get());

             /*   try {
                    ZohoSalesIQ.Tracking.setCustomAction("Video played from \t" +data.getId());
                } catch (PEXException e) {
                    e.printStackTrace();
                }*/

            }
        };

        acceptAction = new Action() {
            @Override
            public void run() throws Exception {
                if (!getLoggedIn()) {
                    Bundle data = new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("Only logged in users can accept a request", data);
                    return;
                }
                apiService.addAccept(data.getId()).subscribe(new Consumer<BaseResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull BaseResp baseResp) throws Exception {
                        messageHelper.show(baseResp.getResMsg());
                        if ("1".equals(baseResp.getResCode())) {
                            accepted.set(true);
                            numAccepts.set(numAccepts.get() + 1);
                        } else accepted.set(false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

                    }
                });

            }
        };
        shareAction = new Action() {
            @Override
            public void run() throws Exception {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this post I found at Braingroom : " + data.getShareUrl());
                navigator.navigateActivity(Intent.createChooser(shareIntent, "Share link using"));

            }
        };
        reportAction = new Action() {
            @Override
            public void run() throws Exception {
                if (!getLoggedIn()) {
                    Bundle data = new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("Only logged in users can report a post", data);
                    return;
                }
                apiService.report(data.getId()).subscribe(new Consumer<ReportResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ReportResp resp) throws Exception {
                        messageHelper.show("Reported!");
                        if (resp.getData().get(0).getReported() == 0) {
                            reported.set(false);
                        } else {
                            reported.set(true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                        reported.set(!reported.get());
                    }
                });
                reported.set(!reported.get());

            }
        };

    }

    public void showMenuPopup(View v) {
        PopupMenu.OnMenuItemClickListener clickListener = new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_report) {
                    try {
                        reportAction.run();
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }
                if (item.getItemId() == R.id.action_share) {
                    try {
                        shareAction.run();
                    } catch (Exception e) {
                        // e.printStackTrace();
                    }
                }

                return true;
            }
        };
        if (isActivityRequest)
            navigator.showMenuPopup(R.menu.connect_feed_item_2, v, clickListener);
        else
            navigator.showMenuPopup(R.menu.connect_feed_item_1, v, clickListener);

    }

    private String getVideoId(String videoUrl) {
        if (videoUrl == null) return null;
        try {
            return videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
        } catch (IndexOutOfBoundsException iobe) {
            return null;
        }
    }

    private String getHumanDate(String timeStamp) {
        if (timeStamp == null)
            return "";
        long time = Long.valueOf(timeStamp) * 1000;
        try {
            java.text.DateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
            Date netDate = (new Date(time));
            return sdf.format(netDate);
        } catch (Exception ex) {
            return "";
        }
    }


}

package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.model.response.LikeResp;
import com.braingroom.user.model.response.ReportResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.ConnectUiHelper;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.view.activity.PostDetailActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


public class ConnectFeedItemViewModel extends ViewModel {

    @NonNull
    public final ObservableField<String> vendorImage;

    @NonNull
    public final ObservableField<String> vendorName;

    @NonNull
    public final ObservableField<String> date;

    @NonNull
    public final ObservableField<String> segment;

    @NonNull
    public final ObservableField<String> title;

    @NonNull
    public final ObservableField<String> description;

    @NonNull
    public final ObservableField<Integer> numLikes;

    @NonNull
    public final ObservableField<String> numComments;

    @NonNull
    public final ObservableField<String> image;

    @NonNull
    public final ObservableField<String> video;

    @NonNull
    public final ObservableField<String> videoThumb;

    @NonNull
    public final ObservableBoolean liked, reported;

    @NonNull
    public ObservableBoolean acceptVisibility, accepted;

    @NonNull
    public final Action likeAction, commentAction, reportAction, likedUsersAction, playAction, detailShowAction;

    public ConnectFeedItemViewModel(final ConnectFeedResp.Snippet data, final ConnectUiHelper uiHelper, final HelperFactory helperFactory
            , final MessageHelper messageHelper, final Navigator navigator) {
        this.vendorImage = new ObservableField<>(data.getVendorImage());
        this.date = new ObservableField<>(getHumanDate(data.getDate()));
        this.segment = new ObservableField<>(data.getSegName());
        this.title = new ObservableField<>(data.getTitle());
        this.description = new ObservableField<>(data.getDescription());
        this.numLikes = new ObservableField<>("".equals(data.getNumLikes()) ? 0 : Integer.parseInt(data.getNumLikes()));
        this.numComments = new ObservableField<>(data.getNumComments());
        this.vendorName = new ObservableField<>(data.getVendorName());
        this.image = new ObservableField<>("".equals(data.getImage()) ? null : data.getImage());
        this.video = new ObservableField<>(getVideoId(data.getVideo()));
        this.videoThumb = new ObservableField<>(video.get() == null ? null : "http://img.youtube.com/vi/" + video.get() + "/hqdefault.jpg");
        this.liked = new ObservableBoolean(data.getLiked() == 0 ? false : true);
        this.reported = new ObservableBoolean(data.getReported() == 0 ? false : true);
        this.detailShowAction = new Action() {
            @Override
            public void run() throws Exception {
                Bundle bundleData = new Bundle();
                bundleData.putString("postId", data.getId());
                navigator.navigateActivity(PostDetailActivity.class, bundleData);
            }
        };
        likeAction = new Action() {
            @Override
            public void run() throws Exception {

                if (!loggedIn.get()) {

                    Bundle data =new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("Only logged in users can like a post",data);
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
            }
        };
        reportAction = new Action() {
            @Override
            public void run() throws Exception {
                if (!loggedIn.get()) {
                    Bundle data =new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("Only logged in users can report a post",data);
                    return;
                }
                apiService.report(data.getId()).subscribe(new Consumer<ReportResp>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull ReportResp resp) throws Exception {
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
            return "xx";
        }
    }


}

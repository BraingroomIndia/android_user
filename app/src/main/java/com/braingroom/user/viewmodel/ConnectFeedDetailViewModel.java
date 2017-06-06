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

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ConnectFeedDetailViewModel extends ViewModel {

    @NonNull
    public final ObservableField<String> vendorImage = new ObservableField<>();

    @NonNull
    public final ObservableField<String> vendorName = new ObservableField<>();

    @NonNull
    public final ObservableField<String> date = new ObservableField<>();

    @NonNull
    public final ObservableField<String> segment = new ObservableField<>();

    @NonNull
    public final ObservableField<String> title = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> description = new ObservableField<>("");

    @NonNull
    public final ObservableField<Integer> numLikes = new ObservableField<>();

    @NonNull
    public final ObservableField<String> numComments = new ObservableField<>();

    @NonNull
    public final ObservableField<String> image = new ObservableField<>();

    @NonNull
    public final ObservableField<String> video = new ObservableField<>();

    @NonNull
    public final ObservableField<String> videoThumb = new ObservableField<>();

    @NonNull
    public final ObservableBoolean liked = new ObservableBoolean();
    @NonNull
    public final ObservableBoolean reported = new ObservableBoolean();

    @NonNull
    public ObservableBoolean acceptVisibility, accepted;

    @NonNull
    public final Action likeAction, commentAction, reportAction, likedUsersAction, playAction;

    @NonNull
    String postId;

    public ConnectFeedDetailViewModel(final String postId, final ConnectUiHelper uiHelper, final HelperFactory helperFactory
            , final MessageHelper messageHelper, final Navigator navigator) {

        apiService.getFeedsByPostID(postId).subscribe(new Consumer<ConnectFeedResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ConnectFeedResp resp) throws Exception {

                vendorImage.set(resp.getData().get(0).getVendorImage());
                date.set(getHumanDate(resp.getData().get(0).getDate()));
                segment.set(resp.getData().get(0).getSegName());
                title.set(resp.getData().get(0).getTitle());
                description.set(resp.getData().get(0).getDescription());
                numLikes.set("".equals(resp.getData().get(0).getNumLikes()) ? 0 : Integer.parseInt(resp.getData().get(0).getNumLikes()));
                numComments.set(resp.getData().get(0).getNumComments());
                vendorName.set(resp.getData().get(0).getVendorName());
                image.set("".equals(resp.getData().get(0).getImage()) ? null : resp.getData().get(0).getImage());
                video.set(getVideoId(resp.getData().get(0).getVideo()));
                videoThumb.set(video.get() == null ? null : "http://img.youtube.com/vi/" + video.get() + "/hqdefault.jpg");
                liked.set(resp.getData().get(0).getLiked() == 0 ? false : true);
                reported.set(resp.getData().get(0).getReported() == 0 ? false : true);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {

            }
        });

        likeAction = new Action() {
            @Override
            public void run() throws Exception {

                if (!loggedIn.get()) {
                    Bundle data =new Bundle();
                    data.putString("backStackActivity", ConnectHomeActivity.class.getSimpleName());
                    messageHelper.showLoginRequireDialog("Only logged in users can like a post",data);
                    return;
                }

                if (postId == null) return;

                apiService.like(postId).subscribe(new Consumer<LikeResp>() {
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
                if (postId == null) return;
                uiHelper.openCommentsFragment(postId);
            }
        };
        likedUsersAction = new Action() {
            @Override
            public void run() throws Exception {
                if (postId == null) return;
                uiHelper.openLikesFragment(postId, null, null);
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
                if (postId == null) return;
                apiService.report(postId).subscribe(new Consumer<ReportResp>() {
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

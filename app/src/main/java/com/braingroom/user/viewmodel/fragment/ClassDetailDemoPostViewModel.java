package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.braingroom.user.R;
import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.viewmodel.RowShimmerItemViewModel;
import com.braingroom.user.viewmodel.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import static com.braingroom.user.utils.Constants.defaultProfilePic;

/**
 * Created by godara on 24/07/17.
 */

public class ClassDetailDemoPostViewModel extends ViewModel {

    @NonNull
    public final int profilePicPlaceHolder;
    @NonNull
    public final ObservableField<String> vendorImage = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> vendorName = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> vendorCollege = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> date = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> title = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> description = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> numLikes = new ObservableField<>(""), numAccepts = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> numComments = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> image = new ObservableField<>("");

    @NonNull
    public final ObservableField<String> videoThumb = new ObservableField<>("");
    @NonNull
    public final ObservableBoolean showLoadingItem, showNoPost;


    public final Action openConnect;

    public final int[] profilePicResArray = new int[]{
            R.drawable.man,
            R.drawable.man_1,
            R.drawable.man_2,
            R.drawable.man_3,
            R.drawable.man_4,
            R.drawable.man_5,
            R.drawable.woman,
            R.drawable.woman_1,
            R.drawable.woman_2,
            R.drawable.woman_3,
            R.drawable.woman_4,
            R.drawable.woman_5,

    };

    public ClassDetailDemoPostViewModel(final Navigator navigator, final ConnectFilterData filterData) {
        openConnect = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                data.putSerializable("connectFilterData", filterData);
                navigator.navigateActivity(ConnectHomeActivity.class, data);

            }
        };
        profilePicPlaceHolder = profilePicResArray[CommonUtils.randInt(0, profilePicResArray.length)];
        showLoadingItem = new ObservableBoolean(true);
        showNoPost = new ObservableBoolean(false);

        apiService.getConnectFeed(filterData, 0).onErrorReturn(new Function<Throwable, ConnectFeedResp>() {
            @Override
            public ConnectFeedResp apply(@io.reactivex.annotations.NonNull Throwable throwable) throws Exception {
                return new ConnectFeedResp(0, new ArrayList<ConnectFeedResp.Snippet>());
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ConnectFeedResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ConnectFeedResp resp) throws Exception {
                showLoadingItem.set(false);
                if (!resp.getData().isEmpty()) {
                    ConnectFeedResp.Snippet data = resp.getData().get(0);
                    data.setVideo(getVideoId(data.getVideo()));
                    vendorImage.set(defaultProfilePic.equalsIgnoreCase(data.getVendorImage()) ? "" : data.getVendorImage());
                    vendorName.set(data.getVendorName());
                    title.set(data.getTitle());
                    vendorCollege.set(data.getInstituteName());
                    date.set(getHumanDate(data.getDate()));
                    description.set(data.getDescription());
                    numLikes.set(data.getNumLikes());
                    numComments.set(data.getNumComments());
                    numAccepts.set(data.getNumAccepted() + "");
                    videoThumb.set(!TextUtils.isEmpty(data.getImage()) ? data.getImage() : TextUtils.isEmpty(data.getVideo()) ? null : "http://img.youtube.com/vi/" + data.getVideo() + "/hqdefault.jpg");


                } else showNoPost.set(true);
            }
        });
    }

    private Observable<List<ViewModel>> getLoadingItems() {
        int count;
        if (nonReactiveItems.isEmpty())
            count = 4;
        else
            count = 2;
        List<ViewModel> result = new ArrayList<>();
        result.addAll(nonReactiveItems);
        result.addAll(Collections.nCopies(count, new RowShimmerItemViewModel()));
        return Observable.just(result);
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

    private String getVideoId(String videoUrl) {
        if (videoUrl == null) return null;
        try {
            return videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
        } catch (IndexOutOfBoundsException iobe) {
            return null;
        }
    }
}

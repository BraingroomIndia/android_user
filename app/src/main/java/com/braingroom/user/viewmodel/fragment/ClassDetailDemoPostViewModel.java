package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.braingroom.user.model.dto.ConnectFilterData;
import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ConnectHomeActivity;
import com.braingroom.user.viewmodel.ViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by godara on 24/07/17.
 */

public class ClassDetailDemoPostViewModel extends ViewModel {
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

    public final Action openConnect;



    public ClassDetailDemoPostViewModel(final Navigator navigator, final ConnectFilterData filterData) {
        openConnect = new Action() {
            @Override
            public void run() throws Exception {
                Bundle data = new Bundle();
                data.putSerializable("connectFilterData",filterData);
                navigator.navigateActivity(ConnectHomeActivity.class, data);

            }
        };

        apiService.getConnectFeed(filterData,0).subscribe(new Consumer<ConnectFeedResp>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull ConnectFeedResp resp) throws Exception {
                if (!resp.getData().isEmpty()){
                    ConnectFeedResp.Snippet data = resp.getData().get(0);
                    data.setVideo(getVideoId(data.getVideo()));
                    vendorImage.set(data.getVendorImage());
                    vendorName.set(data.getVendorName());
                    vendorCollege.set(data.getInstituteName());
                    date.set(getHumanDate(data.getDate()));
                    description.set(data.getDescription());
                    numLikes.set(data.getNumLikes());
                    numComments.set(data.getNumComments());
                    numAccepts.set(data.getNumAccepted()+"");
                    videoThumb.set(!TextUtils.isEmpty(data.getImage()) ? data.getImage() : TextUtils.isEmpty(data.getVideo()) ? null : "http://img.youtube.com/vi/" + data.getVideo() + "/hqdefault.jpg");



                }
            }
        });
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

package com.braingroom.user.viewmodel.fragment;

import android.databinding.ObservableField;
import android.text.Spanned;

import com.braingroom.user.model.response.ConnectFeedResp;
import com.braingroom.user.utils.CommonUtils;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.ViewModel;

import io.reactivex.functions.Action;

/**
 * Created by godara on 14/09/17.
 */


public class FeaturedPostItemViewModel extends ViewModel {
    private final ConnectFeedResp.Snippet data;
    //    public final MessageHelper messageHelper;
    public final Navigator navigator;
//    public final HelperFactory helperFactory;

    public final String userImage;
    public final String userName;
    public final String userCollegeName;
    public final String postTitle;
    public final String postImage;
    public final String postVideo;
    public final Spanned postDescription;
    public final Action playVideo;
    public final String minimumNumberOfLine;


    public FeaturedPostItemViewModel(ConnectFeedResp.Snippet snippet, final Navigator navigator) {
        this.data = snippet;
//        this.messageHelper = messageHelper;
        this.navigator = navigator;
//        this.helperFactory = helperFactory;
        this.userImage = data.getVendorImage();
        this.userName = data.getVendorName();
        this.userCollegeName = data.getInstituteName();
        this.postTitle = data.getTitle();
        this.postDescription = CommonUtils.fromHtml(data.getDescription());
        this.postVideo = isEmpty(data.getVideo()) ? null : data.getVideo();
        this.postImage = isEmpty(data.getImage()) ? (isEmpty(data.getVideo()) ? null : "http://img.youtube.com/vi/" + data.getVideo() + "/hqdefault.jpg") : data.getImage();
        if (isEmpty(postImage)) {
            minimumNumberOfLine = "4";
        } else minimumNumberOfLine = "2";

        playVideo = new Action() {
            @Override
            public void run() throws Exception {
                navigator.openStandaloneYoutube(postVideo);
            }
        };

    }
}

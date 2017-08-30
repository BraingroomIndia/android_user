package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;

import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import io.reactivex.functions.Action;

public class ProfileHeaderViewModel extends ViewModel {

    public final FollowButtonViewModel followButton;
    public final ObservableField<String> numFollowers = new ObservableField<>("...");
    public final ObservableField<String> numFollowing = new ObservableField<>("...");
    public final ObservableField<String> numPosts = new ObservableField<>("...");
    public final ObservableField<String> profileUrl = new ObservableField<>();
    public final Action followersClicked, followingClicked, postsClicked;


    public ProfileHeaderViewModel(final HelperFactory helperFactory, final MessageHelper messageHelper, final Navigator navigator
            , boolean isThirdParty, String numFollowers, String numFollowing, String numPosts, String profileUrl) {
        this.numPosts.set(numPosts);
        this.numFollowers.set(numFollowers);
        this.numFollowing.set(numFollowing);
        this.profileUrl.set(profileUrl);

        if (!isThirdParty) {
//            TODO hardcode user Id
            followButton = new FollowButtonViewModel(123 + "", messageHelper, navigator, FollowButtonViewModel.STATE_EDIT);
        } else {
            //            TODO hardcode user Id
            followButton = new FollowButtonViewModel(123 + "", messageHelper, navigator, FollowButtonViewModel.STATE_LOADING);
        }

        followersClicked = new Action() {
            @Override
            public void run() throws Exception {

            }
        };
        followingClicked = new Action() {
            @Override
            public void run() throws Exception {

            }
        };
        postsClicked = new Action() {
            @Override
            public void run() throws Exception {

            }
        };
    }

}




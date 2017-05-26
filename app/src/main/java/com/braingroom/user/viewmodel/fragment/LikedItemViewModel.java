package com.braingroom.user.viewmodel.fragment;

import com.braingroom.user.viewmodel.ViewModel;

public class LikedItemViewModel extends ViewModel {

    public final String userImage, userName;

    public LikedItemViewModel(String userImage, String userName) {

        this.userImage = userImage;
        this.userName = userName;
    }

}

package com.braingroom.user.viewmodel.fragment;

import android.support.annotation.NonNull;
import android.text.Spanned;

import com.braingroom.user.model.response.WinnerResp;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.viewmodel.ViewModel;

/**
 * Created by godara on 14/09/17.
 */

public class WinnersViewModel extends ViewModel {
    public final String userImage;
    public final String userName;
    public final String userCollegeName;
    public final Spanned prizeRank;
    public final String prizeText;

    public WinnersViewModel(@NonNull final WinnerResp.Snippet data, @NonNull final Navigator navigator) {

        this.userName = data.getUserName();
        this.userImage = data.getUserImage();
        this.userCollegeName = data.getUserCollege();
        this.prizeRank = data.getPrizeRank();
        this.prizeText = data.getPrizeText();
    }
}

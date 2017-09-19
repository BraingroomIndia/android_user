package com.braingroom.user.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

import com.braingroom.user.BR;
import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.braingroom.user.model.response.FollowResp;
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;
import com.braingroom.user.view.activity.ProfileActivity;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class FollowButtonViewModel extends ViewModel {

    public static final int STATE_LOADING = 0;
    public static final int STATE_FOLLOW = 1;
    public static final int STATE_FOLLOWED = 2;
    public static final int STATE_EDIT = 3;
    public static final int STATE_HIDDEN = 4;
    public final ObservableField<String> textVal = new ObservableField<>();
    private int backgroundDrawable = R.drawable.solid_gray_rounded_corner_chat;
    public final ObservableInt currentState = new ObservableInt();
    public final Action clickAction;

    @Bindable
    public Drawable getBackgroundDrawable() {
        return ContextCompat.getDrawable(UserApplication.getInstance(), backgroundDrawable);
    }

    public void setBackgroundDrawable(@DrawableRes int backgroundDrawable) {
        this.backgroundDrawable = backgroundDrawable;
        notifyPropertyChanged(BR.backgroundDrawable);
    }

    public FollowButtonViewModel(@NonNull final String userId, final MessageHelper messageHelper, final Navigator navigator, int state) {
        changeButtonState(state);
        clickAction = new Action() {
            @Override
            public void run() throws Exception {
                if (!getLoggedIn()) {
                    messageHelper.showLoginRequireDialog("Only logged in users can follow", null);
                    return;
                }
                switch (currentState.get()) {
                    case STATE_LOADING:
                        messageHelper.show("loading");
                        break;
                    case STATE_FOLLOW:
                        changeButtonState(STATE_LOADING);
                        apiService.follow(userId).subscribe(new Consumer<FollowResp>() {
                            @Override
                            public void accept(@NonNull FollowResp resp) throws Exception {
                                if (resp.getData().isEmpty()) {
                                    messageHelper.show(resp.getResMsg());
                                    changeButtonState(STATE_FOLLOW);
                                } else changeButtonState(STATE_FOLLOWED);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                changeButtonState(STATE_FOLLOW);
                            }
                        });
                        break;
                    case STATE_FOLLOWED:
//                        messageHelper.show("followed");
                        break;
                    case STATE_EDIT:
//                        messageHelper.show("edit");
                        navigator.navigateActivity(ProfileActivity.class, null);
                        break;
                }

            }
        };
    }

    public void changeButtonState(int state) {
        currentState.set(state);
        switch (currentState.get()) {
            case STATE_LOADING:
                setBackgroundDrawable(R.drawable.solid_blue_rounded_corner_follow);
                textVal.set("...");
                break;
            case STATE_FOLLOW:
                setBackgroundDrawable(R.drawable.solid_blue_rounded_corner_follow);
                textVal.set("FOLLOW");
                break;
            case STATE_FOLLOWED:
                setBackgroundDrawable(R.drawable.solid_blue_rounded_corner_follow);
                textVal.set("FOLLOWED");
                break;
            case STATE_EDIT:
                setBackgroundDrawable(R.drawable.solid_blue_rounded_corner_follow);
                textVal.set("EDIT");
                break;
        }

    }

}




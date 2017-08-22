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
import com.braingroom.user.utils.HelperFactory;
import com.braingroom.user.view.MessageHelper;
import com.braingroom.user.view.Navigator;

import io.reactivex.functions.Action;

public class FollowButtonViewModel extends ViewModel {

    public static final int STATE_LOADING = 0;
    public static final int STATE_FOLLOW = 1;
    public static final int STATE_FOLLOWED = 2;
    public static final int STATE_EDIT = 3;
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

    public FollowButtonViewModel(final HelperFactory helperFactory, final MessageHelper messageHelper, final Navigator navigator, int state) {
        changeButtonState(state);
        clickAction = new Action() {
            @Override
            public void run() throws Exception {
                switch (currentState.get()) {
                    case STATE_LOADING:
                        messageHelper.show("loading");
                        break;
                    case STATE_FOLLOW:
                        messageHelper.show("follow");
                        break;
                    case STATE_FOLLOWED:
                        messageHelper.show("followed");
                        break;
                    case STATE_EDIT:
                        messageHelper.show("edit");
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
                textVal.set("Loading...");
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




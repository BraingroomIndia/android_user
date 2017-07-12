package com.braingroom.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.View;

public interface Navigator {


    void navigateActivity(Class<?> destination, @Nullable Bundle bundle);

    void navigateActivityForResult(Class<?> destination, @Nullable Bundle bundle, int reqCode);

    void navigateActivity(Intent intent);

    void finishActivity();

    void launchImageChooserActivity(int reqCode);

    void launchVideoChooserActivity(int reqCode);

    void launchPlaceSearchIntent(int reqCode);

    void finishActivity(Intent resultData);

    void openStandaloneYoutube(String videoId);

    void openStandaloneVideo(String videoUrl);

    void hideKeyBoard(View view);

    void showMenuPopup(@MenuRes int layout, View v, PopupMenu.OnMenuItemClickListener clickListner);
}

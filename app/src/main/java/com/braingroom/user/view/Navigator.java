package com.braingroom.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

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
}

package com.braingroom.user.utils;

import android.support.v7.app.AppCompatActivity;

import com.braingroom.user.FCMInstanceIdService;
import com.braingroom.user.view.activity.Index;
import com.braingroom.user.view.activity.MvvmActivity;
import com.braingroom.user.view.activity.QRCodeReaderActivity;
import com.braingroom.user.view.activity.Splash;
import com.braingroom.user.viewmodel.ViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(ViewModel baseViewmodel);

    void inject(FCMInstanceIdService fcmInstanceIdService);

    void inject(MvvmActivity baseActivity);

    void inject(Splash activity);

    void inject(Index activity);

    void inject(QRCodeReaderActivity activity);

}

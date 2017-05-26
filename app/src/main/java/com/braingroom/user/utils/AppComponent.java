package com.braingroom.user.utils;

import com.braingroom.user.FCMInstanceIdService;
import com.braingroom.user.view.activity.BaseActivity;
import com.braingroom.user.viewmodel.ViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(ViewModel baseViewmodel);
    void inject(FCMInstanceIdService fcmInstanceIdService);
    void inject(BaseActivity baseActivity);

}

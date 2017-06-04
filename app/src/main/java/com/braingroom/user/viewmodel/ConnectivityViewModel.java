package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;
import com.github.pwittchen.reactivenetwork.library.rx2.Connectivity;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ConnectivityViewModel extends ViewModel {

    public static final int INTERNET_CONNECTIVITY_PLACEHOLDER = R.drawable.avatar_female;
    public ObservableField<Boolean> isConnected = new ObservableField<>(true);
    public final Action retryAction;

    public ConnectivityViewModel(@NonNull final Action retryAction) {
        this.retryAction = retryAction;
        ReactiveNetwork.observeNetworkConnectivity(UserApplication.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Connectivity>() {
                    @Override
                    public void accept(final Connectivity connectivity) {
                        isConnected.set(connectivity.isAvailable());
                        Log.d("Connectivity ", "accept: " + connectivity.isAvailable());
                    }
                });
    }


}

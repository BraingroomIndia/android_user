package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ConnectivityViewModel extends ViewModel {

    public static final int INTERNET_CONNECTIVITY_PLACEHOLDER = R.drawable.no_network;
    public ObservableBoolean isConnected = new ObservableBoolean(true);
    public final Action retryAction;
    private Disposable internetDisposable;

    public ConnectivityViewModel(@NonNull final Action retryAction) {
        this.retryAction = retryAction;

    }

    @Override
    public void onResume() {
        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) {
                        if (!isConnected.get() && isConnectedToInternet) {
                            try {
                                retryAction.run();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        isConnected.set(isConnectedToInternet);

                        Log.d("Connectivity", "accept: " + isConnectedToInternet);
                    }
                });
    }

    @Override
    public void onPause() {
        safelyDispose(internetDisposable);
    }

    private void safelyDispose(Disposable... disposables) {
        for (Disposable subscription : disposables) {
            if (subscription != null && !subscription.isDisposed()) {
                subscription.dispose();
            }
        }
    }
}

package com.braingroom.user.viewmodel;

import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.util.Log;

import com.braingroom.user.R;
import com.braingroom.user.UserApplication;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

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
        internetDisposable = UserApplication.getInstance().getInternetStatusBus().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean isConnectedToInternet) {
                if (!isConnectedToInternet) {
                    try {
                        isConnected.set(false);
                    } catch (Exception e) {
                        //e.printStackTrace();
                    }
                }

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

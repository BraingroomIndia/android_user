/*
package com.braingroom.user.model.response;

//Created by godara on 12/05/17.



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;

import static android.net.NetworkInfo.State;



public class InternetConnection {
    private Context context;
    private BroadcastReceiver broadcastReceiver;
    private PublishSubject<Boolean> internetStatusHotObservable;
    private final int maxRetryLimit = 5;
    private int delayBetweenRetry = 100;
    private int currentRepeatCount = 1;

    public InternetConnection(Context context) {
        this.context = context;
    }

    public Observable<Boolean> isInternetOnObservable() {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return isInternetOn();
            }
        });
    }

    public boolean isInternetOn() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Observable<Boolean> getInternetStatusHotObservable() {
        internetStatusHotObservable = PublishSubject.create();
        return internetStatusHotObservable.hide().serialize();
    }

// Register for Internet connection change broadcast receiver

    public void registerBroadCastReceiver() {
        final IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle extras = intent.getExtras();
                NetworkInfo info = extras.getParcelable("networkInfo");
                assert info != null;
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    checkForWorkingInternetConnection();
                } else {
                    internetStatusHotObservable.onNext(isInternetOn());
                }
            }
        };

        context.registerReceiver(broadcastReceiver, filter);
    }

 */
/*It takes a few milliseconds, from the connection is on
      to we get an active working internet connection.
*//*

    private void checkForWorkingInternetConnection() {
        currentRepeatCount = 1;
        delayBetweenRetry = 100;

        isInternetOnObservable().retryWhen(new Function<Observable<Boolean>, Boolean>() {
            @Override
            public Boolean apply(@NonNull Observable<Boolean> booleanObservable) throws Exception {
                return booleanObservable.flatMap(new Function<Boolean, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(@NonNull Boolean aVoid) throws Exception {
                        if (currentRepeatCount >= maxRetryLimit) {
                            return Observable.empty();
                        }
                        currentRepeatCount++;
                        delayBetweenRetry += 300;

                        return Observable.timer(delayBetweenRetry, TimeUnit.MILLISECONDS);
                    }
                })
.filter(new Predicate<Object>() {
    @Override
    public boolean test(@NonNull Object aBoolean) throws Exception {
        return (aBoolean==true);
    }
}).subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(@NonNull Boolean internetConnection) throws Exception {
                                currentRepeatCount = maxRetryLimit;
                                internetStatusHotObservable.onNext(isInternetOn());
                            }
                        });

    }

 //unRegister for Internet connection change broadcast receiver

    public void unRegisterBroadCastReceiver() {
        context.unregisterReceiver(broadcastReceiver);
    }
}*/

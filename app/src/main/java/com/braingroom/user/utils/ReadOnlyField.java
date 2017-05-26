package com.braingroom.user.utils;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


public class ReadOnlyField<T> extends ObservableField<T> {
    private final Observable<T> source;
    private final HashMap<OnPropertyChangedCallback, Disposable> subscriptions = new HashMap<>();

    public static <U> ReadOnlyField<U> create(@NonNull Observable<U> source) {
        return new ReadOnlyField<>(source);
    }

    private ReadOnlyField(@NonNull Observable<T> source) {
        super();
        this.source = source.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                ReadOnlyField.super.set(t);
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.e("ReadOnlyField", "onError in source observable", throwable);
            }
        }).onErrorResumeNext(Observable.<T>empty()).share();
    }

    /**
     * @deprecated Setter of ReadOnlyField does nothing. Merge with the source Observable instead.
     * See <a href="https://github.com/manas-chaudhari/android-mvvm/tree/master/Documentation/ObservablesAndSetters.md">Documentation/ObservablesAndSetters.md</a>
     */
    @Deprecated
    @Override
    public void set(T value) {
    }

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.addOnPropertyChangedCallback(callback);
        subscriptions.put(callback, source.subscribe());
    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.removeOnPropertyChangedCallback(callback);
        Disposable subscription = subscriptions.remove(callback);
        if (subscription != null && !subscription.isDisposed()) {
            subscription.dispose();
        }
    }
}

package com.braingroom.user.utils;

import io.reactivex.annotations.NonNull;

/**
 * Created by agrahari on 12/04/17.
 */

public interface MyConsumer<T> {
    void accept(@NonNull T var1);
}

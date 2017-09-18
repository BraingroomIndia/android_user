/*
 * Copyright 2016 Manas Chaudhari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.braingroom.user.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.afollestad.materialdialogs.MaterialDialog;

public interface MessageHelper {


    void show(String message);

    void showLoginRequireDialog(String message, Bundle data);

    void showDismissInfo(@Nullable String title, @NonNull String content);

    void showDismissInfo(@Nullable String title,@NonNull String buttonText , @NonNull String content);

    void showAcceptableInfo(@Nullable String title, @NonNull String content, @NonNull MaterialDialog.SingleButtonCallback positiveCallback);

    void showAcceptableInfo(@Nullable String title, @NonNull String content, String postiveText, @NonNull MaterialDialog.SingleButtonCallback positiveCallback);

    void showAcceptDismissInfo(/*@Nullable String title,@NonNull String content,@NonNull String positiveText,@NonNull String negativeText, @NonNull */);

    void showProgressDialog(@Nullable String title, @NonNull String content);

    void dismissActiveProgress();
}

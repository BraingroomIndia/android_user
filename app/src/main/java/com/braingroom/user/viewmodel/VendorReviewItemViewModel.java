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

package com.braingroom.user.viewmodel;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.braingroom.user.view.MessageHelper;

import io.reactivex.functions.Action;

public class VendorReviewItemViewModel extends ViewModel {

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> review = new ObservableField<>();
    public final ObservableField<String> rating = new ObservableField<>();
    public final Action onItemClicked;

    public VendorReviewItemViewModel(@NonNull final MessageHelper messageHelper
            , @NonNull final String name, @NonNull final String review, @NonNull String rating) {
        this.name.set(name);
        this.review.set(review);
        this.rating.set(rating);

        onItemClicked = new Action() {
            @Override
            public void run() throws Exception {
                messageHelper.showDismissInfo(name + "'s" + " Review", review);
            }
        };
    }
}

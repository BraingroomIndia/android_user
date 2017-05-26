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

import android.support.annotation.NonNull;

public class SearchResultItem {

    @NonNull
    public final String query;

    @NonNull
    public final String categoryId;

    @NonNull
    public final String category;

    public SearchResultItem(@NonNull String query, @NonNull String categoryId, @NonNull String category) {
        this.query = query;
        this.categoryId = categoryId;
        this.category = category;
    }
}

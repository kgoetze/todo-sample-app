/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rab.com.sae_sample_app.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.UUID;

/**
 * Immutable model class for a Task.
 */
public final class Task {

    @NonNull
    private String mId;

    @Nullable
    private String mTitle;

    private boolean mCompleted;

    public Task(@Nullable String title) {
        mId = UUID.randomUUID().toString();
        mTitle = title;
        mCompleted = false;
    }

    public Task(@Nullable String title, @NonNull String id, boolean completed) {
        mId = id;
        mTitle = title;
        mCompleted = completed;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    public boolean isCompleted() {
        return mCompleted;
    }

    public void setCompleted(boolean value) {
        mCompleted = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (mCompleted != task.mCompleted) return false;
        if (!mId.equals(task.mId)) return false;
        return mTitle != null ? mTitle.equals(task.mTitle) : task.mTitle == null;

    }

    @Override
    public int hashCode() {
        int result = mId.hashCode();
        result = 31 * result + (mTitle != null ? mTitle.hashCode() : 0);
        result = 31 * result + (mCompleted ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Task with title " + mTitle;
    }
}

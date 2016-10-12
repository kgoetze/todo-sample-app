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

package rab.com.sae_sample_app.tasks;

import android.support.annotation.NonNull;

import java.util.List;

import rab.com.sae_sample_app.model.Task;
import rab.com.sae_sample_app.model.datasource.TasksDataSource;

/**
 * Listens to user actions from the UI, retrieves the data and updates the
 * UI as required.
 */
public class TasksPresenter implements TasksContract.Presenter {

    private final TasksDataSource mDataSource;

    private final TasksContract.View mTasksView;

    public TasksPresenter(@NonNull TasksDataSource dataSource, @NonNull TasksContract.View tasksView) {
        if (dataSource == null) {
            throw new NullPointerException("dataSource cannot be null");
        }
        if (tasksView == null) {
            throw new NullPointerException("tasksView cannot be null");
        }
        mDataSource = dataSource;
        mTasksView = tasksView;
    }

    @Override
    public void loadTasks() {
        List<Task> tasks = mDataSource.getTasks();

        if (tasks != null && !tasks.isEmpty()) {
            // The view may not be able to handle UI updates anymore
            if (!mTasksView.isActive()) {
                return;
            }

            if (tasks.isEmpty()) {
                // Show a message indicating there are no tasks
                mTasksView.showNoTasks();
            } else {
                // Show the list of tasks
                mTasksView.showTasks(tasks);
            }
        } else {
            // The view may not be able to handle UI updates anymore
            if (!mTasksView.isActive()) {
                return;
            }
            mTasksView.showNoTasks();
        }
    }

    @Override
    public void addNewTask(String title) {
        if (title != null) {
            Task task = new Task(title);
            mDataSource.saveTask(task);
            loadTasks();
        }
    }

    @Override
    public void deleteTask(Task task) {
        if (task != null) {
            mDataSource.deleteTask(task.getId());
            loadTasks();
        }
    }

    @Override
    public void completeTask(Task task) {
        if (task != null) {
            mDataSource.completeTask(task);
            loadTasks();
        }
    }

    @Override
    public void activateTask(Task task) {
        if (task != null) {
            mDataSource.activateTask(task);
            loadTasks();
        }
    }

    @Override
    public void clearTasks() {
        mDataSource.deleteAllTasks();
        loadTasks();
    }

    @Override
    public void clearCompletedTasks() {
        mDataSource.clearCompletedTasks();
        loadTasks();
    }

}

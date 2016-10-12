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

package rab.com.sae_sample_app.model.datasource;

import java.util.ArrayList;
import java.util.List;

import rab.com.sae_sample_app.model.Task;

/**
 * Concrete implementation of a data source as a db.
 */
public class InMemoryDataSource implements TasksDataSource {

    private static InMemoryDataSource instance;

    List<Task> tasks = new ArrayList<Task>();

    // Prevent direct instantiation.
    private InMemoryDataSource() {
    }

    public static InMemoryDataSource getInstance() {
        if (instance == null) {
            instance = new InMemoryDataSource();
        }
        return instance;
    }

    @Override
    public List<Task> getTasks() {
        return tasks;

    }

    @Override
    public Task getTask(String taskId) {
        Task task = null;

        if (taskId != null) {
            if (tasks != null) {
                for (Task t : tasks) {
                    if (t.getId().equals(taskId)) {
                        task = t;
                        break;
                    }
                }
            }
        }

        return task;
    }

    @Override
    public void saveTask(Task task) {
        if (task != null) {
            if (!tasks.contains(task)) {
                tasks.add(task);
            }
        }
    }

    @Override
    public void completeTask(Task task) {
        if (task != null) {
            task.setCompleted(true);
        }
    }

    @Override
    public void activateTask(Task task) {
        if (task != null) {
            task.setCompleted(false);
        }
    }

    @Override
    public void clearCompletedTasks() {
        List<Task> incompleteTasks = new ArrayList<>();
        for (Task t : tasks) {
            if (!t.isCompleted()) {
                incompleteTasks.add(t);
            }
        }
        tasks = incompleteTasks;
    }

    @Override
    public void deleteAllTasks() {
        tasks = new ArrayList<>();
    }

    @Override
    public void deleteTask(String taskId) {
        if (taskId != null) {
            tasks.remove(getTask(taskId));
        }
    }
}

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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rab.com.sae_sample_app.model.Task;

/**
 * Concrete implementation of a data source as a db.
 */
public class DBDataSource implements TasksDataSource {

    private static DBDataSource instance;

    private TasksDbHelper mDbHelper;

    // Prevent direct instantiation.
    private DBDataSource(@NonNull Context context) {
        mDbHelper = new TasksDbHelper(context);
    }

    public static DBDataSource getInstance(@NonNull Context context) {
        if (instance == null) {
            instance = new DBDataSource(context);
        }
        return instance;
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_COMPLETED
        };

        // get tasks from DB
        Cursor c = db.query(TaskEntry.TABLE_NAME, projection, null, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                boolean completed =
                        c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                Task task = new Task(title, itemId, completed);
                tasks.add(task);
            }
        }
        if (c != null) {
            c.close();
        }
        db.close();

        return tasks;

    }

    @Override
    public Task getTask(String taskId) {
        Task task = null;

        if (taskId != null) {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            String[] projection = {
                    TaskEntry.COLUMN_NAME_ENTRY_ID,
                    TaskEntry.COLUMN_NAME_TITLE,
                    TaskEntry.COLUMN_NAME_COMPLETED
            };
            String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {taskId};

            // get tasks from DB
            Cursor c = db.query(TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                String itemId = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_ENTRY_ID));
                String title = c.getString(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_TITLE));
                boolean completed =
                        c.getInt(c.getColumnIndexOrThrow(TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                task = new Task(title, itemId, completed);
            }
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return task;
    }

    @Override
    public void saveTask(Task task) {
        if (task != null) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_ENTRY_ID, task.getId());
            values.put(TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());

            db.insert(TaskEntry.TABLE_NAME, null, values);
            db.close();
        }
    }

    @Override
    public void completeTask(Task task) {
        if (task != null) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, true);

            String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {task.getId()};

            db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
            db.close();
        }
    }

    @Override
    public void activateTask(Task task) {
        if (task != null) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TaskEntry.COLUMN_NAME_COMPLETED, false);

            String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {task.getId()};

            db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
            db.close();
        }
    }

    @Override
    public void clearCompletedTasks() {
    }

    @Override
    public void deleteAllTasks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(TaskEntry.TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void deleteTask(String taskId) {
        if (taskId != null) {
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " LIKE ?";
            String[] selectionArgs = {taskId};

            db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);
            db.close();
        }
    }

    /* Inner class that defines the table contents */
    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_COMPLETED = "completed";
    }

    public class TasksDbHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "Tasks.db";
        private static final String TEXT_TYPE = " TEXT";
        private static final String BOOLEAN_TYPE = " INTEGER";
        private static final String COMMA_SEP = ",";

        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + TaskEntry.TABLE_NAME + " (" +
                        TaskEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                        TaskEntry.COLUMN_NAME_ENTRY_ID + TEXT_TYPE + COMMA_SEP +
                        TaskEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                        TaskEntry.COLUMN_NAME_COMPLETED + BOOLEAN_TYPE +
                        " )";

        public TasksDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Not required as at version 1
        }
    }
}

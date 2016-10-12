package rab.com.sae_sample_app.tasks;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rab.com.sae_sample_app.R;
import rab.com.sae_sample_app.model.Task;
import rab.com.sae_sample_app.model.datasource.DBDataSource;

public class TaskActivity extends AppCompatActivity implements TasksContract.View {

    @BindView(R.id.taskView_root)
    View mRootView;
    @BindView(R.id.taskView_taskList)
    ListView mTasksListView;
    @BindView(R.id.taskView_noTasks)
    View mNoTasksView;
    private TasksContract.Presenter mPresenter;
    private boolean mIsActive = false;
    private TaskListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        // Set up tasks view
        mListAdapter = new TaskListAdapter(new ArrayList<Task>(0), new TaskListAdapter.TaskItemListener() {
            @Override
            public void onCompleteTaskClick(Task completedTask) {
                mPresenter.completeTask(completedTask);
            }

            @Override
            public void onActivateTaskClick(Task activatedTask) {
                mPresenter.activateTask(activatedTask);
            }

            @Override
            public void onDeleteTaskClick(final Task task) {
                new AlertDialog.Builder(TaskActivity.this)
                        .setTitle("Delete")
                        .setMessage("Do you really want to delete this task?")
                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.deleteTask(task);
                            }
                        })
                        .setNegativeButton(R.string.button_cancel, null)
                        .show();
            }
        });
        mTasksListView.setAdapter(mListAdapter);

        // Set up floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTask();
            }
        });

        // init presenter and load tasks
        mIsActive = true;
        mPresenter = new TasksPresenter(DBDataSource.getInstance(getBaseContext()), this);
        mPresenter.loadTasks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();
        if (id == R.id.action_clear) {
            mPresenter.clearTasks();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIsActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mIsActive = false;
    }

    @Override
    public boolean isActive() {
        return mIsActive;
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mListAdapter.setData(tasks);

        mTasksListView.setVisibility(View.VISIBLE);
        mNoTasksView.setVisibility(View.GONE);
    }

    public void showAddTask() {
        // show dialog
        final TaskAddDialog dialog = new TaskAddDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTaskAddListener(new TaskAddDialog.TaskAddListener() {
            @Override
            public void onAdd(String title) {
                dialog.dismiss();
                mPresenter.addNewTask(title);
            }

            @Override
            public void onCancel() {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void showNoTasks() {
        mTasksListView.setVisibility(View.GONE);
        mNoTasksView.setVisibility(View.VISIBLE);
    }
}

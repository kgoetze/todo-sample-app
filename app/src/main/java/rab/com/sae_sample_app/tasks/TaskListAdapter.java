package rab.com.sae_sample_app.tasks;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import rab.com.sae_sample_app.R;
import rab.com.sae_sample_app.model.Task;

/**
 * Created by kgoetze on 19/09/16.
 */
public class TaskListAdapter extends BaseAdapter {

    private List<Task> mTasks;
    private TaskItemListener mItemListener;
    public TaskListAdapter(List<Task> tasks, @NonNull TaskItemListener itemListener) {
        setList(tasks);
        mItemListener = itemListener;
    }

    public void setData(List<Task> tasks) {
        setList(tasks);
        notifyDataSetChanged();
    }

    private void setList(List<Task> tasks) {
        mTasks = tasks;
    }

    @Override
    public int getCount() {
        if (mTasks != null) {
            return mTasks.size();
        }
        return 0;
    }

    @Override
    public Task getItem(int i) {
        if (mTasks != null) {
            return mTasks.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            // inflate the layout
            convertView = ViewHolder.inflate(parent);

            // set up the ViewHolder
            viewHolder = new ViewHolder(convertView);

            // store the holder with the view.
            convertView.setTag(viewHolder);
        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Task task = getItem(position);
        viewHolder.setTask(task);
        viewHolder.setOnCheckBoxListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!task.isCompleted()) {
                    mItemListener.onCompleteTaskClick(task);
                } else {
                    mItemListener.onActivateTaskClick(task);
                }
            }
        });
        viewHolder.setOnDeleteListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemListener.onDeleteTaskClick(task);
            }
        });

        return convertView;
    }

    public interface TaskItemListener {
        void onCompleteTaskClick(Task task);

        void onActivateTaskClick(Task task);

        void onDeleteTaskClick(Task task);
    }

    static class ViewHolder {
        View rootView;
        TextView titleTextView;
        CheckBox completeCheckBox;
        View deleteBtn;

        public ViewHolder(View view) {
            rootView = view;
            titleTextView = (TextView) view.findViewById(R.id.title);
            completeCheckBox = (CheckBox) view.findViewById(R.id.complete);
            deleteBtn = view.findViewById(R.id.delete);
        }

        public static View inflate(ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            return inflater.inflate(R.layout.cell_task_item, parent, false);
        }

        public void setTask(Task task) {
            titleTextView.setText(task.getTitle());

            completeCheckBox.setChecked(task.isCompleted());
            if (task.isCompleted()) {
                rootView.setBackground(rootView.getContext()
                        .getResources().getDrawable(R.drawable.cell_bg_task_completed));
            } else {
                rootView.setBackground(rootView.getContext()
                        .getResources().getDrawable(R.drawable.cell_bg_task_open));
            }
        }

        public void setOnCheckBoxListener(View.OnClickListener listener) {
            completeCheckBox.setOnClickListener(listener);
        }

        public void setOnDeleteListener(View.OnClickListener listener) {
            deleteBtn.setOnClickListener(listener);
        }

    }
}
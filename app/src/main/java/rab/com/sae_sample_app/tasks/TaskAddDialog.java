package rab.com.sae_sample_app.tasks;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import rab.com.sae_sample_app.R;

/**
 * Created by kgoetze on 19/09/16.
 */
public class TaskAddDialog extends Dialog {

    @BindView(R.id.dialog_titleInput)
    EditText mTitleInput;

    @BindView(R.id.dialog_okBtn)
    Button mOkBtn;

    @BindView(R.id.dialog_cancelBtn)
    Button mCancelBtn;

    private TaskAddListener mTaskAddListener;

    public TaskAddDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        setContentView(R.layout.dialog_add_task);
        ButterKnife.bind(this);

        setTitle(getContext().getString(R.string.dialog_addTask_title));

        mOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTaskAddListener != null) {
                    mTaskAddListener.onAdd(mTitleInput.getText().toString());
                }
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTaskAddListener != null) {
                    mTaskAddListener.onCancel();
                }
            }
        });
    }

    public void setTaskAddListener(TaskAddListener listener) {
        this.mTaskAddListener = listener;
    }

    interface TaskAddListener {
        void onAdd(String title);

        void onCancel();
    }
}

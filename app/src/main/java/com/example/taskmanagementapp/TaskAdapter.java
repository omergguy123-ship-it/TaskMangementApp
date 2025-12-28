package com.example.taskmanagementapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagementapp.R;
import com.example.taskmanagementapp.Task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskVH> {

    private final List<Task> tasks;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskVH holder, int position) {
        Task t = tasks.get(position);

        holder.tvTitle.setText(t.title);
        holder.tvDesc.setText(t.description == null ? "" : t.description);

        if (t.dueMillis == -1L) {
            holder.tvDue.setText("Due: Not set");
        } else {
            holder.tvDue.setText("Due: " + sdf.format(new Date(t.dueMillis)));
        }

        holder.cbDone.setOnCheckedChangeListener(null);
        holder.cbDone.setChecked(t.isDone);
        holder.cbDone.setOnCheckedChangeListener((buttonView, isChecked) -> t.isDone = isChecked);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskVH extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvDue;
        CheckBox cbDone;

        public TaskVH(@NonNull View itemView) {
            super(itemView);
            cbDone = itemView.findViewById(R.id.cbDone);
            tvTitle = itemView.findViewById(R.id.tvTaskTitle);
            tvDesc = itemView.findViewById(R.id.tvTaskDesc);
            tvDue = itemView.findViewById(R.id.tvTaskDue);
        }
    }
}

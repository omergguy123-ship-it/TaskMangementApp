package com.example.taskmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {

    private final ArrayList<Task> tasks = new ArrayList<>();
    private TaskAdapter adapter;

    private TextView tvEmpty;

    private ActivityResultLauncher<Intent> addTaskLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        tvEmpty = findViewById(R.id.tvEmpty);

        RecyclerView rvTasks = findViewById(R.id.rvTasks);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(tasks);
        rvTasks.setAdapter(adapter);

        addTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String title = result.getData().getStringExtra("title");
                        String description = result.getData().getStringExtra("description");
                        long dueMillis = result.getData().getLongExtra("dueMillis", -1L);

                        tasks.add(0, new Task(title, description, dueMillis)); // add to top
                        adapter.notifyItemInserted(0);

                        updateEmptyState();
                    }
                }
        );

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });

        updateEmptyState();
    }

    private void updateEmptyState() {
        tvEmpty.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
    }
}

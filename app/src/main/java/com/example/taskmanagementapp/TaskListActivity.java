package com.example.taskmanagementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TaskListActivity extends AppCompatActivity {
    FloatingActionButton AddTaskButton1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_list);
        Intent TaskListTOAddTask = new Intent(TaskListActivity.this, AddTaskActivity.class);
        AddTaskButton1 = findViewById(R.id.fabAdd);
        AddTaskButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TaskListTOAddTask);
            }
        });
    }
}
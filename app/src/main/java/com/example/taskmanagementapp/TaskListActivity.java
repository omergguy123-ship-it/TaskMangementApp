package com.example.taskmanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Map;

import java.util.ArrayList;

public class TaskListActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference tasksRef;
    private final ArrayList<Task> tasks = new ArrayList<>();
    private TaskAdapter adapter;

    private TextView tvEmpty;

    private ActivityResultLauncher<Intent> addTaskLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        db = FirebaseFirestore.getInstance();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        tasksRef = db.collection("users").document(uid).collection("tasks");
        tvEmpty = findViewById(R.id.tvEmpty);

        RecyclerView rvTasks = findViewById(R.id.rvTasks);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskAdapter(tasks);
        rvTasks.setAdapter(adapter);
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(TaskListActivity.this, LoginActivity.class));
            finish();
        });

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


                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("title", title);
                        taskMap.put("description", description);
                        taskMap.put("dueMillis", dueMillis);
                        taskMap.put("isDone", false);
                        taskMap.put("createdAt", System.currentTimeMillis());

                        tasksRef.add(taskMap);

                    }
                }
        );

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(TaskListActivity.this, AddTaskActivity.class);
            addTaskLauncher.launch(intent);
        });

        updateEmptyState();
        listenForTasks();
    }

    private void updateEmptyState() {
        tvEmpty.setVisibility(tasks.isEmpty() ? View.VISIBLE : View.GONE);
    }
    private void listenForTasks() {
        tasksRef.orderBy("createdAt", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshot, e) -> {
                    if (e != null || snapshot == null) return;

                    tasks.clear();
                    for (DocumentSnapshot doc : snapshot.getDocuments()) {
                        String title = doc.getString("title");
                        String desc = doc.getString("description");
                        Long due = doc.getLong("dueMillis");
                        Boolean done = doc.getBoolean("isDone");

                        Task t = new Task(
                                title == null ? "" : title,
                                desc == null ? "" : desc,
                                due == null ? -1L : due
                        );
                        t.isDone = done != null && done;
                        t.docId = doc.getId();

                        tasks.add(t);
                    }

                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                });
    }
}

package com.example.taskmanagementapp;

public class Task {
    public String docId;       // Firestore document id
    public String title;
    public String description;
    public long dueMillis;     // -1 = not set
    public boolean isDone;

    public Task(String title, String description, long dueMillis) {
        this.title = title;
        this.description = description;
        this.dueMillis = dueMillis;
        this.isDone = false;
    }
}

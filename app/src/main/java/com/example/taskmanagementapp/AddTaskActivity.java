    package com.example.taskmanagementapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanagementapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddTaskActivity extends AppCompatActivity {

    private EditText etTitle, etDescription;
    private Button btnSave, btnCancel, btnPickDue;
    private TextView tvDueValue;

    // Store selected due date-time (nullable)
    private Long dueMillis = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        tvDueValue = findViewById(R.id.tvDueValue);
        btnPickDue = findViewById(R.id.btnPickDue);

        btnPickDue.setOnClickListener(v -> openDateThenTimePicker());

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String description = etDescription.getText().toString().trim();

            if (title.isEmpty()) {
                etTitle.setError("Title is required");
                etTitle.requestFocus();
                return;
            }

            Intent result = new Intent();
            result.putExtra("title", title);
            result.putExtra("description", description);

            // If not set, we send -1
            result.putExtra("dueMillis", dueMillis == null ? -1L : dueMillis);

            setResult(RESULT_OK, result);
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void openDateThenTimePicker() {
        Calendar now = Calendar.getInstance();

        DatePickerDialog dp = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    // After picking date, pick time
                    Calendar picked = Calendar.getInstance();
                    picked.set(Calendar.YEAR, year);
                    picked.set(Calendar.MONTH, month);
                    picked.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    int hour = now.get(Calendar.HOUR_OF_DAY);
                    int minute = now.get(Calendar.MINUTE);

                    TimePickerDialog tp = new TimePickerDialog(
                            this,
                            (timeView, pickedHour, pickedMinute) -> {
                                picked.set(Calendar.HOUR_OF_DAY, pickedHour);
                                picked.set(Calendar.MINUTE, pickedMinute);
                                picked.set(Calendar.SECOND, 0);
                                picked.set(Calendar.MILLISECOND, 0);

                                dueMillis = picked.getTimeInMillis();
                                tvDueValue.setText(formatMillis(dueMillis));
                            },
                            hour,
                            minute,
                            true // 24-hour format
                    );

                    tp.show();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        dp.show();
    }

    private String formatMillis(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(millis);
    }
}

package com.example.aula01;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etNome;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<String> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        etNome = findViewById(R.id.et_nome);
        recyclerView = findViewById(R.id.recycler_view);
        Button buttonAdd = findViewById(R.id.button_add);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList, this::deleteTask, this::editTask);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        loadTasks();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskText = etNome.getText().toString();
                if (!taskText.isEmpty()) {
                    addTask(taskText);
                    etNome.setText("");
                }
            }
        });
    }
    private void editTask(int position, String currentTask) {
        // Caixa de diálogo para edição
        final EditText editTaskInput = new EditText(this);
        editTaskInput.setText(currentTask);

        new AlertDialog.Builder(this)
                .setTitle("Editar Tarefa")
                .setView(editTaskInput)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String updatedTask = editTaskInput.getText().toString();
                    if (!updatedTask.isEmpty()) {
                        updateTaskInDatabase(taskList.get(position), updatedTask);
                        loadTasks();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void updateTaskInDatabase(String oldTask, String newTask) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TASK, newTask);
        db.update(DatabaseHelper.TABLE_TASKS, values, DatabaseHelper.COLUMN_TASK + "=?", new String[]{oldTask});
    }

    private void addTask(String task) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TASK, task);
        db.insert(DatabaseHelper.TABLE_TASKS, null, values);
        loadTasks();
    }

    private void loadTasks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TASKS, null, null, null, null, null, null);
        taskList.clear();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TASK);
                if (columnIndex >= 0) {
                    String task = cursor.getString(columnIndex);
                    taskList.add(task);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        taskAdapter.notifyDataSetChanged();
    }


    private void deleteTask(int position) {
        // Remove the task from the database
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String taskToDelete = taskList.get(position);
        db.delete(DatabaseHelper.TABLE_TASKS, DatabaseHelper.COLUMN_TASK + "=?", new String[]{taskToDelete});

        // Reload the tasks from the database
        loadTasks();
    }
}

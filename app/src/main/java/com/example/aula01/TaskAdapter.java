package com.example.aula01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private final List<String> taskList;
    private final OnDeleteClickListener onDeleteClickListener;
    private final OnEditClickListener onEditClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public interface OnEditClickListener {
        void onEditClick(int position, String currentTask);
    }

    public TaskAdapter(List<String> taskList, OnDeleteClickListener onDeleteClickListener, OnEditClickListener onEditClickListener) {
        this.taskList = taskList;
        this.onDeleteClickListener = onDeleteClickListener;
        this.onEditClickListener = onEditClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textTask.setText(taskList.get(position));
        holder.buttonDelete.setOnClickListener(v -> onDeleteClickListener.onDeleteClick(position));
        holder.buttonEdit.setOnClickListener(v -> onEditClickListener.onEditClick(position, taskList.get(position)));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView textTask;
        final Button buttonDelete;
        final Button buttonEdit;

        ViewHolder(View itemView) {
            super(itemView);
            textTask = itemView.findViewById(R.id.text_task);
            buttonDelete = itemView.findViewById(R.id.button_delete);
            buttonEdit = itemView.findViewById(R.id.button_edit);
        }
    }
}


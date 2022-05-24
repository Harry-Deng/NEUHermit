package com.sora.gcdr.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.sora.gcdr.databinding.CellTaskBinding;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.model.TaskViewModel;
import com.sora.gcdr.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private List<Task> dayTasks = new ArrayList<>();
    private final TaskViewModel taskViewModel;

    public TaskListAdapter(TaskViewModel taskViewModel) {
        this.taskViewModel = taskViewModel;
    }

    public void setDayTasks(List<Task> dayTasks) {
        this.dayTasks = dayTasks;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        CellTaskBinding binding;

        public TaskViewHolder(CellTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellTaskBinding binding = CellTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskViewModel.getDayTaskLive().getValue().get(position);

        if (task == null) {
            task = new Task(MyUtils.getCurrentTime(), "也许有bug", false);
        }

        holder.binding.textViewId.setText(String.valueOf(position + 1));
        holder.binding.textViewTime.setText(
                MyUtils.getTimeByLong(task.getDate()));
        holder.binding.textViewContent.setText(task.getContent());
        holder.binding.textViewDone.setText(String.valueOf(task.isDone()));
    }

    @Override
    public int getItemCount() {
        if (dayTasks == null)
            return 0;
        return dayTasks.size();
    }
}

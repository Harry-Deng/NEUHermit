package com.sora.gcdr.ui.home;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.sora.gcdr.R;
import com.sora.gcdr.databinding.CellTaskBinding;
import com.sora.gcdr.databinding.UpdateTaskBinding;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.util.MyUtils;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private final HomeViewModel homeViewModel;

    public TaskListAdapter(HomeViewModel homeViewModel) {
        this.homeViewModel = homeViewModel;
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
        List<Task> value = homeViewModel.getDayTaskLive().getValue();
        if (value == null) {
            return;
        }
        Task task = value.get(position);
        holder.binding.textViewId.setText(String.valueOf(position + 1));
        holder.binding.textViewTime.setText(
                MyUtils.getTimeByLong(task.getDate()));
        holder.binding.textViewContent.setText(task.getContent());

//        if (TextUtils.isEmpty(task.getContent()))
//            holder.binding.textViewContent.setVisibility(View.GONE);

        holder.binding.textViewDone.setText(String.valueOf(task.isDone()));

        //长按修改
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                UpdateTaskBinding binding = UpdateTaskBinding.inflate(LayoutInflater.from(v.getContext()));
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                binding.textViewDate.setText(MyUtils.getDateByLong(task.getDate()));
                binding.textViewTime.setText(MyUtils.getTimeByLong(task.getDate()));

                binding.editTextContent.setText(task.getContent());

                builder.setView(binding.getRoot())
                        .setPositiveButton(R.string.buttonOK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                task.setContent(binding.editTextContent.getText().toString());
                                homeViewModel.update(task);

                            }
                        })
                        .setNegativeButton(R.string.buttonCancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .create()
                        .show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        if (homeViewModel.getDayTaskLive().getValue() == null)
            return 0;
        return homeViewModel.getDayTaskLive().getValue().size();
    }

}

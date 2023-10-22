package com.sora.gcdr.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sora.gcdr.activity.SetAlarmActivity;
import com.sora.gcdr.databinding.CellTaskBinding;
import com.sora.gcdr.db.task.Task;
import com.sora.gcdr.util.MyUtils;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private Context mContext;
    private final HomeViewModel homeViewModel;

    public TaskListAdapter(HomeViewModel homeViewModel, Context context) {
        mContext = context;
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
//        holder.binding.textViewId.setText(String.valueOf(position + 1));
        holder.binding.textViewTime.setText(MyUtils.getTimeByLong(task.getDate()));

        holder.binding.textViewSlogan.setText(task.getSlogan());

//        holder.binding.textViewContent.setVisibility(TextUtils.isEmpty(task.getContent()) ? View.GONE : View.VISIBLE);

//        holder.binding.textViewDone.setText(String.valueOf(task.getStatus()));

        if (task.getStatus() == 2) {
            holder.binding.imageViewAlarm.setVisibility(View.VISIBLE);
        }else {
            holder.binding.imageViewAlarm.setVisibility(View.INVISIBLE);
        }

        holder.binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    task.setStatus(0);
                    holder.binding.taskCard.setCardBackgroundColor(Color.rgb(212, 203, 250));
                } else {
                    task.setStatus(1);
                    holder.binding.taskCard.setCardBackgroundColor(Color.rgb(255, 255, 255));
                }
            }
        });


        //长按调出添加提醒的对话框，不再提供修改日程内容的接口
        holder.itemView.setOnLongClickListener(v -> {
            Intent intent = new Intent(v.getContext(), SetAlarmActivity.class);
            intent.putExtra("task", task);
            v.getContext().startActivity(intent);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        if (homeViewModel.getDayTaskLive().getValue() == null)
            return 0;
        return homeViewModel.getDayTaskLive().getValue().size();
    }

}

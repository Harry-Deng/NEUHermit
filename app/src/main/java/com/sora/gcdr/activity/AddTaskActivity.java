package com.sora.gcdr.activity;

import static cn.leancloud.LeanCloud.getContext;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sora.gcdr.R;
import com.sora.gcdr.databinding.ActivityAddTaskBinding;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.util.MyUtils;

import java.util.Calendar;

/**
 * 添加待办的页面,使用一个Activity
 */
public class AddTaskActivity extends AppCompatActivity {

    ActivityAddTaskBinding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTaskBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        int curYear = intent.getIntExtra("year", Calendar.getInstance().get(Calendar.YEAR));
        int curMonth = intent.getIntExtra("month", Calendar.getInstance().get(Calendar.MONTH));
        int curDay = intent.getIntExtra("day", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        //初始化一些数据
        binding.textViewDate.setText((getString(R.string.year_month_day_d_d_d, curYear, curMonth, curDay)));
        binding.textViewTime.setText(MyUtils.getTimeByLong(System.currentTimeMillis()));
        //设置待办日期，弹出一个日期选择器
        binding.textViewDate.setOnClickListener(v -> new DatePickerDialog(this,
                (view1, year, month, dayOfMonth) -> binding.textViewDate.setText(getString(R.string.year_month_day_d_d_d, year, month + 1, dayOfMonth)),
                curYear, curMonth, curDay)
                .show());
        //设置待办时间，弹出一个时间选择器
        binding.textViewTime.setOnClickListener(v -> new TimePickerDialog(this, (view12, hourOfDay, minute) -> binding.textViewTime.setText(getString(R.string.hour_minute_d_d, hourOfDay, minute)),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true)
                .show());
        //点击提交按钮，把待办提交到数据库，导航回到日历页面，并清理返回栈
        binding.buttonOK.setOnClickListener(v -> {
            String datetime =
                    binding.textViewDate.getText().toString() + " " + binding.textViewTime.getText().toString();
            Task task = new Task(
                    MyUtils.timeToLong(datetime),
                    binding.editTextContent.getText().toString(),
                    1
            );
//            TaskRepository.getTaskRepository(getContext()).insert(task);
            Intent res = new Intent();
            res.putExtra("task", task);
            setResult(RESULT_OK, res);
            finish();
        });
        //点击取消按钮，待办取消
        binding.buttonCancel.setOnClickListener(
                v -> {
                    setResult(RESULT_CANCELED);
                    finish();
                });
    }
}
package com.sora.gcdr.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sora.gcdr.adapter.TaskListAdapter;
import com.sora.gcdr.databinding.ActivityMainBinding;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.model.TaskViewModel;


public class MainActivity extends AppCompatActivity implements
        CalendarView.OnYearChangeListener,
        CalendarView.OnCalendarSelectListener {

    private ActivityMainBinding binding;
    private TaskViewModel taskViewModel;
    private TaskListAdapter adapter;
    private int year, month, day, mYear, curDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        adapter = new TaskListAdapter(taskViewModel);
        binding.recycleView.setAdapter(adapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        taskViewModel.getDayTaskLive().observe(this, tasks -> {
            adapter.setDayTasks(tasks);
            adapter.notifyDataSetChanged();
        });

        binding.textViewMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.calendarLayout.isExpand()) {
                    binding.calendarLayout.expand();
                    return;
                }
                binding.calendarView.showYearSelectLayout(mYear);
                binding.textViewLunar.setVisibility(View.GONE);
                binding.textViewYear.setVisibility(View.GONE);
                binding.textViewMonthDay.setText(String.valueOf(mYear));
            }
        });

        binding.fab.setOnClickListener(v -> {
            Task task = new Task(System.currentTimeMillis(), "第二条记录", false);
            taskViewModel.addTask(task);
        });

        binding.calendarView.setOnCalendarSelectListener(this);
        binding.calendarView.setOnYearChangeListener(this);
        mYear = binding.calendarView.getCurYear();
        year = binding.calendarView.getCurYear();
        month = binding.calendarView.getCurMonth();
        curDay = binding.calendarView.getCurDay();

        binding.textViewMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.calendarLayout.isExpand()) {
                    binding.calendarLayout.expand();
                    return;
                }
                binding.calendarView.showYearSelectLayout(mYear);
                binding.textViewLunar.setVisibility(View.GONE);
                binding.textViewYear.setVisibility(View.GONE);
                binding.textViewMonthDay.setText(String.valueOf(mYear));
            }
        });
        binding.flCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calendarView.scrollToCurrent();
            }
        });
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {

        binding.textViewLunar.setVisibility(View.VISIBLE);
        binding.textViewYear.setVisibility(View.VISIBLE);
        binding.textViewMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        binding.textViewYear.setText(String.valueOf(calendar.getYear()));
        binding.textViewLunar.setText(calendar.getLunar());

        mYear = calendar.getYear();
        year = calendar.getYear();
        month = calendar.getMonth();
        curDay = calendar.getDay();

        Log.e("onDateSelected", "  -- " + calendar.getYear() +
                "  --  " + calendar.getMonth() +
                "  -- " + calendar.getDay() +
                "  --  " + isClick + "  --   " + calendar.getScheme());

    }

    @Override
    public void onYearChange(int year) {
        binding.textViewMonthDay.setText(String.valueOf(year));
        Log.e("onYearChange", " 年份变化 " + year);
    }

}
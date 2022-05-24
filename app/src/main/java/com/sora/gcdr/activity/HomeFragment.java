package com.sora.gcdr.activity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sora.gcdr.adapter.TaskListAdapter;
import com.sora.gcdr.databinding.FragmentHomeBinding;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.model.TaskViewModel;


public class HomeFragment extends Fragment implements
        CalendarView.OnYearChangeListener,
        CalendarView.OnCalendarSelectListener{

    private FragmentHomeBinding binding;
    private TaskViewModel taskViewModel;
    private TaskListAdapter adapter;
    private int year, month, day, mYear, curDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        adapter = new TaskListAdapter(taskViewModel);
        binding.recycleView.setAdapter(adapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        taskViewModel.getDayTaskLive().observe(getViewLifecycleOwner(), tasks -> {
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


        return binding.getRoot();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
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
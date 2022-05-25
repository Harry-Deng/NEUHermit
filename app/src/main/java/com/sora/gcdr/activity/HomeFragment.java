package com.sora.gcdr.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sora.gcdr.MyApplication;
import com.sora.gcdr.R;
import com.sora.gcdr.adapter.TaskListAdapter;
import com.sora.gcdr.databinding.FragmentHomeBinding;
import com.sora.gcdr.model.TaskViewModel;


public class HomeFragment extends Fragment implements
        CalendarView.OnYearChangeListener,
        CalendarView.OnCalendarSelectListener{

    private FragmentHomeBinding binding;
    private TaskViewModel taskViewModel;
    private TaskListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);


        taskViewModel.setYear(binding.calendarView.getCurYear());
        taskViewModel.setMonth(binding.calendarView.getCurMonth());
        taskViewModel.setDay(binding.calendarView.getCurDay());


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
                binding.calendarView.showYearSelectLayout(taskViewModel.getYear());
                binding.textViewLunar.setVisibility(View.GONE);
                binding.textViewYear.setVisibility(View.GONE);
                binding.textViewMonthDay.setText(String.valueOf(taskViewModel.getYear()));
            }
        });

        binding.calendarView.setOnCalendarSelectListener(this);
        binding.calendarView.setOnYearChangeListener(this);


        binding.textViewMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.calendarLayout.isExpand()) {
                    binding.calendarLayout.expand();
                    return;
                }
                binding.calendarView.showYearSelectLayout(taskViewModel.getYear());
                binding.textViewLunar.setVisibility(View.GONE);
                binding.textViewYear.setVisibility(View.GONE);
                binding.textViewMonthDay.setText(String.valueOf(taskViewModel.getYear()));
            }
        });

        binding.flCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.calendarView.scrollToCurrent();
            }
        });


        binding.fab.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_homeFragment_to_addTaskFragment);
        });

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

        taskViewModel.setYear(calendar.getYear());
        taskViewModel.setMonth(calendar.getMonth());
        taskViewModel.setDay(calendar.getDay());

        taskViewModel.updateDayTaskLive();
        adapter.notifyDataSetChanged();

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
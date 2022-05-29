package com.sora.gcdr.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import com.sora.gcdr.R;
import com.sora.gcdr.databinding.FragmentHomeBinding;
import com.sora.gcdr.db.Task;

import java.util.List;


public class HomeFragment extends Fragment implements
        CalendarView.OnYearChangeListener,
        CalendarView.OnCalendarSelectListener {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private TaskListAdapter adapter;

    Calendar selectedCalendar;

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
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        binding.textViewLunar.setVisibility(View.VISIBLE);
        binding.textViewYear.setVisibility(View.VISIBLE);

        selectedCalendar = binding.calendarView.getSelectedCalendar();
        binding.textViewMonthDay.setText(selectedCalendar.getYear() + "月" + selectedCalendar.getDay() + "日");
        binding.textViewYear.setText(String.valueOf(selectedCalendar.getYear()));
        binding.textViewLunar.setText(selectedCalendar.getLunar());

        homeViewModel.setYear(binding.calendarView.getCurYear());
        homeViewModel.setMonth(binding.calendarView.getCurMonth());
        homeViewModel.setDay(binding.calendarView.getCurDay());
        homeViewModel.updateDayTaskLive();

        adapter = new TaskListAdapter(homeViewModel);
        binding.recycleView.setAdapter(adapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        homeViewModel.getDayTaskLive().observe(getViewLifecycleOwner(), tasks -> {
//            adapter.setDayTasks(tasks);
            adapter.notifyDataSetChanged();
        });

        binding.textViewMonthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.calendarLayout.isExpand()) {
                    binding.calendarLayout.expand();
                    return;
                }
                binding.calendarView.showYearSelectLayout(homeViewModel.getYear());
                binding.textViewLunar.setVisibility(View.GONE);
                binding.textViewYear.setVisibility(View.GONE);
                binding.textViewMonthDay.setText(String.valueOf(homeViewModel.getYear()));
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
                binding.calendarView.showYearSelectLayout(homeViewModel.getYear());
                binding.textViewLunar.setVisibility(View.GONE);
                binding.textViewYear.setVisibility(View.GONE);
                binding.textViewMonthDay.setText(String.valueOf(homeViewModel.getYear()));
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


        //右滑删除
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.
                SimpleCallback(0
                ,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                homeViewModel.delete(homeViewModel.getDayTaskLive().getValue().get(viewHolder.getAdapterPosition())); ;
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(binding.recycleView);

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

        homeViewModel.setYear(calendar.getYear());
        homeViewModel.setMonth(calendar.getMonth());
        homeViewModel.setDay(calendar.getDay());


        homeViewModel.getDayTaskLive().removeObservers(getViewLifecycleOwner());
        homeViewModel.updateDayTaskLive();
        homeViewModel.getDayTaskLive().observe(getViewLifecycleOwner(), new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
//                adapter.setDayTasks(tasks);
                adapter.notifyDataSetChanged();
            }
        });


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
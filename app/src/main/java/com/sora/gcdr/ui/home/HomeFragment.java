package com.sora.gcdr.ui.home;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sora.gcdr.databinding.FragmentHomeBinding;
import com.sora.gcdr.db.Task;

import java.util.Objects;

/**
 * 日历页界面
 */
public class HomeFragment extends Fragment implements
        CalendarView.OnYearChangeListener,
        CalendarView.OnCalendarSelectListener {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;
    private TaskListAdapter adapter;

    //startActivityForResult的替代，等待添加的结果
    ActivityResultLauncher<Intent> intentActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                //回调函数
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null) {
                        Task task = result.getData().getParcelableExtra("task");
                        homeViewModel.addTask(task);
                    } else {
                        Toast.makeText(getContext(), "插入失败了", Toast.LENGTH_SHORT);
                    }
                }
                //取消了添加
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        //数据绑定，部分使用
        binding.setData(homeViewModel);
        adapter = new TaskListAdapter(homeViewModel);
        binding.recycleView.setAdapter(adapter);
        binding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //启动时手动触发一次 日期选中事件
        onCalendarSelect(binding.calendarView.getSelectedCalendar(), true);
        binding.tvCurrentDay.setText(String.valueOf(homeViewModel.getDay()));

        //左上角日期点击后触发年视图，函数写在外面
        binding.textViewMonthDay.setOnClickListener(this::onMonthDayClickListener);
        //日期选中事件
        binding.calendarView.setOnCalendarSelectListener(this);
        //年视图的日期选中事件
        binding.calendarView.setOnYearChangeListener(this);
        //右上角回到今日
        binding.flCurrent.setOnClickListener(v -> {
            binding.calendarView.closeYearSelectLayout();
            binding.calendarView.scrollToCurrent();
        });
        binding.fab.setOnClickListener(this::onAddTaskClickedListener);

        //右滑删除
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.
                SimpleCallback(0
                , ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                 int direction) {
                homeViewModel.delete(Objects.requireNonNull(homeViewModel.getDayTaskLive().getValue()).get(viewHolder.getAdapterPosition()));
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(binding.recycleView);

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        //显示年和阴历
        binding.textViewLunar.setVisibility(View.VISIBLE);
        binding.textViewYear.setVisibility(View.VISIBLE);
        binding.textViewLunar.setText(calendar.getLunar());
        //更改ViewModel的数据
        homeViewModel.setYear(calendar.getYear());
        homeViewModel.setMonth(calendar.getMonth());
        homeViewModel.setDay(calendar.getDay());
        //同步选中日的待办
        homeViewModel.getDayTaskLive().removeObservers(getViewLifecycleOwner());
        homeViewModel.updateDayTaskLive();
        homeViewModel.getDayTaskLive().observe(getViewLifecycleOwner(), tasks -> adapter.notifyDataSetChanged());
    }

    @Override
    public void onYearChange(int year) {
        binding.textViewMonthDay.setText(String.valueOf(year));
    }

    private void onMonthDayClickListener(View v) {
        if (!binding.calendarLayout.isExpand()) {
            binding.calendarLayout.expand();
            return;
        }
        binding.calendarView.showYearSelectLayout(homeViewModel.getYear());
        binding.textViewLunar.setVisibility(View.GONE);
        binding.textViewYear.setVisibility(View.GONE);
        binding.textViewMonthDay.setText(String.valueOf(homeViewModel.getYear()));
    }

    /**
     * 添加待办的点击事件，跳转到添加待办的Activity，把当前选中的数据传过去
     */
    private void onAddTaskClickedListener(View v) {
        Intent intent = new Intent(getContext(), AddTaskActivity.class);
        intent.putExtra("year", homeViewModel.getYear());
        intent.putExtra("month", homeViewModel.getMonth());
        intent.putExtra("day", homeViewModel.getDay());
//        startActivity(intent);
        intentActivityResultLauncher.launch(intent);
    }
}
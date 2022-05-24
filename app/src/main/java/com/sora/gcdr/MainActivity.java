package com.sora.gcdr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.sora.gcdr.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity implements
        CalendarView.OnYearChangeListener,
        CalendarView.OnCalendarSelectListener {


    private ActivityMainBinding binding;
    private int year, month, day, mYear, curDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);

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
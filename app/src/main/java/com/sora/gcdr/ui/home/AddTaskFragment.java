package com.sora.gcdr.ui.home;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.sora.gcdr.R;
import com.sora.gcdr.databinding.FragmentAddTaskBinding;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.ui.home.HomeViewModel;
import com.sora.gcdr.util.MyUtils;

import java.util.Calendar;

public class AddTaskFragment extends Fragment {

    FragmentAddTaskBinding binding;
    HomeViewModel homeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAddTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //        binding.textViewDate.setText();
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding.textViewDate.setText(homeViewModel.getDate());
        binding.textViewTime.setText(MyUtils.getTimeByLong(System.currentTimeMillis()));

        binding.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                binding.textViewDate.setText("" + year + "-" + (month+1)+ "-" + dayOfMonth);
                            }
                        }, homeViewModel.getYear(), homeViewModel.getMonth(), homeViewModel.getDay()).show();
            }
        });
        binding.textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        binding.textViewTime.setText(hourOfDay + ":" + minute);
                    }
                },
                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                        Calendar.getInstance().get(Calendar.MINUTE),
                        true)
                        .show();
            }
        });

        binding.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datetime =
                        binding.textViewDate.getText().toString() + " " + binding.textViewTime.getText().toString();
                Task task = new Task(
                        MyUtils.timeToLong(datetime),
                        binding.editTextContent.getText().toString(),
                        false
                );
                homeViewModel.addTask(task);
                Navigation.findNavController(v).navigate(R.id.action_addTaskFragment_to_homeFragment);
            }
        });
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_addTaskFragment_to_homeFragment);
            }
        });
    }
}
package com.sora.gcdr.activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import android.os.Bundle;

import com.sora.gcdr.R;
import com.sora.gcdr.databinding.ActivityMainBinding;
import com.sora.gcdr.model.TaskViewModel;


public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding binding;
    TaskViewModel taskViewModel;
    NavController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);
        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        controller = hostFragment.getNavController();
        controller.navigateUp();
    }
}
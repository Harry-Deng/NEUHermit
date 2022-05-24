package com.sora.gcdr.activity;

import androidx.appcompat.app.AppCompatActivity;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sora.gcdr.R;
import com.sora.gcdr.adapter.TaskListAdapter;
import com.sora.gcdr.databinding.ActivityMainBinding;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.model.TaskViewModel;


public class MainActivity extends AppCompatActivity  {

    ActivityMainBinding binding;

    TaskViewModel taskViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_main);
        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);

        NavController controller = hostFragment.getNavController();
        controller.navigateUp();

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        binding.fab.setOnClickListener(v -> {
            Task task = new Task(System.currentTimeMillis(), "第二条记录", false);
            taskViewModel.addTask(task);
        });
    }

}
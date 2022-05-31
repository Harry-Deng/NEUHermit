package com.sora.gcdr.ui;

import static android.graphics.Color.*;

import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.sora.gcdr.R;
import com.sora.gcdr.databinding.ActivityMainBinding;

/**
 * 主Activity
 * 管理各个Fragment
 */
public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //固定
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //导航初始化
        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        navController = hostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

    }

}
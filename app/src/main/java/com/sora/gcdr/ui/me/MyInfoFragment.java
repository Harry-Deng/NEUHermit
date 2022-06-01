package com.sora.gcdr.ui.me;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sora.gcdr.MyApplication;
import com.sora.gcdr.R;
import com.sora.gcdr.activity.LoginActivity;
import com.sora.gcdr.databinding.FragmentMyInfoBinding;

public class MyInfoFragment extends Fragment {

    private MyInfoViewModel mViewModel;
    FragmentMyInfoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMyInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(MyInfoViewModel.class);
        binding.setData(mViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        //功能未实现
        binding.space.setOnClickListener(this::onNoWriteFunctionClicked);
        binding.friends.setOnClickListener(this::onNoWriteFunctionClicked);
        binding.collections.setOnClickListener(this::onNoWriteFunctionClicked);


        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), LoginActivity.class);

                startActivity(intent);
            }
        });
        //未登录
        if (MyApplication.isLogin) {
            binding.logOutCard.setVisibility(View.GONE);
            binding.noUserCard.setVisibility(View.VISIBLE);
            binding.haveUser.setVisibility(View.GONE);
            
        } else {
            binding.logOutCard.setVisibility(View.VISIBLE);
            binding.noUserCard.setVisibility(View.GONE);
            binding.haveUser.setVisibility(View.VISIBLE);

            binding.login.setClickable(true);
        }


    }

    public void onNoWriteFunctionClicked(View view) {
        Toast.makeText(requireActivity(), "功能仍未实现", Toast.LENGTH_SHORT).show();
    }
}
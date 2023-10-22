package com.sora.gcdr.ui.me;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sora.gcdr.R;
import com.sora.gcdr.databinding.FragmentMySpaceBinding;
import com.sora.gcdr.databinding.FragmentShareBinding;
import com.sora.gcdr.ui.share.ShareAdapter;
import com.sora.gcdr.ui.share.ShareViewModel;

public class MySpaceFragment extends Fragment {

    FragmentMySpaceBinding binding;
    ShareAdapter adapter;
    ShareViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMySpaceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar2);

        binding.toolbar2.setTitle("我的空间");
        binding.toolbar2.setTitleTextColor(Color.rgb(255, 255, 255));
        viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        viewModel.getMyShare();

        viewModel.getShareLiveData().observe(getViewLifecycleOwner(), lcObjects -> adapter.notifyDataSetChanged());

        adapter = new ShareAdapter(viewModel);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

    }
}
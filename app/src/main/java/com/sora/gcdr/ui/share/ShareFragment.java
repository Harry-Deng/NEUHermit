package com.sora.gcdr.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sora.gcdr.R;
import com.sora.gcdr.databinding.FragmentShareBinding;

import java.util.List;

import cn.leancloud.LCObject;

public class ShareFragment extends Fragment {
    FragmentShareBinding binding;
    ShareAdapter adapter;
    ShareViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentShareBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);

        viewModel.getShareLiveData().observe(getViewLifecycleOwner(), new Observer<List<LCObject>>() {
            @Override
            public void onChanged(List<LCObject> lcObjects) {
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new ShareAdapter(viewModel);
        binding.recyclerview.setAdapter(adapter);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_easyAppFragment_to_addShareFragment);
            }
        });

    }
}
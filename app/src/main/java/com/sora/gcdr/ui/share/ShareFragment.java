package com.sora.gcdr.ui.share;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.sora.gcdr.MyApplication;
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
        viewModel.updateShare();

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
                //未登录
                if ("".equals(MyApplication.getInstance().getUser().getUsername() )) {
                    Toast.makeText(MyApplication.getInstance(), "只有登录才能发布动态..", Toast.LENGTH_SHORT).show();
                    return;
                }
                Navigation.findNavController(v).navigate(R.id.action_easyAppFragment_to_addShareFragment);
            }
        });

        binding.getmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getMoreShare();
                adapter.notifyDataSetChanged();
            }
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.updateShare();
                binding.swipeRefresh.setRefreshing(false);
            }
        });

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
                viewModel.getShareLiveData().getValue().remove(viewHolder.getAdapterPosition());
                adapter.notifyDataSetChanged();
//                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(binding.recyclerview);

    }

}
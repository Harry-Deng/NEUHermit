package com.sora.gcdr.ui.app;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sora.gcdr.databinding.CellShareBinding;
import com.sora.gcdr.databinding.CellTaskBinding;
import com.sora.gcdr.db.ShareItem;
import com.sora.gcdr.db.Task;
import com.sora.gcdr.ui.home.HomeViewModel;
import com.sora.gcdr.ui.home.TaskListAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCObject;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {
    List<LCObject> shareItems;
//    private EasyAppViewModel homeViewModel;
//
//    public ShareAdapter(EasyAppViewModel homeViewModel) {
//        this.homeViewModel = homeViewModel;
//    }

    public void setShareItems(List<LCObject> shareItems) {
        this.shareItems = shareItems;
    }

    public static class ShareViewHolder extends RecyclerView.ViewHolder {
        CellShareBinding binding;
        public ShareViewHolder(CellShareBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @NonNull
    @Override
    public ShareViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CellShareBinding binding = CellShareBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ShareViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewHolder holder, int position) {
        if (shareItems==null)
            return;
        LCObject shareItem = shareItems.get(position);
        String user=  shareItem.getString("user");
        String datetime = shareItem.getUpdatedAtString();
        String content = shareItem.getString("content");

        holder.binding.username.setText(user);;
        holder.binding.datetime.setText(datetime);;
        holder.binding.content.setText(content);;

    }

    @Override
    public int getItemCount() {
        if (shareItems == null)
            return 0;
        return shareItems.size();
    }


}

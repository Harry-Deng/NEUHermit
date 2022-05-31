package com.sora.gcdr.ui.share;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sora.gcdr.databinding.CellShareBinding;

import cn.leancloud.LCObject;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private ShareViewModel shareViewModel;

    public ShareAdapter(ShareViewModel shareViewModel) {
        this.shareViewModel = shareViewModel;
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
        if (shareViewModel.getShareLiveData().getValue()==null)
            return;
        LCObject shareItem = shareViewModel.getShareLiveData().getValue().get(position);
        String user=  shareItem.getString("user");
        String datetime = shareItem.getUpdatedAtString();
        String content = shareItem.getString("content");

        holder.binding.username.setText(user);;
        holder.binding.datetime.setText(datetime);;
        holder.binding.content.setText(content);;

    }

    @Override
    public int getItemCount() {
        if (shareViewModel.getShareLiveData().getValue() == null)
            return 0;
        return shareViewModel.getShareLiveData().getValue().size();
    }


}

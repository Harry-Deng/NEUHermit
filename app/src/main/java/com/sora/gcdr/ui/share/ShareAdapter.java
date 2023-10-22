package com.sora.gcdr.ui.share;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sora.gcdr.R;
import com.sora.gcdr.databinding.CellShareBinding;
import com.sora.gcdr.util.MyUtils;

import cn.leancloud.LCObject;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private ShareViewModel shareViewModel;

    private boolean isGood = true;

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
        if (shareViewModel.getShareLiveData().getValue() == null)
            return;
        LCObject shareItem = shareViewModel.getShareLiveData().getValue().get(position);
        String nickname = shareItem.getString("nickname");
        String datetime = MyUtils.getDateTimeByLong(shareItem.getUpdatedAt().getTime());
        String content = shareItem.getString("content");
        String image = shareItem.getString("qqNumber");

        holder.binding.nickName.setText(nickname);

        holder.binding.datetime.setText(datetime);

        holder.binding.content.setText(content);

        holder.binding.goodView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {
                if (isGood){
                    holder.binding.goodView.setImageDrawable(view.getResources().getDrawable(R.drawable.gooded));
                    isGood = false;
                }else {
                    holder.binding.goodView.setImageDrawable(view.getResources().getDrawable(R.drawable.good));
                    isGood = true;
                }

            }
        });

        Glide.with(holder.itemView)
                .load("https://q4.qlogo.cn/g?b=qq&nk=" + image + "&s=100")
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.default_avater)
                .into(holder.binding.image);
    }

    @Override
    public int getItemCount() {
        if (shareViewModel.getShareLiveData().getValue() == null)
            return 0;
        return shareViewModel.getShareLiveData().getValue().size();
    }


}

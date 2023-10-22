package com.sora.gcdr.ui.me;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sora.gcdr.MyApplication;
import com.sora.gcdr.R;
import com.sora.gcdr.activity.LoginActivity;
import com.sora.gcdr.databinding.FragmentMyInfoBinding;
import com.sora.gcdr.databinding.InputNumberLayoutBinding;
import com.sora.gcdr.db.User;
import com.yxing.ScanCodeActivity;
import com.yxing.ScanCodeConfig;
import com.yxing.def.ScanStyle;

import cn.leancloud.LCObject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MyInfoFragment extends Fragment {

    private MyInfoViewModel mViewModel;
    FragmentMyInfoBinding binding;

    ActivityResultLauncher<Intent> intentActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    freshContent();
                    //取消了添加
                }
            });

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
        binding.settings.setOnClickListener(this::onNoWriteFunctionClicked);


        binding.scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScanCodeConfig.create(MyInfoFragment.this.getActivity() ,MyInfoFragment.this)
                        //设置扫码页样式 ScanStyle.NONE：无  ScanStyle.QQ ：仿QQ样式   ScanStyle.WECHAT ：仿微信样式    ScanStyle.CUSTOMIZE ： 自定义样式
                        .setStyle(ScanStyle.QQ)
                        //扫码成功是否播放音效  true ： 播放   false ： 不播放
                        .setPlayAudio(true)
                        .buidler()
                        //跳转扫码页   扫码页可自定义样式
                        .start(ScanCodeActivity.class);
            }
        });

        binding.imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputNumberLayoutBinding binding = InputNumberLayoutBinding.inflate(getLayoutInflater());
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(requireActivity())
                        .setIcon(R.drawable.qq)
                        .setTitle("关联你的QQ头像")
                        .setCancelable(false)
                        .setView(binding.getRoot());
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //保存数据
                                MyApplication.getInstance().getUser().setQqNumber(binding.qqNumber.getText().toString());
                                LCObject todo = LCObject.createWithoutData("user", MyApplication.getInstance().getUser().getId());
                                todo.put(User.KEY_QQ_NUMBER, MyApplication.getInstance().getUser().getQqNumber());
                                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                                    public void onSubscribe(Disposable disposable) {
                                    }

                                    public void onNext(LCObject savedTodo) {
                                        freshContent();
                                    }

                                    public void onError(Throwable throwable) {
                                    }
                                    public void onComplete() {
                                    }
                                });
                                ;
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.create().show();
            }
        });

        freshContent();

        binding.login.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            intentActivityResultLauncher.launch(intent);
        });

        //退出登录，删除本地信息
        binding.logOutCard.setOnClickListener(v -> {
            SharedPreferences shp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = shp.edit();
            edit.clear();
            edit.apply();
            MyApplication.getInstance().loadUser();
            freshContent();
        });
    }

    private void freshContent() {
        //未登录
        if ("".equals(MyApplication.getInstance().getUser().getUsername())) {
            binding.logOutCard.setVisibility(View.GONE);
            binding.logoffComponent.setVisibility(View.VISIBLE);
            binding.loginComponent.setVisibility(View.GONE);
        } else {
            binding.logOutCard.setVisibility(View.VISIBLE);
            binding.logoffComponent.setVisibility(View.GONE);
            binding.loginComponent.setVisibility(View.VISIBLE);

            binding.username.setText(MyApplication.getInstance().getUser().getUsername());
            binding.nickname.setText(MyApplication.getInstance().getUser().getNickname());
            Glide.with(this)
                    .load("https://q4.qlogo.cn/g?b=qq&nk=" + MyApplication.getInstance().getUser().getQqNumber() + "&s=100")
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.default_avater)
                    .into(binding.imageAvatar);
            binding.login.setClickable(true);
        }

        binding.space.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_myInfoFragment_to_mySpaceFragment);
            }
        });
    }

    public void onNoWriteFunctionClicked(View view) {
        Toast.makeText(requireActivity(), "功能仍未实现", Toast.LENGTH_SHORT).show();
    }

}
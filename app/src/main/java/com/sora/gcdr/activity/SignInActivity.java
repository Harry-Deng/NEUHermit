package com.sora.gcdr.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sora.gcdr.MyApplication;
import com.sora.gcdr.databinding.ActivitySignInBinding;
import com.sora.gcdr.db.User;
import com.sora.gcdr.ui.me.MyInfoViewModel;

import cn.leancloud.LCObject;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.floatingActionButtonBackToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //点击注册，开始
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //注册信息保存到本地
//                MyApplication.getInstance().getUser().setUsername(binding.editTextUsername.getText().toString());
//                MyApplication.getInstance().getUser().setPassword(binding.editTextPassword.getText().toString());
//                MyApplication.getInstance().getUser().setQqNumber("");
//                MyApplication.getInstance().getUser().setNickname(binding.editTextNickname.getText().toString());
                //注册信息上传到云端
                LCObject user = new LCObject("user");
                user.put(User.KEY_USERNAME, binding.editTextUsername.getText().toString());
                user.put(User.KEY_PASSWORD, binding.editTextPassword.getText().toString());
                user.put(User.KEY_NICKNAME, binding.editTextNickname.getText().toString());
                user.put(User.KEY_QQ_NUMBER, "");
                user.saveInBackground()
//                        .observeOn(Schedulers.io())
//                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<LCObject>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                Toast.makeText(MyApplication.getInstance(), "注册中。。。", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onNext(LCObject lcObject) {
                                if (lcObject != null) {
                                    //注册成功，把账号密码发给登录界面，准备登录
                                    Intent intent = new Intent();
                                    intent.putExtra("username", lcObject.getString(User.KEY_USERNAME));
                                    intent.putExtra("password", lcObject.getString(User.KEY_PASSWORD));
                                    Toast.makeText(MyApplication.getInstance(), "注册成功。。。", Toast.LENGTH_LONG).show();
                                    setResult(RESULT_OK,intent);
                                    finish();
                                } else {
                                    Toast.makeText(MyApplication.getInstance(), "注册失败。。。", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(MyApplication.getInstance(), "用户名已存在。。。", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });

    }
}

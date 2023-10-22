package com.sora.gcdr.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sora.gcdr.MyApplication;
import com.sora.gcdr.databinding.ActivityLoginBinding;
import com.sora.gcdr.db.User;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    ActivityResultLauncher<Intent> intentActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    //回调函数
                    if (result.getResultCode() == RESULT_OK) {
                        //注册成功,填充注册信息
                        if (result.getData() != null) {
                            String username = result.getData().getStringExtra("username");
                            String password = result.getData().getStringExtra("password");
                            binding.editUsername.setText(username);
                            binding.editPassword.setText(password);
                            login();
                        }

                    }
                    //取消了添加
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        binding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignInActivity.class);
                intentActivityResultLauncher.launch(intent);
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


        binding.textViewForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MyApplication.getInstance(), "你再想想", Toast.LENGTH_LONG).show();
            }
        });
    }



    private void login() {
        LCQuery<LCObject> query = new LCQuery<>("user");

        query.whereEqualTo(User.KEY_USERNAME, binding.editUsername.getText().toString())
                .whereEqualTo(User.KEY_PASSWORD, binding.editPassword.getText().toString())
                .getFirstInBackground()
                .subscribe(new Observer<LCObject>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(LCObject lcObject) {
                        if (lcObject != null ) {
                            //登陆成功后同步云端数据到本地
                            Toast.makeText(MyApplication.getInstance(), "登录成功", Toast.LENGTH_LONG).show();
                            MyApplication.getInstance().getUser().setId(lcObject.getObjectId());
                            MyApplication.getInstance().getUser().setUsername( lcObject.getString(User.KEY_USERNAME));
                            MyApplication.getInstance().getUser().setPassword( lcObject.getString(User.KEY_PASSWORD));
                            MyApplication.getInstance().getUser().setNickname( lcObject.getString(User.KEY_NICKNAME));
                            MyApplication.getInstance().getUser().setQqNumber( lcObject.getString(User.KEY_QQ_NUMBER));

                            MyApplication.getInstance().saveUser();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(MyApplication.getInstance(), "登录出错", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MyApplication.getInstance(), "应用异常", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}

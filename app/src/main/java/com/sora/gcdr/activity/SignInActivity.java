package com.sora.gcdr.activity;

import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sora.gcdr.MyApplication;
import com.sora.gcdr.R;
import com.sora.gcdr.databinding.ActivitySignInBinding;
import com.sora.gcdr.db.User;

import cn.leancloud.LCObject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;

    private Drawable eye_close;
    private Drawable eye_open;
    private Drawable lockLogo;
    private Drawable confirmRight;
    private Drawable confirmWrong;
    private Boolean eye_state = true;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        this.getWindow().setStatusBarColor(Color.TRANSPARENT);
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );

        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.floatingActionButtonBackToLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        init();

        //点击注册
        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInput()){
                    //注册信息上传到云端
                    LCObject user = new LCObject("user");
                    user.put(User.KEY_USERNAME, binding.editTextUsername.getText().toString());
                    user.put(User.KEY_PASSWORD, binding.editTextPassword.getText().toString());
                    user.put(User.KEY_NICKNAME, binding.editTextNickname.getText().toString());
                    user.put(User.KEY_QQ_NUMBER, "");
                    user.saveInBackground().subscribe(new Observer<LCObject>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(LCObject lcObject) {
                            if (lcObject != null) {
                                //注册成功，把账号密码发给登录界面，准备登录
                                Intent intent = new Intent();
                                intent.putExtra("username", lcObject.getString(User.KEY_USERNAME));
                                intent.putExtra("password", lcObject.getString(User.KEY_PASSWORD));
                                Toast.makeText(MyApplication.getInstance(), "注册成功", Toast.LENGTH_LONG).show();
                                setResult(RESULT_OK,intent);
                                finish();
                            } else {
                                Toast.makeText(MyApplication.getInstance(), "注册失败", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(MyApplication.getInstance(), "用户名重复", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
            }
        });
    }

    //检查密码是否符合规范
    private boolean checkPassWord(String password) {
        boolean hasSpace = false;
        boolean hasDigit = false;
        boolean hasLetter = false;
        boolean lengthIsCorrect = false;

        //检查密码是否有空格、大小写字母、数字
        for(int i = 0; i < password.length(); i++){
            char c = password.charAt(i);
            if(Character.isSpaceChar(c)){
                hasSpace = true;
                break;
            }
            if(Character.isDigit(c)){
                hasDigit = true;
            }
            if(Character.isUpperCase(c)||Character.isLowerCase(c)){
                hasLetter = true;
            }
        }

        //检查密码长度
        if(password.length() > 7 && password.length() < 17){
            lengthIsCorrect = true;
        }

        return (!hasSpace && hasDigit && hasLetter && lengthIsCorrect);
    }

    //检查注册页面输入的数据是否符合规范
    private boolean checkInput() {
        boolean isChecked = true;
        if (binding.editTextUsername.getText().toString().equals("") || (binding.editTextPassword.getText().toString().equals("") || binding.editTextPasswordConfirm.getText().toString().equals("") || binding.editTextNickname.getText().toString().equals(""))) {
            showAlertDialog("请完善您的信息");
            isChecked = false;
        } else if (!checkPassWord(binding.editTextPassword.getText().toString())){
            showAlertDialog("密码的长度应在8-16个字符之间，同时包含大小写字母和数字");
            isChecked = false;
        }else if (!binding.editTextPassword.getText().toString().equals(binding.editTextPasswordConfirm.getText().toString())){
            showAlertDialog("两次密码输入不一致");
            isChecked = false;
        }else if (!binding.checkBoxUserProtocol.isChecked()){
            showAlertDialog("请先同意用户服务协议和隐私政策");
            isChecked = false;
        }
        return isChecked;
    }

    private void showAlertDialog(String message){
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(SignInActivity.this);
        normalDialog.setMessage(message);
        normalDialog.setNegativeButton("确认", null);
        normalDialog.show();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        binding.editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] tempInputStrUnchecked = s.toString().split(" ");
                    StringBuilder tempInputStrChecked = new StringBuilder();
                    for (String value : tempInputStrUnchecked) {
                        tempInputStrChecked.append(value);
                    }
                    binding.editTextPassword.setText(tempInputStrChecked.toString());
                    binding.editTextPassword.setSelection(start);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //监听输入框禁止输入空格
        binding.editTextPasswordConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(" ")) {
                    String[] tempInputStrUnchecked = s.toString().split(" ");
                    StringBuilder tempInputStrChecked = new StringBuilder();
                    for (String value : tempInputStrUnchecked) {
                        tempInputStrChecked.append(value);
                    }
                    binding.editTextPasswordConfirm.setText(tempInputStrChecked.toString());
                    binding.editTextPasswordConfirm.setSelection(start);
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        eye_open = getResources().getDrawable(R.drawable.eyes_open, null);
        eye_open.setBounds(0, 0, 55, 55);
        eye_close = getResources().getDrawable(R.drawable.eyes_close, null);
        eye_close.setBounds(0, 0, 55, 55);
        lockLogo = getResources().getDrawable(R.drawable.password, null);
        lockLogo.setBounds(0, 0, 55, 55);
        confirmRight = getResources().getDrawable(R.drawable.right, null);
        confirmRight.setBounds(0, 0, 48, 48);
        confirmWrong = getResources().getDrawable(R.drawable.wrong, null);
        confirmWrong.setBounds(0, 0, 48, 48);
        binding.textViewUserServiceAgreement.setText(Html.fromHtml("<a href='https://www.dengemo.com'>用户服务协议</a>", FROM_HTML_MODE_COMPACT));
        binding.textViewUserServiceAgreement.setMovementMethod(LinkMovementMethod.getInstance());
        binding.textViewPrivacyPolicy.setText(Html.fromHtml("<a href='https://www.dengemo.com'>隐私政策</a>", FROM_HTML_MODE_COMPACT));
        binding.textViewPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());

        binding.editTextPasswordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (binding.editTextPassword.getText().toString().equals(binding.editTextPasswordConfirm.getText().toString())) {
                        if (binding.editTextPassword.getText().toString().equals("")) {
                            binding.editTextPasswordConfirm.setCompoundDrawables(lockLogo, null, confirmWrong, null);
                        } else {
                            binding.editTextPasswordConfirm.setCompoundDrawables(lockLogo, null, confirmRight, null);
                        }
                    } else {
                        binding.editTextPasswordConfirm.setCompoundDrawables(lockLogo, null, confirmWrong, null);
                    }
                }
            }
        });

        binding.editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getAction()!=MotionEvent.ACTION_UP){
                        return false;
                    }
                    binding.editTextPassword.setCompoundDrawables(lockLogo, null, eye_close,null);
                    Drawable currentDrawable = binding.editTextPassword.getCompoundDrawables()[2];

                    if(motionEvent.getX()>binding.editTextPassword.getWidth()-binding.editTextPassword.getPaddingRight()-currentDrawable.getIntrinsicWidth()){
                        if(eye_state){
                            binding.editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            binding.editTextPassword.setCompoundDrawables(lockLogo, null, eye_open, null);
                        }else {
                            binding.editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            binding.editTextPassword.setCompoundDrawables(lockLogo, null, eye_close, null);
                        }
                        eye_state = !eye_state;
                    }
                    return false;
            }
        });
    }

    //实现对组件焦点变动的反应
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    private boolean isShouldHideInput(View view, MotionEvent event) {
        if (view instanceof EditText) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            view.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + view.getHeight();
            int right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                view.setFocusable(false);
                view.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
    }
}
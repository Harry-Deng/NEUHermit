package com.sora.gcdr.ui.me;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.sora.gcdr.MyApplication;

import java.util.logging.Handler;

public class MyInfoViewModel extends AndroidViewModel {
    SavedStateHandle handle;
    private static final String KEY_AVATAR = "com.sora.gcdr.ui.me.avatar";
    private static final String KEY_USERNAME = "com.sora.gcdr.ui.me.username";
    private static final String KEY_NICKNAME = "com.sora.gcdr.ui.me.nickname";
    private static final String KEY_PASSWORD = "com.sora.gcdr.ui.me.password";

    public MyInfoViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        this.handle = handle;
        if (!handle.contains(KEY_AVATAR)) {
            SharedPreferences shp = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
            handle.set(KEY_AVATAR, shp.getString(KEY_AVATAR, ""));

            handle.set(KEY_USERNAME, shp.getString(KEY_USERNAME, ""));

            handle.set(KEY_NICKNAME, shp.getString(KEY_NICKNAME, ""));

            handle.set(KEY_PASSWORD, shp.getString(KEY_PASSWORD, ""));
            MyApplication.isLogin = true;
        }
    }

    public String getUsername() {
        return handle.get(KEY_USERNAME);
    }

    public String getNickname() {
        return handle.get(KEY_NICKNAME);
    }

    public String getAvatar() {
        return handle.get(KEY_AVATAR);
    }

    public String getPassword() {
        return handle.get(KEY_PASSWORD);
    }

    public void save() {
        SharedPreferences shp = getApplication().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(KEY_AVATAR, handle.get(KEY_AVATAR));
        edit.putString(KEY_USERNAME, handle.get(KEY_USERNAME));
        edit.putString(KEY_NICKNAME, handle.get(KEY_NICKNAME));
        edit.putString(KEY_PASSWORD, handle.get(KEY_PASSWORD));
        edit.apply();
    }
}
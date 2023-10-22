package com.sora.gcdr;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.sora.gcdr.db.User;

import cn.leancloud.LeanCloud;

public class MyApplication extends Application {
    private static MyApplication myApplication;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        //LeanCloud初始化
        LeanCloud.initialize(this, "f8Q703mVGuhFns6PpwvfxFdx-gzGzoHsz", "bqkQjMhzJ0yPJUo2dwQAlnOd", "https://f8q703mv.lc-cn-n1-shared.com");

        user = new User();
        loadUser();
    }

    public void loadUser() {
        SharedPreferences shp = getSharedPreferences("user", Context.MODE_PRIVATE);
        user.setUsername(shp.getString(User.KEY_USERNAME, ""));
        user.setPassword(shp.getString(User.KEY_PASSWORD, ""));
        user.setNickname(shp.getString(User.KEY_NICKNAME, ""));
        user.setQqNumber(shp.getString(User.KEY_QQ_NUMBER, ""));
        user.setId(shp.getString(User.KEY_USER_ID,""));
    }

    public void saveUser() {
        //保存到本地
        SharedPreferences shp = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shp.edit();
        edit.putString(User.KEY_USERNAME, user.getUsername());
        edit.putString(User.KEY_NICKNAME, user.getNickname());
        edit.putString(User.KEY_PASSWORD, user.getPassword());
        edit.putString(User.KEY_QQ_NUMBER, user.getQqNumber());
        edit.putString(User.KEY_USER_ID, user.getId());
        edit.apply();
    }

    public User getUser() {
        return user;
    }

    public static MyApplication getInstance() {
        return myApplication;
    }
}

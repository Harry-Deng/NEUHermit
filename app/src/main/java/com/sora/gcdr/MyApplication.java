package com.sora.gcdr;

import android.app.Application;
import android.content.SharedPreferences;

import cn.leancloud.LeanCloud;

public class MyApplication extends Application {

    private static MyApplication myApplication;
    public static boolean isLogin;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        isLogin = false;
        //LeanCloud初始化
        LeanCloud.initialize(this, "f8Q703mVGuhFns6PpwvfxFdx-gzGzoHsz", "bqkQjMhzJ0yPJUo2dwQAlnOd", "https://f8q703mv.lc-cn-n1-shared.com");
    }

    public static MyApplication getInstance() {
        return myApplication;
    }
}

package com.example.text;

import android.app.Application;

import cn.leancloud.LeanCloud;

public class MyLeanCloudApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 提供 this、App ID、App Key、Server Host 作为参数
        // 注意这里千万不要调用 cn.leancloud.core.LeanCloud 的 initialize 方法，否则会出现 NetworkOnMainThread 等错误。
        LeanCloud.initialize(this,
                "pEtzS5WpbQQlGEDfaQ3eThCy-gzGzoHsz",
                "G99jlJLBw79DHw92Ny5PIStf",
                "https://petzs5wp.lc-cn-n1-shared.com");

    }
}

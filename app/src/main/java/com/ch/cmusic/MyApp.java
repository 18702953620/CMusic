package com.ch.cmusic;

import android.app.Application;

/**
 * 作者： ch
 * 时间： 2019/3/22 0022-上午 11:44
 * 描述：
 * 来源：
 */

public class MyApp extends Application {

    private static MyApp myApp;

    public static MyApp getMyApp() {
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
    }
}

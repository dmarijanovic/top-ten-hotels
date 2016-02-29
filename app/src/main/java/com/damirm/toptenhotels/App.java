package com.damirm.toptenhotels;

import android.app.Application;

import com.damirm.toptenhotels.utils.Cache;

public class App extends Application {

    public static App instance;

    private Cache cache;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        cache = new Cache();
    }

    public Cache getCache() {
        return cache;
    }

    public static App get() {
        return instance;
    }
}

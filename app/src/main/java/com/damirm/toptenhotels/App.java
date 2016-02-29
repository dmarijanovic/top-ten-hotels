package com.damirm.toptenhotels;

import android.app.Application;

import com.damirm.toptenhotels.utils.BitmapCache;

public class App extends Application {

    public static App instance;

    private BitmapCache bitmapCache;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        bitmapCache = new BitmapCache();
    }

    public BitmapCache getBitmapCache() {
        return bitmapCache;
    }

    public static App get() {
        return instance;
    }
}

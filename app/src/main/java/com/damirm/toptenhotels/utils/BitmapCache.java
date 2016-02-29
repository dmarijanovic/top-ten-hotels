package com.damirm.toptenhotels.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.HashMap;
import java.util.Map;

public class BitmapCache {

    private LruCache<String, Bitmap> bitmapMemoryCache;
    private Map<String, Integer> placeImageCount;

    public BitmapCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        bitmapMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        placeImageCount = new HashMap<>();
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null && bitmap != null) {
            bitmapMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return bitmapMemoryCache.get(key);
    }

    public int getImageCount(String placeId) {
        return placeImageCount.get(placeId);
    }

    public void setImageCount(String placeId, int count) {
        placeImageCount.put(placeId, count);
    }
}

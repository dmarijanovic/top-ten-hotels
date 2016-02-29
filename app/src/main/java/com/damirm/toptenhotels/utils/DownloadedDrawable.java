package com.damirm.toptenhotels.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.damirm.toptenhotels.network.PlacePhotoTask;

import java.lang.ref.WeakReference;

public class DownloadedDrawable extends ColorDrawable {

    private final WeakReference<PlacePhotoTask> placePhotoTaskReference;

    public DownloadedDrawable(PlacePhotoTask placePhotoTask) {
        super(Color.WHITE);
        placePhotoTaskReference = new WeakReference<>(placePhotoTask);
    }

    public PlacePhotoTask getPlacePhotoTaks() {
        return placePhotoTaskReference.get();
    }
}

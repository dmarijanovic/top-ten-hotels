package com.damirm.toptenhotels.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.damirm.toptenhotels.App;
import com.damirm.toptenhotels.network.PlacePhotoTask;
import com.google.android.gms.common.api.GoogleApiClient;

public class PhotoUtil {

    public static void downloadOrGetFromCache(GoogleApiClient apiClient, ImageView imageView,
                                              String placeId, int imagePosition, boolean scaled) {
        Bitmap bitmap = App.get().getCache().getBitmapFromMemCache(getCacheKey(imageView, placeId, imagePosition, scaled));

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            if (cancelPotentialDownload(imageView, placeId, imagePosition)) {
                PlacePhotoTask photoTask = new PlacePhotoTask(apiClient, imageView, placeId, imagePosition, scaled);
                DownloadedDrawable downloadedDrawable = new DownloadedDrawable(photoTask);
                imageView.setImageDrawable(downloadedDrawable);
                photoTask.execute(placeId);
            }
        }
    }

    private static boolean cancelPotentialDownload(ImageView imageView, String placeId, int imagePosition) {
        PlacePhotoTask placePhotoTask = getPlacePhotoTask(imageView);
        if (placePhotoTask != null) {
            if (placePhotoTask.placeId == null ||
                    !(placePhotoTask.placeId.equals(placeId) && placePhotoTask.position == imagePosition)) {
                placePhotoTask.cancel(true);
            } else {
                // The same URL is already being downloaded.
                return false;
            }
        }
        return true;
    }

    public static PlacePhotoTask getPlacePhotoTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                DownloadedDrawable downloadedDrawable = (DownloadedDrawable)drawable;
                return downloadedDrawable.getPlacePhotoTaks();
            }
        }
        return null;
    }

    public static String getCacheKey(ImageView imageView, String placeId, int imagePosition, boolean scaled) {
        return placeId + "-" + imagePosition + "-" +
                (scaled ? imageView.getHeight() + "-" + imageView.getWidth() : "full-size" );
    }

}

package com.damirm.toptenhotels.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.damirm.toptenhotels.App;
import com.damirm.toptenhotels.R;
import com.damirm.toptenhotels.network.PlacePhotoTask;
import com.google.android.gms.common.api.GoogleApiClient;

public class PhotoUtil {

    public interface PhotoCallback {
        void onPhotoLoaded();
    }

    /**
     * Method will set bitmap image to image view. If bitmap is not present in cache it will
     * execute async request to download image from network. If view is marked as stable view it will
     * check view already contains requested image.
     *
     * @param apiClient Connection to google api
     * @param imageView View whet image will be placed
     * @param placeId Place id
     * @param imagePosition Image position from selected place
     * @param scaled If true it will scale image to current view width and height
     * @param hasStableView If true it will check if view already contains requested image
     */
    public static void downloadOrGetFromCache(GoogleApiClient apiClient, ImageView imageView,
              String placeId, int imagePosition, boolean scaled, boolean hasStableView, final PhotoCallback photoCallback) {

        if (hasStableView && !needUpdate(imageView, placeId, imagePosition, scaled)) {
            notifyListener(photoCallback);
            return;
        }

        Bitmap bitmap = App.get().getBitmapCache().getBitmapFromMemCache(getCacheKey(imageView, placeId, imagePosition, scaled));

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            notifyListener(photoCallback);
        } else {
            if (cancelPotentialDownload(imageView, placeId, imagePosition)) {
                PlacePhotoTask photoTask = new PlacePhotoTask(apiClient, imageView, placeId, imagePosition, scaled) {

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);

                        notifyListener(photoCallback);
                    }
                };

                DownloadedDrawable downloadedDrawable = new DownloadedDrawable(photoTask);
                imageView.setImageDrawable(downloadedDrawable);
                photoTask.execute(placeId);
            }
        }
    }

    private static void notifyListener(final PhotoCallback photoCallback) {
        if (photoCallback != null) {
            photoCallback.onPhotoLoaded();
        }
    }

    public static void downloadOrGetFromCache(GoogleApiClient apiClient, ImageView imageView,
                                              String placeId, int imagePosition, boolean scaled, boolean hasStableView) {
        downloadOrGetFromCache(apiClient, imageView, placeId, imagePosition, scaled, hasStableView, null);
    }

    public static void downloadOrGetFromCache(GoogleApiClient apiClient, ImageView imageView,
                                              String placeId, int imagePosition, boolean scaled) {
        downloadOrGetFromCache(apiClient, imageView, placeId, imagePosition, scaled, false, null);
    }

    private static boolean cancelPotentialDownload(ImageView imageView, String placeId, int imagePosition) {
        PlacePhotoTask placePhotoTask = getPlacePhotoTask(imageView);
        if (placePhotoTask != null) {
            if (placePhotoTask.placeId == null ||
                    !(placePhotoTask.placeId.equals(placeId) && placePhotoTask.position == imagePosition)) {
                placePhotoTask.cancel(true);
            } else {
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
                return downloadedDrawable.getPlacePhotoTask();
            }
        }
        return null;
    }

    public static String getCacheKey(ImageView imageView, String placeId, int imagePosition, boolean scaled) {
        return placeId + "-" + imagePosition + "-" +
                (scaled ? imageView.getHeight() + "-" + imageView.getWidth() : "full-size" );
    }

    private static boolean needUpdate(ImageView imageView, String placeId, int imagePosition, boolean scaled) {
        String cacheKey = getCacheKey(imageView, placeId, imagePosition, scaled);
        String imageCacheKey = (String) imageView.getTag(R.id.KEY_IMAGE_CACHE_KEY);
        return !cacheKey.equals(imageCacheKey);
    }

}

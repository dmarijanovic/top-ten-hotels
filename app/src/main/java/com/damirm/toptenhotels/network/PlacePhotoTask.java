package com.damirm.toptenhotels.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.damirm.toptenhotels.App;
import com.damirm.toptenhotels.utils.PhotoUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.Places;

import java.lang.ref.WeakReference;

public class PlacePhotoTask extends AsyncTask<String, Void, Bitmap> {

    private GoogleApiClient apiClient;
    private WeakReference<ImageView> imageViewReference;
    private boolean scaled;
    private int width;
    private int height;
    public String placeId;
    public int position;

    public PlacePhotoTask(GoogleApiClient apiClient, ImageView imageView, String placeId,
                          int position, boolean scaled) {
        this.apiClient = apiClient;
        this.position = position;
        this.imageViewReference = new WeakReference<>(imageView);
        this.placeId = placeId;
        this.scaled = scaled;
        this.width = imageView.getWidth() >= 64 ? imageView.getWidth() : 64;
        this.height = imageView.getHeight() >= 64 ? imageView.getHeight() : 64;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;

        PlacePhotoMetadataResult result = Places.GeoDataApi
                .getPlacePhotos(apiClient, placeId).await();

            if (result.getStatus().isSuccess()) {
                PlacePhotoMetadataBuffer photoMetadata = result.getPhotoMetadata();

                App.get().getBitmapCache().setImageCount(placeId, photoMetadata.getCount());
                if (photoMetadata.getCount() > position && !isCancelled()) {
                    PlacePhotoMetadata photo = photoMetadata.get(position);

                    if (scaled) {
                        bitmap = photo.getScaledPhoto(apiClient, width, height).await().getBitmap();
                    } else {
                        bitmap = photo.getPhoto(apiClient).await().getBitmap();
                    }
                }
                photoMetadata.release();
            }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }

        ImageView imageView = imageViewReference != null ? imageViewReference.get() : null;
        if (imageView != null) {
            PlacePhotoTask placePhotoTask = PhotoUtil.getPlacePhotoTask(imageView);

            if (placePhotoTask != null && placePhotoTask.equals(this)) {
                imageView.setImageBitmap(bitmap);
                App.get().getBitmapCache()
                        .addBitmapToMemoryCache(PhotoUtil.getCacheKey(imageView, placeId, position, scaled), bitmap);
            }
        }
    }

}

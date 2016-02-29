package com.damirm.toptenhotels.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.damirm.toptenhotels.R;
import com.damirm.toptenhotels.utils.ZoomableImageView;

public class ImageFragment extends Fragment {
    

    private static final String TAG = ImageFragment.class.getSimpleName();

    private static final String KEY_IMAGE_INDEX = "image_index";

    ZoomableImageView zoomableImageView;
    private GalleryImage galleryImage;

    private int imagePosition;

    public interface GalleryImage {
        void requestImageByPosition(ImageView imageView, int position);
        void onImageClick();
    }

    public static ImageFragment newInstance(int imageIndex) {
        Bundle args = new Bundle();
        args.putInt(KEY_IMAGE_INDEX, imageIndex);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imagePosition = getArguments().getInt(KEY_IMAGE_INDEX);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.gallery_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        zoomableImageView = (ZoomableImageView) getView().findViewById(R.id.zoomableImageView);
        zoomableImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryImage.onImageClick();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            galleryImage = (GalleryImage) getActivity();
            galleryImage.requestImageByPosition(zoomableImageView, imagePosition);
        } catch (Exception e) {
            Log.e(TAG, "Error attaching interface", e);
        }
    }
}

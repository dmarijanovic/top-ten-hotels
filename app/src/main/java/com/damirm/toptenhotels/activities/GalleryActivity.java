package com.damirm.toptenhotels.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damirm.toptenhotels.App;
import com.damirm.toptenhotels.R;
import com.damirm.toptenhotels.fragments.ImageFragment;
import com.damirm.toptenhotels.adapters.ImagePageAdapter;
import com.damirm.toptenhotels.utils.PhotoUtil;

public class GalleryActivity extends BaseActivity implements ImageFragment.GalleryImage {

    public static final String KEY_PLACE_ID = "place_id";
    public static final String KEY_SELECTED_IMAGE_POSITION = "selected_image_position";

    private String placeId;
    private int currentImagePosition;

    private ViewPager viewPager;
    private LinearLayout statusContainer;
    private TextView status;
    private FragmentStatePagerAdapter adapter;
    private int imageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        initToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            placeId = extras.getString(KEY_PLACE_ID);
            currentImagePosition = extras.getInt(KEY_SELECTED_IMAGE_POSITION);
        }

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        statusContainer = (LinearLayout) findViewById(R.id.statusContainer);
        status = (TextView) findViewById(R.id.status);

        imageCount = App.get().getBitmapCache().getImageCount(placeId);
        adapter = new ImagePageAdapter(getSupportFragmentManager(), imageCount);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        // hide toolbar when activity starts
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toggleToolbarVisibility();
                    }
                }, 500);
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentImagePosition);
        setStatus(currentImagePosition);
    }

    @Override
    public void requestImageByPosition(ImageView imageView, int position, PhotoUtil.PhotoCallback photoCallback) {
        PhotoUtil.downloadOrGetFromCache(apiClient, imageView, placeId, position, false, true, photoCallback);
    }

    @Override
    public void onImageClick() {
        toggleToolbarVisibility();
    }

    private void toggleToolbarVisibility() {
        Toolbar toolbar = getToolbar();
        boolean isToolbarHidden = toolbar.getTag(R.id.KEY_TOOLBAR_HIDDEN) != null ?
                (Boolean) toolbar.getTag(R.id.KEY_TOOLBAR_HIDDEN) : false;

        if (isToolbarHidden) {
            toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
            toolbar.setTag(R.id.KEY_TOOLBAR_HIDDEN, false);

            statusContainer.setVisibility(View.VISIBLE);
        } else {
            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
            toolbar.setTag(R.id.KEY_TOOLBAR_HIDDEN, true);

            statusContainer.setVisibility(View.INVISIBLE);
        }
    }

    private void setStatus(int position) {
        status.setText((position + 1) + "/" + imageCount);
    }

}

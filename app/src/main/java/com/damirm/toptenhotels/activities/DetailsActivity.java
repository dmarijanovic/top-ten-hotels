package com.damirm.toptenhotels.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.damirm.toptenhotels.R;
import com.damirm.toptenhotels.models.PlaceData;
import com.damirm.toptenhotels.utils.PhotoUtil;

public class DetailsActivity extends BaseActivity {

    private PlaceData placeData;

    private ImageView coverImageView;
    private ImageView teaserImageView1;
    private ImageView teaserImageView2;
    private ImageView teaserImageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        initToolbar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            placeData = (PlaceData) extras.get(PlaceData.class.getName());
        }

        setTitle(placeData.name);

        TextView name = (TextView) findViewById(R.id.name);
        TextView street = (TextView) findViewById(R.id.street);
        TextView city = (TextView) findViewById(R.id.city);
        TextView description = (TextView) findViewById(R.id.description);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        coverImageView = (ImageView) findViewById(R.id.cover_image);
        teaserImageView1 = (ImageView) findViewById(R.id.teaser_image_1);
        teaserImageView2 = (ImageView) findViewById(R.id.teaser_image_2);
        teaserImageView3 = (ImageView) findViewById(R.id.teaser_image_3);

        coverImageView.setOnClickListener(onTeaserImageListener(0));
        teaserImageView1.setOnClickListener(onTeaserImageListener(1));
        teaserImageView2.setOnClickListener(onTeaserImageListener(2));
        teaserImageView3.setOnClickListener(onTeaserImageListener(3));

        name.setText(placeData.name);
        street.setText(placeData.street);
        city.setText(placeData.getCityWithPb());
        description.setText(placeData.description);
        ratingBar.setRating(placeData.rating * 10);

        PhotoUtil.downloadOrGetFromCache(apiClient, coverImageView, placeData.placeId, 0, true);

        PhotoUtil.downloadOrGetFromCache(apiClient, teaserImageView1, placeData.placeId, 1, true);

        PhotoUtil.downloadOrGetFromCache(apiClient, teaserImageView2, placeData.placeId, 2, true);

        PhotoUtil.downloadOrGetFromCache(apiClient, teaserImageView3, placeData.placeId, 3, true);
    }

    private View.OnClickListener onTeaserImageListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GalleryActivity.class);
                Bundle extras = new Bundle();
                extras.putString(GalleryActivity.KEY_PLACE_ID, placeData.placeId);
                extras.putInt(GalleryActivity.KEY_SELECTED_IMAGE_POSITION, position);
                intent.putExtras(extras);

                startActivity(intent);
            }
        };
    }
}

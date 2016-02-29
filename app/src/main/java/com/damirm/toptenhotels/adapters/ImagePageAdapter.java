package com.damirm.toptenhotels.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.damirm.toptenhotels.fragments.ImageFragment;

public class ImagePageAdapter extends FragmentStatePagerAdapter {

    private int imageCount;

    public ImagePageAdapter(FragmentManager fm, int imageCount) {
        super(fm);
        this.imageCount = imageCount;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return imageCount;
    }
}

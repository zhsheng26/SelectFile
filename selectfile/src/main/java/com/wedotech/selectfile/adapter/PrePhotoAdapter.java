package com.wedotech.selectfile.adapter;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wedotech.selectfile.R;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.preview.view.BigImageView;
import com.wedotech.selectfile.preview.view.ProgressPieIndicator;

import java.util.ArrayList;

/**
 * Created by zhsheng on 2016/11/18.
 */

public class PrePhotoAdapter extends PagerAdapter {
    private final ArrayList<Photo> photos;

    public PrePhotoAdapter(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_pic_view, null);
        BigImageView imgView = (BigImageView) view.findViewById(R.id.photo_view_pic);
       /* Glide.with(view.getContext())
                .load(photos.get(position).getPath())
                .thumbnail(0.1f)
                .fitCenter()
                .into(imgView);*/
        imgView.setProgressIndicator(new ProgressPieIndicator());
        imgView.showImage(Uri.parse(photos.get(position).getPath()));
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
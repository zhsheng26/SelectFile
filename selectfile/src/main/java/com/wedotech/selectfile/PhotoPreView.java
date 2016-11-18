package com.wedotech.selectfile;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.wedotech.selectfile.adapter.PrePhotoAdapter;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.preview.BigImageViewer;
import com.wedotech.selectfile.preview.loader.GlideImageLoader;

import java.util.ArrayList;

/**
 * Created by zhsheng on 2016/11/18.
 */

public class PhotoPreView extends ViewPager {

    private ArrayList<Photo> photoArrayList;
    private PrePhotoAdapter prePhotoAdapter;

    public PhotoPreView(Context context) {
        super(context);
        initState();
    }


    public PhotoPreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initState() {
        BigImageViewer.initialize(GlideImageLoader.with(getContext().getApplicationContext()));
        photoArrayList = new ArrayList<>(10);
        prePhotoAdapter = new PrePhotoAdapter(photoArrayList);
        setAdapter(prePhotoAdapter);
    }

    public void putData(ArrayList<Photo> photos) {
        photoArrayList.clear();
        photoArrayList.addAll(photos);
        prePhotoAdapter.notifyDataSetChanged();
    }
}

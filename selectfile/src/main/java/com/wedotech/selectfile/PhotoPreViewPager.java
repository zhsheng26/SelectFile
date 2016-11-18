package com.wedotech.selectfile;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.wedotech.selectfile.adapter.PrePhotoAdapter;
import com.wedotech.selectfile.models.Photo;

import java.util.ArrayList;

/**
 * Created by zhsheng on 2016/11/18.
 */

public class PhotoPreViewPager extends ViewPager {

    private ArrayList<Photo> photoArrayList;
    private PrePhotoAdapter prePhotoAdapter;

    public PhotoPreViewPager(Context context) {
        super(context);
        initState();
    }


    public PhotoPreViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initState() {
        photoArrayList = new ArrayList<>(10);
        prePhotoAdapter = new PrePhotoAdapter(photoArrayList);
        setAdapter(prePhotoAdapter);
    }

    public void putData(ArrayList<Photo> photos) {
        photoArrayList.clear();
        photoArrayList.addAll(photos);
        prePhotoAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}

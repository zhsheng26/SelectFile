package com.wedotech.selectfile;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.wedotech.selectfile.adapter.PhotoSelectedGridAdapter;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.support.OnGridPhotoActionListener;

import java.util.ArrayList;

/**
 * Created by zhsheng on 2016/11/17.
 */

public class PhotoGridView extends RecyclerView {


    private ArrayList<Photo> photoArr;
    private PhotoSelectedGridAdapter adapter;
    private OnGridPhotoActionListener actionListener;

    public PhotoGridView(Context context) {
        super(context);
        setupState();
    }

    public PhotoGridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoGridView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupState();
    }


    private void setupState() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        photoArr = new ArrayList<>(10);
        adapter = new PhotoSelectedGridAdapter(photoArr);
        setAdapter(adapter);
    }

    public void setOnPhotoActionListener(OnGridPhotoActionListener actionListener) {
        adapter.setOnGridPhotoActionListener(actionListener);
    }

    public void putPhotos(ArrayList<Photo> photos) {
        photoArr.clear();
        addPhotos(photos);
    }

    public void addPhoto(Photo photo) {
        photoArr.add(photo);
        adapter.notifyDataSetChanged();
    }

    public void addPhotos(ArrayList<Photo> photos) {
        photoArr.addAll(photos);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<Photo> getSelectedPhoto() {
        return adapter.getSelectedPhotos();
    }
}

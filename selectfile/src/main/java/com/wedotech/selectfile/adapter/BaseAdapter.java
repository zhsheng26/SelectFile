package com.wedotech.selectfile.adapter;

import android.support.v7.widget.RecyclerView;

import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.support.OnPhotoSelectedListener;

import java.util.ArrayList;

/**
 * Created by zhsheng on 2016/11/8.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter {

    protected ArrayList<Photo> photos;
    int maxCount;
    OnPhotoSelectedListener selectedListener;
    ArrayList<Photo> selectedPhotos = new ArrayList<>(9);

    BaseAdapter(ArrayList<Photo> photos) {
        this(photos, 9);
    }

    BaseAdapter(ArrayList<Photo> photos, int maxCount) {
        if (photos == null) {
            this.photos = new ArrayList<>();
        } else {
            this.photos = photos;
        }
        this.maxCount = maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public void putSelectedPhotos(ArrayList<Photo> photos) {
        if (photos != null && photos.size() > 0) {
            selectedPhotos.addAll(photos);
        }
    }

    public ArrayList<Photo> getSelectedPhotos() {
        return selectedPhotos;
    }

    public void setOnPhotoSelectedListener(OnPhotoSelectedListener selectedListener) {
        selectedPhotos.clear();
        this.selectedListener = selectedListener;
    }
}

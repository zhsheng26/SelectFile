package com.wedotech.selectfile;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.wedotech.selectfile.adapter.BaseAdapter;
import com.wedotech.selectfile.adapter.PhotoNoGroupAdapter;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.support.OnPhotoSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhsheng on 2016/11/5.
 */

public class PhotoDisplayView extends RecyclerView {

    private int column = 4;
    private boolean groupByDate = false;
    private BaseAdapter adapter;
    private ArrayList<Photo> photos;
    private OnPhotoSelectedListener selectedListener;

    public PhotoDisplayView(Context context) {
        super(context);
        setupView();
    }

    public PhotoDisplayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoDisplayView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SelectFile, defStyle, 0);
        groupByDate = typedArray.getBoolean(R.styleable.SelectFile_groupByDate, false);
        column = typedArray.getInteger(R.styleable.SelectFile_column, 4);
        typedArray.recycle();
        setupView();
    }

    private void setupView() {
        setLayoutManager(new GridLayoutManager(getContext(), column));
        photos = new ArrayList<>();
        if (!groupByDate) {
            adapter = new PhotoNoGroupAdapter(photos);
            adapter.setOnPhotoSelectedListener(new OnPhotoSelectedListener() {
                @Override
                public void photoSelected(Photo photo, int selectedCount) {
                    if (selectedListener != null)
                        selectedListener.photoSelected(photo, selectedCount);
                }
            });
        } else {
            //adapter = new PhotoDateGroupAdapter(photos, this);
        }
        setAdapter(adapter);
    }

    public void setupPhotos(List<Photo> photos) {
        if (photos == null) return;
        this.photos.clear();
        this.photos.addAll(photos);
        adapter.notifyDataSetChanged();
    }

    public void setOnPhotoSelectedListener(OnPhotoSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

}

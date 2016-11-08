package com.wedotech.selectfile;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.wedotech.selectfile.adapter.PhotoNoGroupAdapter;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.support.StickyHeaderHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhsheng on 2016/11/5.
 */

public class PhotoDisplayView extends RecyclerView implements StickyHeaderHelper.OnStickyHeaderChangeListener {

    private int column;
    private boolean groupByDate;
    private PhotoNoGroupAdapter noGroupAdapter;
    private List<Photo> photos;

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
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        photos = new ArrayList<>();
        noGroupAdapter = new PhotoNoGroupAdapter(photos, this);
        setAdapter(noGroupAdapter);
    }

    public void setupPhotos(List<Photo> photos) {
        if (photos == null) return;
        this.photos.clear();
        this.photos.addAll(photos);
        noGroupAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStickyHeaderChange(int sectionIndex) {

    }
}

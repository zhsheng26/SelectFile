package com.wedotech.selectfile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.wedotech.selectfile.adapter.PhotoDirectoryAdapter;
import com.wedotech.selectfile.cursors.loadercallbacks.FileResultCallback;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.models.PhotoDirectory;
import com.wedotech.selectfile.support.FilePickerConst;
import com.wedotech.selectfile.support.MediaStoreHelper;
import com.wedotech.selectfile.support.OnSelectDirListener;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhsheng on 2016/10/26.
 */

public class AlbumView extends RecyclerView implements OnSelectDirListener {

    private List<PhotoDirectory> directories = new ArrayList<>(50);
    private PhotoDirectoryAdapter directoryAdapter;
    private OnSelectDirListener dirListener;

    public AlbumView(Context context) {
        super(context);
        setupView();
    }

    public AlbumView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlbumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupView();
    }

    private void setupView() {
        addItemDecoration(new HorizontalDividerItemDecoration
                .Builder(getContext())
                .margin(20, 20)
                .size(2)
                .build());
        setLayoutManager(new LinearLayoutManager(getContext()));
        directoryAdapter = new PhotoDirectoryAdapter(directories);
        setAdapter(directoryAdapter);
        directoryAdapter.setOnSelectDirListener(this);
    }

    public void showPhotos(FragmentActivity activity) {
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(FilePickerConst.EXTRA_SHOW_GIF, false);
        MediaStoreHelper.getPhotoDirs(activity, mediaStoreArgs, new FileResultCallback<PhotoDirectory>() {
            @Override
            public void onResultCallback(List<PhotoDirectory> files) {
                dealPhotos(files);
            }
        });
    }

    private void dealPhotos(List<PhotoDirectory> files) {
        directories.clear();
        directories.addAll(files);
        directoryAdapter.notifyPhotoDataSetChange();
    }

    public void setOnSelectDirListener(OnSelectDirListener dirListener) {
        this.dirListener = dirListener;
    }


    @Override
    public void onSelectDir(List<Photo> photos) {
        if (dirListener != null) dirListener.onSelectDir(photos);
    }
}

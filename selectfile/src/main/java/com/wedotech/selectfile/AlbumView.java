package com.wedotech.selectfile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wedotech.selectfile.cursors.loadercallbacks.FileResultCallback;
import com.wedotech.selectfile.models.PhotoDirectory;

import java.util.List;

/**
 * Created by zhsheng on 2016/10/26.
 */

public class AlbumView extends FrameLayout {

    private RecyclerView recyclerView;

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
        recyclerView = new RecyclerView(getContext());
        addView(recyclerView);

    }

    public void showPhotos(FragmentActivity activity) {
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(FilePickerConst.EXTRA_SHOW_GIF, false);
        MediaStoreHelper.getPhotoDirs(activity, mediaStoreArgs, new FileResultCallback<PhotoDirectory>() {
            @Override
            public void onResultCallback(List<PhotoDirectory> files) {

            }
        });
    }
}
package com.wedotech.selectfile;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.wedotech.selectfile.models.Photo;

import java.util.List;

/**
 * Created by zhsheng on 2016/11/5.
 */

public class PhotoDisplayView extends RecyclerView {
    public PhotoDisplayView(Context context) {
        super(context);
        setupView();
    }

    public PhotoDisplayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoDisplayView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupView();
    }

    public void setupPhotos(List<Photo> photos) {

    }

    private void setupView() {

    }

}

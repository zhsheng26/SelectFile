package com.wedotech.selectfile.support;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by zhsheng on 2016/10/31.
 */

public class ImageLoader {
    public static void display(Context context, ImageView imageView, Object o) {
        Glide.with(context)
                .load(o)
                .into(imageView);
    }
}

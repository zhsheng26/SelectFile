package com.wedotech.selectfile.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wedotech.selectfile.R;
import com.wedotech.selectfile.models.BaseFile;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.support.ImageLoader;
import com.wedotech.selectfile.support.StickyHeaderHelper;

import java.util.List;

/**
 * Created by zhsheng on 2016/10/26.
 */

public class PhotoNoGroupAdapter extends BaseRecyclerAdapter {

    public PhotoNoGroupAdapter(@Nullable List<Photo> items, StickyHeaderHelper.OnStickyHeaderChangeListener stickyHeaderChangeListener) {
        super(items, stickyHeaderChangeListener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_photo, null);
        return new PhotoViewHolder(itemView, this, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseFile item = getItem(position);
        PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
        ImageLoader.display(getRecyclerView().getContext(), photoViewHolder.ivPhoto, item.getPath());
    }

    public class PhotoViewHolder extends PhotoItemViewHolder {
        ImageView ivPhoto;

        public PhotoViewHolder(View view, BaseRecyclerAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
        }
    }
}

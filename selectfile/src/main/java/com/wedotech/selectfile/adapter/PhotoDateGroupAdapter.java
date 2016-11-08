package com.wedotech.selectfile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wedotech.selectfile.R;
import com.wedotech.selectfile.models.BaseFile;
import com.wedotech.selectfile.support.FilePickerConst;
import com.wedotech.selectfile.support.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zhsheng on 2016/10/26.
 */

public class PhotoDateGroupAdapter extends BaseAdapter {
    public PhotoDateGroupAdapter(ArrayList<BaseFile> photos) {
        super(photos);
    }

    public PhotoDateGroupAdapter(ArrayList<BaseFile> photos, int maxCount) {
        super(photos, maxCount);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FilePickerConst.PHOTO_PICKER) {
            return new PhotoViewHolder(View.inflate(parent.getContext(), R.layout.item_photo, null));
        } else {
            return new HeaderTitleViewHolder(View.inflate(parent.getContext(), R.layout.item_header_title, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        BaseFile baseFile = photos.get(position);
        if (itemViewType == FilePickerConst.PHOTO_PICKER) {
            PhotoViewHolder photoViewHolder = (PhotoViewHolder) holder;
            ImageLoader.display(holder.itemView.getContext(), photoViewHolder.ivPhoto, baseFile.getPath());
        } else {
            HeaderTitleViewHolder titleViewHolder = (HeaderTitleViewHolder) holder;
            titleViewHolder.tvTitle.setText(baseFile.getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return photos.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
        }
    }

    private class HeaderTitleViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvSelectAll;

        public HeaderTitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSelectAll = (TextView) itemView.findViewById(R.id.tv_select_all);
        }
    }

}

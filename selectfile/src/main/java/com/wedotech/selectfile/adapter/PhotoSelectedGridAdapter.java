package com.wedotech.selectfile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wedotech.selectfile.R;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.support.FilePickerConst;
import com.wedotech.selectfile.support.ImageLoader;
import com.wedotech.selectfile.support.OnGridPhotoActionListener;

import java.util.ArrayList;

/**
 * Created by zhsheng on 2016/11/17.
 */

public class PhotoSelectedGridAdapter extends BaseAdapter {
    private OnGridPhotoActionListener actionListener;

    public PhotoSelectedGridAdapter(ArrayList<Photo> photos) {
        super(photos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FilePickerConst.PHOTO_PICKER) {
            return new GridItemPhotoHolder(View.inflate(parent.getContext(), R.layout.item_grid_photo, null));
        } else {
            return new GridItemAddHolder(View.inflate(parent.getContext(), R.layout.item_grid_add, null));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == FilePickerConst.PHOTO_PICKER) {
            Photo photo = photos.get(position);
            GridItemPhotoHolder photoHolder = (GridItemPhotoHolder) holder;
            photoHolder.setPhoto(photo);
        } else {
            GridItemAddHolder addHolder = (GridItemAddHolder) holder;
            addHolder.addAction.setImageResource(R.drawable.ic_add_photo);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int size = photos.size();
        if (size < 9) {
            if (position < size) {
                return FilePickerConst.PHOTO_PICKER;
            } else {
                return FilePickerConst.TITLE_ADD;
            }
        } else {
            return FilePickerConst.PHOTO_PICKER;
        }
    }

    @Override
    public int getItemCount() {
        return photos.size() < 9 ? photos.size() + 1 : 9;
    }

    @Override
    public ArrayList<Photo> getSelectedPhotos() {
        return photos;
    }

    public void setOnGridPhotoActionListener(OnGridPhotoActionListener onGridPhotoActionListener) {
        this.actionListener = onGridPhotoActionListener;
    }

    private class GridItemAddHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView addAction;

        public GridItemAddHolder(View itemView) {
            super(itemView);
            addAction = (ImageView) itemView.findViewById(R.id.iv_add_photo);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (actionListener != null) actionListener.onClickAdd();
        }
    }

    private class GridItemPhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivPhoto;
        ImageView ivDelete;
        private Photo photoObj;

        public GridItemPhotoHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            ivPhoto.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
        }

        public void setPhoto(Photo photo) {
            this.photoObj = photo;
            ImageLoader.display(itemView.getContext(), ivPhoto, photo.getPath());
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.iv_photo) {
                if (actionListener != null) actionListener.onClickPhoto(photoObj);
            } else {
                photos.remove(photoObj);
                notifyItemRemoved(getAdapterPosition());
                if (actionListener != null) actionListener.onClickDelete(photoObj);
            }
        }
    }
}

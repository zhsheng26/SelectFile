package com.wedotech.selectfile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.wedotech.selectfile.R;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.support.ImageLoader;

import java.util.ArrayList;

/**
 * Created by zhsheng on 2016/10/26.
 */

public class PhotoNoGroupAdapter extends BaseAdapter {

    public PhotoNoGroupAdapter(ArrayList<Photo> photos) {
        super(photos);
    }

    public PhotoNoGroupAdapter(ArrayList<Photo> photos, int maxCount) {
        super(photos, maxCount);
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_photo, null);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
        viewHolder.setItemData(photos.get(position));

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }


    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivPhoto;
        CheckBox checkBox;
        private Photo photoObj;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_sel);
            checkBox.setOnClickListener(this);
        }

        public void setItemData(Photo photo) {
            this.photoObj = photo;
            checkBox.setChecked(selectedPhotos.contains(photo));
            ImageLoader.display(itemView.getContext(), ivPhoto, photoObj.getPath());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.cb_sel) {
                boolean isChecked = checkBox.isChecked();
                if (isChecked && selectedPhotos.size() + 1 > maxCount) {
                    Toast.makeText(v.getContext(), "最多只能选" + maxCount + "张照片", Toast.LENGTH_SHORT).show();
                    checkBox.setChecked(false);
                    return;
                }
                if (isChecked) {
                    selectedPhotos.add(photoObj);
                } else {
                    selectedPhotos.remove(photoObj);
                }
                if (selectedListener != null) {
                    selectedListener.photoSelected(photoObj, selectedPhotos.size());
                }

            } else if (v.getId() == R.id.iv_photo) {
                //进入大图预览
            }
        }
    }


}

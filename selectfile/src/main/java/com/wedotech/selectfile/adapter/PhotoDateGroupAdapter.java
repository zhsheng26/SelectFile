package com.wedotech.selectfile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wedotech.selectfile.R;
import com.wedotech.selectfile.models.BaseFile;
import com.wedotech.selectfile.support.FilePickerConst;
import com.wedotech.selectfile.support.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhsheng on 2016/10/26.
 */

public class PhotoDateGroupAdapter extends BaseAdapter {
    private ArrayList<String> selectedGroup = new ArrayList<>(10);

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
            photoViewHolder.setItemData(baseFile);
        } else {
            HeaderTitleViewHolder titleViewHolder = (HeaderTitleViewHolder) holder;
            titleViewHolder.tvTitle.setText(baseFile.getTitle());
            titleViewHolder.tvSelectAll.setText(selectedGroup.contains(String.valueOf(position)) ? "取消全选" : "全选");
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

    private class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivPhoto;
        CheckBox checkBox;
        private BaseFile photoObj;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_sel);
            checkBox.setOnClickListener(this);
        }

        public void setItemData(BaseFile photo) {
            this.photoObj = photo;
            checkBox.setChecked(selectedPhotos.contains(photo));
            ImageLoader.display(itemView.getContext(), ivPhoto, photoObj.getPath());
        }


        @Override
        public void onClick(View v) {
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
            int focusPosition = getAdapterPosition();
            //找到所在组的group
            int headerPosition = findHeaderByFocusPosition(focusPosition);
            List<BaseFile> files = findGroupPhotoByHeaderPosition(headerPosition);
            if (selectedPhotos.containsAll(files)) {
                selectedGroup.add(String.valueOf(headerPosition));
            } else {
                selectedGroup.remove(String.valueOf(headerPosition));
            }
            notifyItemChanged(headerPosition);
        }
    }

    private class HeaderTitleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvSelectAll;

        public HeaderTitleViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvSelectAll = (TextView) itemView.findViewById(R.id.tv_select_all);
            tvSelectAll.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            List<BaseFile> groupPhotos = findGroupPhotoByHeaderPosition(position);
            int size = groupPhotos.size();
            TextView textView = (TextView) v;
            String txt = textView.getText().toString();
            boolean checkAll = "全选".equals(txt);
            if (checkAll && selectedPhotos.size() + size > maxCount) {
                Toast.makeText(v.getContext(), "最多只能选" + maxCount + "张照片", Toast.LENGTH_SHORT).show();
                return;
            }
            textView.setText(checkAll ? "取消全选" : "全选");
            if (checkAll) {
                selectedPhotos.addAll(groupPhotos);
                selectedGroup.add(String.valueOf(position));
            } else {
                selectedPhotos.removeAll(groupPhotos);
                selectedGroup.remove(String.valueOf(position));
            }
            notifyItemRangeChanged(position + 1, size);
        }
    }

    private int findHeaderByFocusPosition(int focusPosition) {
        int firstIndex = 0;
        for (int i = focusPosition; i >= 0; i--) {
            BaseFile baseFile = photos.get(i);
            int type = baseFile.getType();
            if (type == FilePickerConst.TITLE_HEADER) {
                firstIndex = i;
                break;
            }
        }
        return firstIndex;
    }

    private List<BaseFile> findGroupPhotoByHeaderPosition(int headerPosition) {
        ArrayList<BaseFile> baseFiles = new ArrayList<>();
        for (int i = headerPosition + 1; i < photos.size(); i++) {
            BaseFile baseFile = photos.get(i);
            if (baseFile.getType() == FilePickerConst.PHOTO_PICKER) {
                baseFiles.add(baseFile);
            } else {
                break;
            }
        }
        return baseFiles;
    }

}

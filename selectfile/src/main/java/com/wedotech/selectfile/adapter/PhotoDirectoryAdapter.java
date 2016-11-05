package com.wedotech.selectfile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wedotech.selectfile.R;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.models.PhotoDirectory;
import com.wedotech.selectfile.support.ImageLoader;
import com.wedotech.selectfile.support.OnSelectDirListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhsheng on 2016/10/26.
 */

public class PhotoDirectoryAdapter extends RecyclerView.Adapter<PhotoDirectoryAdapter.DirectoryViewHolder> {
    private final List<PhotoDirectory> directs;
    private Context context;
    private int sum;
    private OnSelectDirListener dirListener;
    private ArrayList<Photo> selectPhotos = new ArrayList<>();

    public PhotoDirectoryAdapter(List<PhotoDirectory> directories) {
        this.directs = directories;
        getPhotoCount();
    }

    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = View.inflate(context, R.layout.item_directory, null);
        return new DirectoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DirectoryViewHolder holder, int position) {
        if (directs.size() == 0) return;
        if (position == 0) {
            holder.tvDirectoryName.setText("全部");
            ImageLoader.display(context, holder.ivDirectory, directs.get(0).getCoverPath());
            holder.tvDirectoryPicCount.setText(String.valueOf(sum));
        } else {
            PhotoDirectory directory = directs.get(position - 1);
            holder.tvDirectoryName.setText(directory.getName());
            ImageLoader.display(context, holder.ivDirectory, directory.getCoverPath());
            holder.tvDirectoryPicCount.setText(String.valueOf(directory.getPhotoPaths().size()));
        }
    }

    @Override
    public int getItemCount() {
        return directs.size() + 1;
    }

    private int getPhotoCount() {
        sum = 0;
        for (PhotoDirectory photoDirectory : directs) {
            sum += photoDirectory.getPhotoPaths().size();
        }
        return sum;
    }

    public void notifyPhotoDataSetChange() {
        getPhotoCount();
        notifyDataSetChanged();
    }

    public void setOnSelectDirListener(OnSelectDirListener dirListener) {
        this.dirListener = dirListener;
    }

    class DirectoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivDirectory;
        TextView tvDirectoryName;
        TextView tvDirectoryPicCount;

        public DirectoryViewHolder(View itemView) {
            super(itemView);
            ivDirectory = (ImageView) itemView.findViewById(R.id.iv_directory_cover);
            tvDirectoryName = (TextView) itemView.findViewById(R.id.tv_directory_name);
            tvDirectoryPicCount = (TextView) itemView.findViewById(R.id.tv_directory_pic_count);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (dirListener == null) return;
            selectPhotos.clear();
            int position = getAdapterPosition();
            if (position == 0) {
                for (PhotoDirectory directory : directs) {
                    selectPhotos.addAll(directory.getPhotos());
                }
            } else {
                PhotoDirectory photoDirectory = directs.get(position - 1);
                selectPhotos.addAll(photoDirectory.getPhotos());
            }
            dirListener.onSelectDir(selectPhotos);
        }
    }
}

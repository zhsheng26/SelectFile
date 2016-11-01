package com.wedotech.selectfile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wedotech.selectfile.models.PhotoDirectory;

import java.util.List;

/**
 * Created by zhsheng on 2016/10/26.
 */

public class PhotoDirectoryAdapter extends RecyclerView.Adapter<PhotoDirectoryAdapter.DirectoryViewHolder> {
    private final List<PhotoDirectory> directs;
    private Context context;
    private int sum;

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

    class DirectoryViewHolder extends RecyclerView.ViewHolder {

        ImageView ivDirectory;
        TextView tvDirectoryName;
        TextView tvDirectoryPicCount;

        public DirectoryViewHolder(View itemView) {
            super(itemView);
            ivDirectory = (ImageView) itemView.findViewById(R.id.iv_directory_cover);
            tvDirectoryName = (TextView) itemView.findViewById(R.id.tv_directory_name);
            tvDirectoryPicCount = (TextView) itemView.findViewById(R.id.tv_directory_pic_count);
        }
    }
}

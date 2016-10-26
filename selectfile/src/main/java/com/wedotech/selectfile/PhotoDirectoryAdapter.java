package com.wedotech.selectfile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wedotech.selectfile.models.PhotoDirectory;

import java.util.List;

/**
 * Created by zhsheng on 2016/10/26.
 */

public class PhotoDirectoryAdapter extends RecyclerView.Adapter<PhotoDirectoryAdapter.DirectoryViewHolder> {
    private final List<PhotoDirectory> directs;

    public PhotoDirectoryAdapter(List<PhotoDirectory> directories) {
        this.directs = directories;
    }

    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DirectoryViewHolder(new TextView(parent.getContext()));
    }

    @Override
    public void onBindViewHolder(DirectoryViewHolder holder, int position) {
        TextView itemView = (TextView) holder.itemView;
        itemView.setText(directs.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return directs.size();
    }

    class DirectoryViewHolder extends RecyclerView.ViewHolder {

        public DirectoryViewHolder(View itemView) {
            super(itemView);
        }
    }
}

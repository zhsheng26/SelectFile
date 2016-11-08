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

public class PhotoDateGroupAdapter extends BaseRecyclerAdapter {
    public PhotoDateGroupAdapter(@Nullable List<Photo> items, StickyHeaderHelper.OnStickyHeaderChangeListener stickyHeaderChangeListener) {
        super(items, stickyHeaderChangeListener);
        registerAdapterDataObserver(new AdapterDataObserver());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_photo, null);
        return new PhotoDateGroupAdapter.PhotoViewHolder(itemView, this, true);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseFile item = getItem(position);
        PhotoNoGroupAdapter.PhotoViewHolder photoViewHolder = (PhotoNoGroupAdapter.PhotoViewHolder) holder;
        ImageLoader.display(getRecyclerView().getContext(), photoViewHolder.ivPhoto, item.getPath());
    }

    public class PhotoViewHolder extends PhotoItemViewHolder {
        ImageView ivPhoto;

        public PhotoViewHolder(View view, BaseRecyclerAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            ivPhoto = (ImageView) view.findViewById(R.id.iv_photo);
        }
    }

    private class AdapterDataObserver extends RecyclerView.AdapterDataObserver {


        private void updateOrClearHeader() {
            if (mStickyHeaderHelper != null) {
                mStickyHeaderHelper.updateOrClearHeader(true);
            }
        }

        /* Triggered by notifyDataSetChanged() */
        @Override
        public void onChanged() {
            updateOrClearHeader();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            updateOrClearHeader();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            updateOrClearHeader();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            updateOrClearHeader();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            updateOrClearHeader();
        }
    }
}

/*
 * Copyright 2016 Martin Guillon & Davide Steduto (Hyper-Optimized for FlexibleAdapter project)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wedotech.selectfile.support;

import android.animation.Animator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.wedotech.selectfile.adapter.BaseRecyclerAdapter;
import com.wedotech.selectfile.adapter.PhotoItemViewHolder;
import com.wedotech.selectfile.models.HeaderTitle;

public class StickyHeaderHelper extends OnScrollListener {

    private static final String TAG = BaseRecyclerAdapter.class.getSimpleName();

    private BaseRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ViewGroup mStickyHolderLayout;
    private PhotoItemViewHolder mStickyHeaderViewHolder;
    private OnStickyHeaderChangeListener mStickyHeaderChangeListener;
    private int mHeaderPosition = RecyclerView.NO_POSITION;


    public StickyHeaderHelper(BaseRecyclerAdapter adapter,
                              OnStickyHeaderChangeListener stickyHeaderChangeListener) {
        mAdapter = adapter;
        mStickyHeaderChangeListener = stickyHeaderChangeListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        updateOrClearHeader(false);
    }

    public boolean isAttachedToRecyclerView() {
        return mRecyclerView != null;
    }

    public void attachToRecyclerView(RecyclerView parent) {
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(this);
            clearHeader();
        }
        mRecyclerView = parent;
        if (mRecyclerView != null) {
            mRecyclerView.addOnScrollListener(this);
            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {
                    initStickyHeadersHolder();
                }
            });
        }
    }

    public void detachFromRecyclerView(RecyclerView parent) {
        if (mRecyclerView == parent) {
            mRecyclerView.removeOnScrollListener(this);
            mRecyclerView = null;
            mStickyHolderLayout.animate().setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    clearHeader();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            mStickyHolderLayout.animate().alpha(0).start();
            if (BaseRecyclerAdapter.DEBUG) Log.i(TAG, "StickyHolderLayout detached");
        }
    }

    private void initStickyHeadersHolder() {
        //Initialize Holder Layout and show sticky header if exists already
        mStickyHolderLayout = mAdapter.getStickySectionHeadersHolder();
        if (mStickyHolderLayout != null) {
            if (mStickyHolderLayout.getLayoutParams() == null) {
                throw new IllegalStateException("The ViewGroup provided, doesn't have LayoutParams correctly set, please initialize the ViewGroup accordingly");
            }
            mStickyHolderLayout.setClipToPadding(false);
            mStickyHolderLayout.setAlpha(0);
            updateOrClearHeader(false);
            mStickyHolderLayout.animate().alpha(1).start();
            if (BaseRecyclerAdapter.DEBUG) Log.i(TAG, "StickyHolderLayout initialized");
        } else {
            Log.w(TAG, "WARNING! ViewGroup for Sticky Headers unspecified! You must include @layout/sticky_header_layout or implement FlexibleAdapter.getStickySectionHeadersHolder() method");
        }
    }

    public boolean hasStickyHeaderTranslated(int position) {
        RecyclerView.ViewHolder vh = mRecyclerView.findViewHolderForAdapterPosition(position);
        return vh != null && (vh.itemView.getX() < 0 || vh.itemView.getY() < 0);
    }

    private void onStickyHeaderChange(int sectionIndex) {
        if (mStickyHeaderChangeListener != null) {
            mStickyHeaderChangeListener.onStickyHeaderChange(sectionIndex);
        }
    }

    public void updateOrClearHeader(boolean updateHeaderContent) {
        if (mStickyHolderLayout == null || mRecyclerView == null || mRecyclerView.getChildCount() == 0) {
            clearHeader();
            return;
        }
        int firstHeaderPosition = getHeaderPosition(RecyclerView.NO_POSITION);
        if (firstHeaderPosition >= 0 && firstHeaderPosition < mAdapter.getItemCount()) {
            updateHeader(firstHeaderPosition, updateHeaderContent);
        } else {
            clearHeader();
        }
    }

    private void updateHeader(int headerPosition, boolean updateHeaderContent) {
        // Check if there is a new header to be sticky
        if (mHeaderPosition != headerPosition) {
            mHeaderPosition = headerPosition;
            PhotoItemViewHolder holder = getHeaderViewHolder(headerPosition);
            if (BaseRecyclerAdapter.DEBUG)
                Log.d(TAG, "swapHeader newHeaderPosition=" + mHeaderPosition);
            swapHeader(holder);
        } else if (updateHeaderContent && mStickyHeaderViewHolder != null) {
            mAdapter.onBindViewHolder(mStickyHeaderViewHolder, mHeaderPosition);
            ensureHeaderParent();
        }
        translateHeader();
    }

    private void translateHeader() {
        if (mStickyHeaderViewHolder == null) return;

        int headerOffsetX = 0, headerOffsetY = 0;

        //Search for the position where the next header item is found and take the new offset
        for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
            final View nextChild = mRecyclerView.getChildAt(i);
            if (nextChild != null) {
                int adapterPos = mRecyclerView.getChildAdapterPosition(nextChild);
                int nextHeaderPosition = getHeaderPosition(adapterPos);
                if (mHeaderPosition != nextHeaderPosition) {
                    if (Utils.getOrientation(mRecyclerView.getLayoutManager()) == OrientationHelper.HORIZONTAL) {
                        if (nextChild.getLeft() > 0) {
                            int headerWidth = mStickyHolderLayout.getMeasuredWidth();
                            headerOffsetX = Math.min(nextChild.getLeft() - headerWidth, 0);
                            if (headerOffsetX < 0) break;
                        }
                    } else {
                        if (nextChild.getTop() > 0) {
                            int headerHeight = mStickyHolderLayout.getMeasuredHeight();
                            headerOffsetY = Math.min(nextChild.getTop() - headerHeight, 0);
                            if (headerOffsetY < 0) break;
                        }
                    }
                }
            }
        }
        //Fix to remove unnecessary shadow
        //ViewCompat.setElevation(mStickyHeaderViewHolder.getContentView(), 0f);
        //Apply translation
        mStickyHolderLayout.setTranslationX(headerOffsetX);
        mStickyHolderLayout.setTranslationY(headerOffsetY);
        //Log.v(TAG, "TranslationX=" + headerOffsetX + " TranslationY=" + headerOffsetY);
    }

    private void swapHeader(PhotoItemViewHolder newHeader) {
        if (mStickyHeaderViewHolder != null) {
            resetHeader(mStickyHeaderViewHolder);
        }
        mStickyHeaderViewHolder = newHeader;
        if (mStickyHeaderViewHolder != null) {
            mStickyHeaderViewHolder.setIsRecyclable(false);
            ensureHeaderParent();
        }
        onStickyHeaderChange(mHeaderPosition);
    }

    private void ensureHeaderParent() {
        final View view = mStickyHeaderViewHolder.getContentView();
        //#121 - Make sure the measured height (width for horizontal layout) is kept if
        // WRAP_CONTENT has been set for the Header View
        mStickyHeaderViewHolder.itemView.getLayoutParams().width = view.getMeasuredWidth();
        mStickyHeaderViewHolder.itemView.getLayoutParams().height = view.getMeasuredHeight();
        //#139 - Copy xml params instead of Measured params
        ViewGroup.LayoutParams params = mStickyHolderLayout.getLayoutParams();
        params.width = view.getLayoutParams().width;
        params.height = view.getLayoutParams().height;
        removeViewFromParent(view);
        mStickyHolderLayout.setClipToPadding(false);
        mStickyHolderLayout.addView(view);
    }

    public void clearHeader() {
        if (mStickyHeaderViewHolder != null) {
            if (BaseRecyclerAdapter.DEBUG) Log.d(TAG, "clearHeader");
            resetHeader(mStickyHeaderViewHolder);
            mStickyHolderLayout.setAlpha(1);
            mStickyHeaderViewHolder = null;
            mHeaderPosition = RecyclerView.NO_POSITION;
            onStickyHeaderChange(mHeaderPosition);
        }
    }

    private void resetHeader(PhotoItemViewHolder header) {
        final View view = header.getContentView();
        removeViewFromParent(view);
        //Reset transformation on removed header
        view.setTranslationX(0);
        view.setTranslationY(0);
        mStickyHeaderViewHolder.itemView.setVisibility(View.VISIBLE);
        if (!header.itemView.equals(view))
            ((ViewGroup) header.itemView).addView(view);
        header.setIsRecyclable(true);
    }

    private static void removeViewFromParent(final View view) {
        final ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
    }

    @SuppressWarnings("unchecked")
    private int getHeaderPosition(int adapterPosHere) {
        if (adapterPosHere == RecyclerView.NO_POSITION) {
            //Fix to display correct sticky header (especially after the searchText is cleared out)
            if (mRecyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                adapterPosHere = ((StaggeredGridLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPositions(null)[0];
            } else {
                adapterPosHere = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
            }
            if (adapterPosHere == 0 && !hasStickyHeaderTranslated(0)) {
                return RecyclerView.NO_POSITION;
            }
        }
        HeaderTitle header = mAdapter.getSectionHeader(adapterPosHere);
        //Header cannot be sticky if it's also an Expandable in collapsed status, RV will raise an exception
        if (header == null) {
            return RecyclerView.NO_POSITION;
        }
        return mAdapter.getGlobalPositionOf(header);
    }

    /**
     * Gets the header view for the associated header position. If it doesn't exist yet, it will
     * be created, measured, and laid out.
     *
     * @param position the adapter position to get the header view
     * @return ViewHolder of type FlexibleViewHolder of the associated header position
     */
    @SuppressWarnings("unchecked")
    private PhotoItemViewHolder getHeaderViewHolder(int position) {
        //Find existing ViewHolder
        PhotoItemViewHolder holder = (PhotoItemViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (holder == null) {
            //Create and binds a new ViewHolder
            holder = (PhotoItemViewHolder) mAdapter.createViewHolder(mRecyclerView, mAdapter.getItemViewType(position));
            mAdapter.bindViewHolder(holder, position);

            //Restore the Adapter position
            holder.setBackupPosition(position);

            //Calculate width and height
            int widthSpec;
            int heightSpec;
            if (Utils.getOrientation(mRecyclerView.getLayoutManager()) == OrientationHelper.VERTICAL) {
                widthSpec = View.MeasureSpec.makeMeasureSpec(mRecyclerView.getWidth(), View.MeasureSpec.EXACTLY);
                heightSpec = View.MeasureSpec.makeMeasureSpec(mRecyclerView.getHeight(), View.MeasureSpec.UNSPECIFIED);
            } else {
                widthSpec = View.MeasureSpec.makeMeasureSpec(mRecyclerView.getWidth(), View.MeasureSpec.UNSPECIFIED);
                heightSpec = View.MeasureSpec.makeMeasureSpec(mRecyclerView.getHeight(), View.MeasureSpec.EXACTLY);
            }

            //Measure and Layout the stickyView
            final View headerView = holder.itemView;
            int childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    mRecyclerView.getPaddingLeft() + mRecyclerView.getPaddingRight(),
                    headerView.getLayoutParams().width);
            int childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    mRecyclerView.getPaddingTop() + mRecyclerView.getPaddingBottom(),
                    headerView.getLayoutParams().height);

            headerView.measure(childWidth, childHeight);
            headerView.layout(0, 0, headerView.getMeasuredWidth(), headerView.getMeasuredHeight());
        }
        return holder;
    }

    /**
     * @since 05/03/2016
     */
    public interface OnStickyHeaderChangeListener {
        /**
         * Called when the current sticky header changed.
         *
         * @param sectionIndex the position of header, -1 if no header is sticky
         * @since 5.0.0-b1
         */
        void onStickyHeaderChange(int sectionIndex);
    }
}
package com.wedotech.selectfile.adapter;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wedotech.selectfile.R;
import com.wedotech.selectfile.models.BaseFile;
import com.wedotech.selectfile.models.HeaderTitle;
import com.wedotech.selectfile.support.StickyHeaderHelper;
import com.wedotech.selectfile.support.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhsheng on 2016/11/8.
 */

public class BaseRecyclerAdapter<T extends BaseFile> extends RecyclerView.Adapter {

    public static final boolean DEBUG = false;
    private final List<T> mItems;
    private RecyclerView recyclerView;
    private boolean headersShown = false;
    protected StickyHeaderHelper mStickyHeaderHelper;
    private boolean headersSticky = true;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    public StickyHeaderHelper.OnStickyHeaderChangeListener stickyHeaderChangeListener;

    public BaseRecyclerAdapter(@Nullable List<T> items, StickyHeaderHelper.OnStickyHeaderChangeListener stickyHeaderChangeListener) {
        if (items == null) {
            mItems = new ArrayList<>();
        } else {
            mItems = items;
        }
        this.stickyHeaderChangeListener = stickyHeaderChangeListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }


    public final T getItem(@IntRange(from = 0) int position) {
        if (position < 0 || position >= mItems.size()) return null;
        return mItems.get(position);
    }


    @Override
    public long getItemId(int position) {
        T item = getItem(position);
        return item != null ? item.hashCode() : RecyclerView.NO_ID;
    }

    @Override
    public final int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    private void setStickyHeaders(final boolean sticky) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                // Add or Remove the sticky headers
                if (sticky) {
                    headersSticky = true;
                    if (mStickyHeaderHelper == null)
                        mStickyHeaderHelper = new StickyHeaderHelper(BaseRecyclerAdapter.this, stickyHeaderChangeListener);
                    if (!mStickyHeaderHelper.isAttachedToRecyclerView())
                        mStickyHeaderHelper.attachToRecyclerView(recyclerView);
                } else if (mStickyHeaderHelper != null) {
                    headersSticky = false;
                    mStickyHeaderHelper.detachFromRecyclerView(recyclerView);
                    mStickyHeaderHelper = null;
                }
            }
        });
    }

    /**
     * {@inheritDoc}
     * <p>Attaches the StickyHeaderHelper from the RecyclerView when necessary</p>
     *
     * @since 5.0.0-b6
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
        if (mStickyHeaderHelper != null && headersShown) {
            mStickyHeaderHelper.attachToRecyclerView(recyclerView);
        }
    }

    /**
     * {@inheritDoc}
     * <p>Detaches the StickyHeaderHelper from the RecyclerView if necessary.</p>
     *
     * @since 5.0.0-b6
     */
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        if (mStickyHeaderHelper != null) {
            mStickyHeaderHelper.detachFromRecyclerView(recyclerView);
            mStickyHeaderHelper = null;
        }
        super.onDetachedFromRecyclerView(recyclerView);
    }

    /**
     * Returns the ViewGroup (FrameLayout) that will hold the headers when sticky.
     * <p><b>INCLUDE</b> the predefined layout after the RecyclerView widget, example:
     * <pre>&lt;android.support.v7.widget.RecyclerView
     * android:id="@+id/recycler_view"
     * android:layout_width="match_parent"
     * android:layout_height="match_parent"/&gt;</pre>
     * <pre>&lt;include layout="@layout/sticky_header_layout"/&gt;</pre></p>
     * <p><b>OR</b></p>
     * Implement this method to return an already inflated ViewGroup.
     * <br/>The ViewGroup <u>must</u> have {@code android:id="@+id/sticky_header_container"}.
     *
     * @return ViewGroup layout that will hold the sticky header ItemViews
     * @since 5.0.0-b6
     */
    public ViewGroup getStickySectionHeadersHolder() {
        return (ViewGroup) Utils.scanForActivity(recyclerView.getContext()).findViewById(R.id.sticky_header_container);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * Retrieves the {@link HeaderTitle} item of any specified position.
     *
     * @param position the item position
     * @return the HeaderTitle item linked to the specified item position
     * @since 5.0.0-b6
     */
    //TODO: rename to getSectionByItemPosition?
    public HeaderTitle getSectionHeader(@IntRange(from = 0) int position) {
        //Headers are not visible nor sticky
        if (!headersShown) return null;
        //When headers are visible and sticky, get the previous header
        for (int i = position; i >= 0; i--) {
            T item = getItem(i);
            if (isHeader(item)) return (HeaderTitle) item;
        }
        return null;
    }


    public int getGlobalPositionOf(@NonNull BaseFile item) {
        return mItems != null && !mItems.isEmpty() ? mItems.indexOf(item) : -1;
    }

    /**
     * Retrieves all the header items.
     *
     * @return non-null list with all the header items
     * @since 5.0.0-b6
     */
    @NonNull
    public List<HeaderTitle> getHeaderItems() {
        List<HeaderTitle> headers = new ArrayList<>();
        for (T item : mItems) {
            if (isHeader(item))
                headers.add((HeaderTitle) item);
        }
        return headers;
    }

    public boolean isHeader(T item) {
        return item != null && item instanceof HeaderTitle;
    }

}

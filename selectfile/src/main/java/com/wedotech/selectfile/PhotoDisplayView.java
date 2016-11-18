package com.wedotech.selectfile;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.wedotech.selectfile.adapter.BaseAdapter;
import com.wedotech.selectfile.adapter.PhotoDateGroupAdapter;
import com.wedotech.selectfile.adapter.PhotoNoGroupAdapter;
import com.wedotech.selectfile.cursors.loadercallbacks.FileResultCallback;
import com.wedotech.selectfile.models.BaseFile;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.models.PhotoDirectory;
import com.wedotech.selectfile.support.FilePickerConst;
import com.wedotech.selectfile.support.MediaStoreHelper;
import com.wedotech.selectfile.support.OnPhotoSelectedListener;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhsheng on 2016/11/5.
 */

public class PhotoDisplayView extends RecyclerView {

    private int column = 4;
    private boolean groupByDate = false;
    private BaseAdapter adapter;
    private ArrayList<Photo> photoList;
    private OnPhotoSelectedListener selectedListener;
    private Subscription subscribe;

    public PhotoDisplayView(Context context) {
        super(context);
        setupView();
    }

    public PhotoDisplayView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoDisplayView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SelectFile, defStyle, 0);
        groupByDate = typedArray.getBoolean(R.styleable.SelectFile_groupByDate, false);
        column = typedArray.getInteger(R.styleable.SelectFile_column, 4);
        typedArray.recycle();
        setupView();
    }

    private void setupView() {
        final GridLayoutManager manager = new GridLayoutManager(getContext(), column);
        if (groupByDate) {
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return adapter.getItemViewType(position) == FilePickerConst.TITLE_HEADER ? manager.getSpanCount() : 1;
                }
            });
        }
        setLayoutManager(manager);
        photoList = new ArrayList<>();
        if (!groupByDate) {
            adapter = new PhotoNoGroupAdapter(photoList);
            adapter.setOnPhotoSelectedListener(new OnPhotoSelectedListener() {
                @Override
                public void photoSelected(BaseFile photo, int selectedCount) {
                    if (selectedListener != null)
                        selectedListener.photoSelected(photo, selectedCount);
                }
            });
        } else {
            adapter = new PhotoDateGroupAdapter(photoList);
        }
        setAdapter(adapter);
    }

    public void setMaxCount(int maxCount) {
        if (adapter != null) adapter.setMaxCount(maxCount);
    }

    public void putSelectedPhotos(ArrayList<Photo> photos) {
        if (adapter != null) adapter.putSelectedPhotos(photos);
    }

    public void showAllPhoto(FragmentActivity activity) {
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(FilePickerConst.EXTRA_SHOW_GIF, false);
        MediaStoreHelper.getPhotoDirs(activity, mediaStoreArgs, new FileResultCallback<PhotoDirectory>() {
            @Override
            public void onResultCallback(List<PhotoDirectory> files) {
                ArrayList<Photo> photoArrayList = new ArrayList<>();
                for (PhotoDirectory directory : files) {
                    photoArrayList.addAll(directory.getPhotos());
                }
                setupPhotos(photoArrayList);
            }
        });
    }

    private void dealPhotoGroup(ArrayList<Photo> files) {
        Observable.fromIterable(files)
                .groupBy(new Function<Photo, String>() {
                    @Override
                    public String apply(Photo photo) throws Exception {
                        return photo.getTitle();
                    }
                })
                .flatMap(new Function<GroupedObservable<String, Photo>, ObservableSource<List<Photo>>>() {
                    @Override
                    public ObservableSource<List<Photo>> apply(GroupedObservable<String, Photo> stringPhotoGroupedObservable) throws Exception {
                        return stringPhotoGroupedObservable.toList().toObservable();
                    }
                })
                .sorted(new Comparator<List<Photo>>() {
                    @Override
                    public int compare(List<Photo> photos, List<Photo> t1) {
                        Photo photo = photos.get(0);
                        Photo photo1 = t1.get(0);
                        if (photo1.getDateTaken() > photo.getDateTaken()) {
                            return 1;
                        } else if (photo1.getDateTaken() == photo.getDateTaken()) {
                            return 0;
                        } else {
                            return -1;
                        }
                    }
                })
                .map(new Function<List<Photo>, List<Photo>>() {
                    @Override
                    public List<Photo> apply(List<Photo> photos) throws Exception {
                        photoList.add(new Photo(photos.get(0).getTitle()));
                        photoList.addAll(photos);
                        return photoList;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultObserver<List<Photo>>() {
                    @Override
                    public void onNext(List<Photo> value) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    public void setupPhotos(ArrayList<Photo> photos) {
        this.photoList.clear();
        if (photos == null) return;
        if (!groupByDate) {
            this.photoList.addAll(photos);
            adapter.notifyDataSetChanged();
        } else {
            dealPhotoGroup(photos);
        }
    }

    public ArrayList<Photo> getSelectedPhotos() {
        return adapter.getSelectedPhotos();
    }

    public void setOnPhotoSelectedListener(OnPhotoSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

}

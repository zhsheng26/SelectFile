package com.wedotech.selectfile.support;

import com.wedotech.selectfile.models.Photo;

/**
 * Created by zhsheng on 2016/11/17.
 */

public interface OnGridPhotoActionListener {
    void onClickPhoto(Photo photo);

    void onClickDelete(Photo photo);

    void onClickAdd();
}

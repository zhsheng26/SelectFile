package com.wedotech.selectfile.support;

import com.wedotech.selectfile.models.Photo;

import java.util.List;

/**
 * Created by zhsheng on 2016/11/5.
 */

public interface OnSelectDirListener {
    void onSelectDir(List<Photo> photos);
}

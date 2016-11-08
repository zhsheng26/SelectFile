package com.wedotech.selectfile.support;

import com.wedotech.selectfile.models.Photo;

/**
 * Created by zhsheng on 2016/11/8.
 */

public interface OnPhotoSelectedListener {
    /**
     * @param photo         当前获取焦点Photo
     * @param selectedCount 选中的图片个数
     */
    void photoSelected(Photo photo, int selectedCount);
}

package com.wedotech.selectfile.models;

import com.wedotech.selectfile.support.FilePickerConst;

/**
 * Created by zhsheng on 2016/11/8.
 */

public class HeaderTitle implements BaseFile {
    public HeaderTitle(String title) {
        this.title = title;
    }

    private String title;

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getType() {
        return FilePickerConst.TITLE_HEADER;
    }

    @Override
    public String getPath() {
        return title;
    }
}

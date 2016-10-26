package com.wedotech.selectfile.models;


import com.wedotech.selectfile.Utils;

public class BaseFile {
    protected int id;
    protected String name;
    protected String path;

    public BaseFile(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public boolean isImage() {
        String[] types = {"jpg", "png", "gif"};
        return Utils.contains(types, this.path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseFile)) return false;

        BaseFile baseFile = (BaseFile) o;

        return id == baseFile.id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

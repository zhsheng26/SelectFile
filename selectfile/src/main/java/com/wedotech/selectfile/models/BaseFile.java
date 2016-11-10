package com.wedotech.selectfile.models;


import android.os.Parcel;
import android.os.Parcelable;

public class BaseFile implements Parcelable {
    protected int id;
    protected String path;
    protected String title;
    protected int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BaseFile() {
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.path);
        dest.writeString(this.title);
        dest.writeInt(this.type);
    }

    protected BaseFile(Parcel in) {
        this.id = in.readInt();
        this.path = in.readString();
        this.title = in.readString();
        this.type = in.readInt();
    }

}

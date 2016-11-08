package com.wedotech.selectfile.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.wedotech.selectfile.support.FilePickerConst;

public class Photo implements BaseFile, Parcelable {
    private int id;
    private String name;
    private String path;
    private long dateTaken;
    private String title;

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Photo(int id, String name, String path, long dateTaken, String title) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.dateTaken = dateTaken;
        this.title = title;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int getType() {
        return FilePickerConst.PHOTO_PICKER;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeLong(this.dateTaken);
        dest.writeString(this.title);
    }

    protected Photo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.path = in.readString();
        this.dateTaken = in.readLong();
        this.title = in.readString();
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}

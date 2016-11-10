package com.wedotech.selectfile.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.wedotech.selectfile.support.FilePickerConst;

public class Photo extends BaseFile implements Parcelable {
    private String name;
    private long dateTaken;

    public Photo(String title) {
        this.title = title;
        this.type = FilePickerConst.TITLE_HEADER;
    }


    public Photo(int id, String name, String path, long dateTaken, String title) {
        this.id = id;
        this.name = name;
        this.path = path;
        this.dateTaken = dateTaken;
        this.title = title;
        this.type = FilePickerConst.PHOTO_PICKER;
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
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
        dest.writeLong(this.dateTaken);
    }

    protected Photo(Parcel in) {
        super(in);
        this.name = in.readString();
        this.dateTaken = in.readLong();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
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

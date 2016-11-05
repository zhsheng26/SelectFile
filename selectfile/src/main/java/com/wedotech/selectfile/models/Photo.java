package com.wedotech.selectfile.models;


public class Photo implements BaseFile {
    private int id;
    private String name;
    private String path;

    public Photo(int id, String name, String path) {
        this.id = id;
        this.name = name;
        this.path = path;
    }

    public Photo() {
        this(0, null, null);
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

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

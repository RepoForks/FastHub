package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Kosh on 12 Nov 2016, 11:09 AM
 */

public class FilesListModel implements Parcelable, Serializable {

    @SerializedName("filename") private String filename;
    @SerializedName("type") private String type;
    @SerializedName("language") private String language;
    @SerializedName("raw_url") private String rawUrl;
    @SerializedName("size") private int size;
    @SerializedName("content") private String content;
    private boolean needFetching;
    private String id;

    public String getFilename() { return filename;}

    public void setFilename(String filename) { this.filename = filename;}

    public String getType() { return type;}

    public void setType(String type) { this.type = type;}

    public String getLanguage() { return language;}

    public void setLanguage(String language) { this.language = language;}

    public String getRawUrl() { return rawUrl;}

    public void setRawUrl(String rawUrl) { this.rawUrl = rawUrl;}

    public int getSize() { return size;}

    public void setSize(int size) { this.size = size;}

    public FilesListModel() {}

    @Override public String toString() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isNeedFetching() {
        return needFetching;
    }

    public void setNeedFetching(boolean needFetching) {
        this.needFetching = needFetching;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.filename);
        dest.writeString(this.type);
        dest.writeString(this.language);
        dest.writeString(this.rawUrl);
        dest.writeInt(this.size);
        dest.writeString(this.content);
        dest.writeByte(this.needFetching ? (byte) 1 : (byte) 0);
        dest.writeString(this.id);
    }

    protected FilesListModel(Parcel in) {
        this.filename = in.readString();
        this.type = in.readString();
        this.language = in.readString();
        this.rawUrl = in.readString();
        this.size = in.readInt();
        this.content = in.readString();
        this.needFetching = in.readByte() != 0;
        this.id = in.readString();
    }

    public static final Creator<FilesListModel> CREATOR = new Creator<FilesListModel>() {
        @Override public FilesListModel createFromParcel(Parcel source) {return new FilesListModel(source);}

        @Override public FilesListModel[] newArray(int size) {return new FilesListModel[size];}
    };
}

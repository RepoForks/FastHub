package com.fastaccess.data.dao;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kosh on 10 Dec 2016, 7:52 PM
 */

public class ContentModel implements Parcelable {
    private String type;
    private String encoding;
    private long size;
    private String name;
    private String path;
    private String content;
    private String sha;
    private String url;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeString(this.encoding);
        dest.writeLong(this.size);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.content);
        dest.writeString(this.sha);
        dest.writeString(this.url);
    }

    public ContentModel() {}

    protected ContentModel(Parcel in) {
        this.type = in.readString();
        this.encoding = in.readString();
        this.size = in.readLong();
        this.name = in.readString();
        this.path = in.readString();
        this.content = in.readString();
        this.sha = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<ContentModel> CREATOR = new Parcelable.Creator<ContentModel>() {
        @Override public ContentModel createFromParcel(Parcel source) {return new ContentModel(source);}

        @Override public ContentModel[] newArray(int size) {return new ContentModel[size];}
    };
}

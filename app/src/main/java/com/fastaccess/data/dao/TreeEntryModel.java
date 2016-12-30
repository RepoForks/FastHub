package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.fastaccess.data.dao.types.GitEntryType;

/**
 * Created by Kosh on 17 Dec 2016, 10:32 AM
 */

public class TreeEntryModel implements Parcelable {
    private String path;
    private String mode;
    private String sha;
    private String url;
    private GitEntryType type;
    private long size;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public GitEntryType getType() {
        return type;
    }

    public void setType(GitEntryType type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.mode);
        dest.writeString(this.sha);
        dest.writeString(this.url);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeLong(this.size);
    }

    public TreeEntryModel() {}

    protected TreeEntryModel(Parcel in) {
        this.path = in.readString();
        this.mode = in.readString();
        this.sha = in.readString();
        this.url = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : GitEntryType.values()[tmpType];
        this.size = in.readLong();
    }

    public static final Parcelable.Creator<TreeEntryModel> CREATOR = new Parcelable.Creator<TreeEntryModel>() {
        @Override public TreeEntryModel createFromParcel(Parcel source) {return new TreeEntryModel(source);}

        @Override public TreeEntryModel[] newArray(int size) {return new TreeEntryModel[size];}
    };
}

package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Kosh on 17 Dec 2016, 10:31 AM
 */

public class TreeModel implements Parcelable {

    private String sha;
    private String url;
    private List<TreeEntryModel> tree;
    private boolean truncated;

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

    public List<TreeEntryModel> getTree() {
        return tree;
    }

    public void setTree(List<TreeEntryModel> tree) {
        this.tree = tree;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sha);
        dest.writeString(this.url);
        dest.writeTypedList(this.tree);
        dest.writeByte(this.truncated ? (byte) 1 : (byte) 0);
    }

    public TreeModel() {}

    protected TreeModel(Parcel in) {
        this.sha = in.readString();
        this.url = in.readString();
        this.tree = in.createTypedArrayList(TreeEntryModel.CREATOR);
        this.truncated = in.readByte() != 0;
    }

    public static final Parcelable.Creator<TreeModel> CREATOR = new Parcelable.Creator<TreeModel>() {
        @Override public TreeModel createFromParcel(Parcel source) {return new TreeModel(source);}

        @Override public TreeModel[] newArray(int size) {return new TreeModel[size];}
    };
}

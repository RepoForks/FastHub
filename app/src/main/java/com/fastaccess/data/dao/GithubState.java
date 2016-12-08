package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kosh on 08 Dec 2016, 8:57 PM
 */

public class GithubState implements Parcelable {
    private int additions;
    private Integer deletions;
    private Integer total;

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public Integer getDeletions() {
        return deletions;
    }

    public void setDeletions(Integer deletions) {
        this.deletions = deletions;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.additions);
        dest.writeValue(this.deletions);
        dest.writeValue(this.total);
    }

    public GithubState() {}

    protected GithubState(Parcel in) {
        this.additions = in.readInt();
        this.deletions = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<GithubState> CREATOR = new Parcelable.Creator<GithubState>() {
        @Override public GithubState createFromParcel(Parcel source) {return new GithubState(source);}

        @Override public GithubState[] newArray(int size) {return new GithubState[size];}
    };
}

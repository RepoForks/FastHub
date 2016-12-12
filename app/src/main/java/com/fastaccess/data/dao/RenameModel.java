package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kosh on 13 Dec 2016, 12:33 AM
 */

public class RenameModel implements Parcelable {
    private String from;
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.from);
        dest.writeString(this.to);
    }

    public RenameModel() {}

    protected RenameModel(Parcel in) {
        this.from = in.readString();
        this.to = in.readString();
    }

    public static final Parcelable.Creator<RenameModel> CREATOR = new Parcelable.Creator<RenameModel>() {
        @Override public RenameModel createFromParcel(Parcel source) {return new RenameModel(source);}

        @Override public RenameModel[] newArray(int size) {return new RenameModel[size];}
    };
}

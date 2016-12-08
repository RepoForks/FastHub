package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kosh on 08 Dec 2016, 9:05 PM
 */

public class LabelModel implements Parcelable {
    private String url;
    private String name;
    private String color;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.name);
        dest.writeString(this.color);
    }

    public LabelModel() {}

    protected LabelModel(Parcel in) {
        this.url = in.readString();
        this.name = in.readString();
        this.color = in.readString();
    }

    public static final Parcelable.Creator<LabelModel> CREATOR = new Parcelable.Creator<LabelModel>() {
        @Override public LabelModel createFromParcel(Parcel source) {return new LabelModel(source);}

        @Override public LabelModel[] newArray(int size) {return new LabelModel[size];}
    };
}

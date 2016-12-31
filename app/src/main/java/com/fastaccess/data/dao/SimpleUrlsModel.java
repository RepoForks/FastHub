package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kosh on 31 Dec 2016, 3:32 PM
 */

public class SimpleUrlsModel implements Parcelable {

    private String item;
    private String url;

    public SimpleUrlsModel(String item, String url) {
        this.item = item;
        this.url = url;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {dest.writeString(this.item);}

    protected SimpleUrlsModel(Parcel in) {this.item = in.readString();}

    public static final Parcelable.Creator<SimpleUrlsModel> CREATOR = new Parcelable.Creator<SimpleUrlsModel>() {
        @Override public SimpleUrlsModel createFromParcel(Parcel source) {return new SimpleUrlsModel(source);}

        @Override public SimpleUrlsModel[] newArray(int size) {return new SimpleUrlsModel[size];}
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override public String toString() {
        return item;
    }
}

package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kosh on 01 Jan 2017, 1:15 PM
 */

public class LicenseModel implements Parcelable {


    @SerializedName("key") private String key;
    @SerializedName("name") private String name;
    @SerializedName("spdx_id") private String spdxId;
    @SerializedName("url") private String url;
    @SerializedName("featured") private boolean featured;

    public String getKey() { return key;}

    public void setKey(String key) { this.key = key;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getSpdxId() { return spdxId;}

    public void setSpdxId(String spdxId) { this.spdxId = spdxId;}

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url;}

    public boolean isFeatured() { return featured;}

    public void setFeatured(boolean featured) { this.featured = featured;}

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.spdxId);
        dest.writeString(this.url);
        dest.writeByte(this.featured ? (byte) 1 : (byte) 0);
    }

    public LicenseModel() {}

    protected LicenseModel(Parcel in) {
        this.key = in.readString();
        this.name = in.readString();
        this.spdxId = in.readString();
        this.url = in.readString();
        this.featured = in.readByte() != 0;
    }

    public static final Parcelable.Creator<LicenseModel> CREATOR = new Parcelable.Creator<LicenseModel>() {
        @Override public LicenseModel createFromParcel(Parcel source) {return new LicenseModel(source);}

        @Override public LicenseModel[] newArray(int size) {return new LicenseModel[size];}
    };
}

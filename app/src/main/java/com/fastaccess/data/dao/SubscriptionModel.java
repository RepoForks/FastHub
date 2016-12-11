package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kosh on 11 Dec 2016, 12:27 PM
 */

public class SubscriptionModel implements Parcelable {
    private boolean subscribed;
    private boolean ignored;
    private String reason;
    private String url;
    @SerializedName("created_at") private String createdAt;

    @Override public String toString() {
        return "SubscriptionModel{" +
                "subscribed=" + subscribed +
                ", ignored=" + ignored +
                ", reason='" + reason + '\'' +
                ", url='" + url + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public boolean isIgnored() {
        return ignored;
    }

    public void setIgnored(boolean ignored) {
        this.ignored = ignored;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.subscribed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.ignored ? (byte) 1 : (byte) 0);
        dest.writeString(this.reason);
        dest.writeString(this.url);
        dest.writeString(this.createdAt);
    }

    public SubscriptionModel() {}

    protected SubscriptionModel(Parcel in) {
        this.subscribed = in.readByte() != 0;
        this.ignored = in.readByte() != 0;
        this.reason = in.readString();
        this.url = in.readString();
        this.createdAt = in.readString();
    }

    public static final Creator<SubscriptionModel> CREATOR = new Creator<SubscriptionModel>() {
        @Override public SubscriptionModel createFromParcel(Parcel source) {return new SubscriptionModel(source);}

        @Override public SubscriptionModel[] newArray(int size) {return new SubscriptionModel[size];}
    };
}

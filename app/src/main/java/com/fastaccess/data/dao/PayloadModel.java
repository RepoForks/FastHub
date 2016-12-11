package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PayloadModel implements Parcelable {
    @SerializedName("action") private String action;
    @SerializedName("forkee") private RepoModel forkee;

    public RepoModel getForkee() { return forkee;}

    public void setForkee(RepoModel forkee) { this.forkee = forkee;}

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.action);
        dest.writeParcelable(this.forkee, flags);
    }

    public PayloadModel() {}

    protected PayloadModel(Parcel in) {
        this.action = in.readString();
        this.forkee = in.readParcelable(RepoModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<PayloadModel> CREATOR = new Parcelable.Creator<PayloadModel>() {
        @Override public PayloadModel createFromParcel(Parcel source) {return new PayloadModel(source);}

        @Override public PayloadModel[] newArray(int size) {return new PayloadModel[size];}
    };
}
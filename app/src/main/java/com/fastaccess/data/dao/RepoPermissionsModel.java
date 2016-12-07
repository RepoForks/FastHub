package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kosh on 03 Dec 2016, 11:12 AM
 */

public class RepoPermissionsModel implements Parcelable {

    private boolean admin;
    private boolean push;
    private boolean pull;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isPush() {
        return push;
    }

    public void setPush(boolean push) {
        this.push = push;
    }

    public boolean isPull() {
        return pull;
    }

    public void setPull(boolean pull) {
        this.pull = pull;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.admin ? (byte) 1 : (byte) 0);
        dest.writeByte(this.push ? (byte) 1 : (byte) 0);
        dest.writeByte(this.pull ? (byte) 1 : (byte) 0);
    }

    public RepoPermissionsModel() {}

    protected RepoPermissionsModel(Parcel in) {
        this.admin = in.readByte() != 0;
        this.push = in.readByte() != 0;
        this.pull = in.readByte() != 0;
    }

    public static final Parcelable.Creator<RepoPermissionsModel> CREATOR = new Parcelable.Creator<RepoPermissionsModel>() {
        @Override public RepoPermissionsModel createFromParcel(Parcel source) {return new RepoPermissionsModel(source);}

        @Override public RepoPermissionsModel[] newArray(int size) {return new RepoPermissionsModel[size];}
    };
}

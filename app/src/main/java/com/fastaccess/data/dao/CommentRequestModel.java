package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kosh on 20 Nov 2016, 10:40 AM
 */

public class CommentRequestModel implements Parcelable {
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {dest.writeString(this.body);}

    public CommentRequestModel() {}

    protected CommentRequestModel(Parcel in) {this.body = in.readString();}

    public static final Parcelable.Creator<CommentRequestModel> CREATOR = new Parcelable.Creator<CommentRequestModel>() {
        @Override public CommentRequestModel createFromParcel(Parcel source) {return new CommentRequestModel(source);}

        @Override public CommentRequestModel[] newArray(int size) {return new CommentRequestModel[size];}
    };
}

package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kosh on 16 Dec 2016, 11:40 PM
 */

public class MergeResponseModel implements Parcelable {

    private String sha;
    private boolean merged;
    private String message;
    @SerializedName("documentation_url") private String documentationUrl;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDocumentationUrl() {
        return documentationUrl;
    }

    public void setDocumentationUrl(String documentationUrl) {
        this.documentationUrl = documentationUrl;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sha);
        dest.writeByte(this.merged ? (byte) 1 : (byte) 0);
        dest.writeString(this.message);
        dest.writeString(this.documentationUrl);
    }

    public MergeResponseModel() {}

    protected MergeResponseModel(Parcel in) {
        this.sha = in.readString();
        this.merged = in.readByte() != 0;
        this.message = in.readString();
        this.documentationUrl = in.readString();
    }

    public static final Parcelable.Creator<MergeResponseModel> CREATOR = new Parcelable.Creator<MergeResponseModel>() {
        @Override public MergeResponseModel createFromParcel(Parcel source) {return new MergeResponseModel(source);}

        @Override public MergeResponseModel[] newArray(int size) {return new MergeResponseModel[size];}
    };
}

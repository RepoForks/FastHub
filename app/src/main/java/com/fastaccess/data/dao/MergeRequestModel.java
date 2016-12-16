package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kosh on 16 Dec 2016, 11:42 PM
 */

public class MergeRequestModel implements Parcelable {

    @SerializedName("commit_message") private String commitMessage;
    private String sha;
    private String base;
    private String head;

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.commitMessage);
        dest.writeString(this.sha);
        dest.writeString(this.base);
        dest.writeString(this.head);
    }

    public MergeRequestModel() {}

    protected MergeRequestModel(Parcel in) {
        this.commitMessage = in.readString();
        this.sha = in.readString();
        this.base = in.readString();
        this.head = in.readString();
    }

    public static final Parcelable.Creator<MergeRequestModel> CREATOR = new Parcelable.Creator<MergeRequestModel>() {
        @Override public MergeRequestModel createFromParcel(Parcel source) {return new MergeRequestModel(source);}

        @Override public MergeRequestModel[] newArray(int size) {return new MergeRequestModel[size];}
    };
}

package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kosh on 17 Dec 2016, 10:35 AM
 */

public class CommitCompareModel implements Parcelable {
    private String url;
    private String status;
    private List<CommentsModel> commits;
    private List<FilesListModel> files;
    @SerializedName("base_commit") private CommitModel basecommit;
    @SerializedName("merge_base_commit") private CommitModel mergeBaseCommit;
    @SerializedName("ahead_by") private int aheadBy;
    @SerializedName("behind_by") private int behindBy;
    @SerializedName("total_commits") private int totalCommits;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CommentsModel> getCommits() {
        return commits;
    }

    public void setCommits(List<CommentsModel> commits) {
        this.commits = commits;
    }

    public List<FilesListModel> getFiles() {
        return files;
    }

    public void setFiles(List<FilesListModel> files) {
        this.files = files;
    }

    public CommitModel getBasecommit() {
        return basecommit;
    }

    public void setBasecommit(CommitModel basecommit) {
        this.basecommit = basecommit;
    }

    public CommitModel getMergeBaseCommit() {
        return mergeBaseCommit;
    }

    public void setMergeBaseCommit(CommitModel mergeBaseCommit) {
        this.mergeBaseCommit = mergeBaseCommit;
    }

    public int getAheadBy() {
        return aheadBy;
    }

    public void setAheadBy(int aheadBy) {
        this.aheadBy = aheadBy;
    }

    public int getBehindBy() {
        return behindBy;
    }

    public void setBehindBy(int behindBy) {
        this.behindBy = behindBy;
    }

    public int getTotalCommits() {
        return totalCommits;
    }

    public void setTotalCommits(int totalCommits) {
        this.totalCommits = totalCommits;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.status);
        dest.writeTypedList(this.commits);
        dest.writeTypedList(this.files);
        dest.writeParcelable(this.basecommit, flags);
        dest.writeParcelable(this.mergeBaseCommit, flags);
        dest.writeInt(this.aheadBy);
        dest.writeInt(this.behindBy);
        dest.writeInt(this.totalCommits);
    }

    public CommitCompareModel() {}

    protected CommitCompareModel(Parcel in) {
        this.url = in.readString();
        this.status = in.readString();
        this.commits = in.createTypedArrayList(CommentsModel.CREATOR);
        this.files = in.createTypedArrayList(FilesListModel.CREATOR);
        this.basecommit = in.readParcelable(CommitModel.class.getClassLoader());
        this.mergeBaseCommit = in.readParcelable(CommitModel.class.getClassLoader());
        this.aheadBy = in.readInt();
        this.behindBy = in.readInt();
        this.totalCommits = in.readInt();
    }

    public static final Parcelable.Creator<CommitCompareModel> CREATOR = new Parcelable.Creator<CommitCompareModel>() {
        @Override public CommitCompareModel createFromParcel(Parcel source) {return new CommitCompareModel(source);}

        @Override public CommitCompareModel[] newArray(int size) {return new CommitCompareModel[size];}
    };
}

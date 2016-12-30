package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 17 Dec 2016, 10:30 AM
 */

public class PullRequestCommitModel implements Parcelable {

    private String sha;
    private String url;
    private String message;
    private UserModel author;
    private UserModel committer;
    private boolean distinct;
    private TreeModel tree;
    private List<PullRequestCommitModel> parents;
    @SerializedName("comment_count") private int commentCount;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserModel getAuthor() {
        return author;
    }

    public void setAuthor(UserModel author) {
        this.author = author;
    }

    public UserModel getCommitter() {
        return committer;
    }

    public void setCommitter(UserModel committer) {
        this.committer = committer;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public TreeModel getTree() {
        return tree;
    }

    public void setTree(TreeModel tree) {
        this.tree = tree;
    }

    public List<PullRequestCommitModel> getParents() {
        return parents;
    }

    public void setParents(List<PullRequestCommitModel> parents) {
        this.parents = parents;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sha);
        dest.writeString(this.url);
        dest.writeString(this.message);
        dest.writeParcelable(this.author, flags);
        dest.writeParcelable(this.committer, flags);
        dest.writeByte(this.distinct ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.tree, flags);
        dest.writeList(this.parents);
        dest.writeInt(this.commentCount);
    }

    public PullRequestCommitModel() {}

    protected PullRequestCommitModel(Parcel in) {
        this.sha = in.readString();
        this.url = in.readString();
        this.message = in.readString();
        this.author = in.readParcelable(UserModel.class.getClassLoader());
        this.committer = in.readParcelable(UserModel.class.getClassLoader());
        this.distinct = in.readByte() != 0;
        this.tree = in.readParcelable(TreeModel.class.getClassLoader());
        this.parents = new ArrayList<PullRequestCommitModel>();
        in.readList(this.parents, PullRequestCommitModel.class.getClassLoader());
        this.commentCount = in.readInt();
    }

    public static final Parcelable.Creator<PullRequestCommitModel> CREATOR = new Parcelable.Creator<PullRequestCommitModel>() {
        @Override public PullRequestCommitModel createFromParcel(Parcel source) {return new PullRequestCommitModel(source);}

        @Override public PullRequestCommitModel[] newArray(int size) {return new PullRequestCommitModel[size];}
    };
}

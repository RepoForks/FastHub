package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 08 Dec 2016, 8:59 PM
 */

public class GitCommitModel implements Parcelable {
    private String sha;
    private String url;
    private String message;
    private ActorModel author;
    private ActorModel committer;
    private ActorModel tree;
    private boolean distinct;
    private List<GitCommitModel> parents;
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

    public ActorModel getAuthor() {
        return author;
    }

    public void setAuthor(ActorModel author) {
        this.author = author;
    }

    public ActorModel getCommitter() {
        return committer;
    }

    public void setCommitter(ActorModel committer) {
        this.committer = committer;
    }

    public ActorModel getTree() {
        return tree;
    }

    public void setTree(ActorModel tree) {
        this.tree = tree;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public List<GitCommitModel> getParents() {
        return parents;
    }

    public void setParents(List<GitCommitModel> parents) {
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
        dest.writeParcelable(this.tree, flags);
        dest.writeByte(this.distinct ? (byte) 1 : (byte) 0);
        dest.writeList(this.parents);
        dest.writeInt(this.commentCount);
    }

    public GitCommitModel() {}

    protected GitCommitModel(Parcel in) {
        this.sha = in.readString();
        this.url = in.readString();
        this.message = in.readString();
        this.author = in.readParcelable(ActorModel.class.getClassLoader());
        this.committer = in.readParcelable(ActorModel.class.getClassLoader());
        this.tree = in.readParcelable(ActorModel.class.getClassLoader());
        this.distinct = in.readByte() != 0;
        this.parents = new ArrayList<GitCommitModel>();
        in.readList(this.parents, GitCommitModel.class.getClassLoader());
        this.commentCount = in.readInt();
    }

    public static final Parcelable.Creator<GitCommitModel> CREATOR = new Parcelable.Creator<GitCommitModel>() {
        @Override public GitCommitModel createFromParcel(Parcel source) {return new GitCommitModel(source);}

        @Override public GitCommitModel[] newArray(int size) {return new GitCommitModel[size];}
    };
}

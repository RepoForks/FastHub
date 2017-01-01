package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kosh on 08 Dec 2016, 8:55 PM
 */

public class CommitModel implements Parcelable {

    private String url;
    private String ref;
    private RepoModel repo;
    private String sha;
    private boolean distinct;
    private GitCommitModel commit;
    private UserModel author;
    private UserModel committer;
    private List<CommitModel> parents;
    private GithubState stats;
    private List<CommitFileModel> files;
    @SerializedName("html_url") private String htmlUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public RepoModel getRepo() {
        return repo;
    }

    public void setRepo(RepoModel repo) {
        this.repo = repo;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public GitCommitModel getCommit() {
        return commit;
    }

    public void setCommit(GitCommitModel commit) {
        this.commit = commit;
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

    public List<CommitModel> getParents() {
        return parents;
    }

    public void setParents(List<CommitModel> parents) {
        this.parents = parents;
    }

    public GithubState getStats() {
        return stats;
    }

    public void setStats(GithubState stats) {
        this.stats = stats;
    }

    public List<CommitFileModel> getFiles() {
        return files;
    }

    public void setFiles(List<CommitFileModel> files) {
        this.files = files;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.ref);
        dest.writeParcelable(this.repo, flags);
        dest.writeString(this.sha);
        dest.writeByte(this.distinct ? (byte) 1 : (byte) 0);
        dest.writeParcelable(this.commit, flags);
        dest.writeParcelable(this.author, flags);
        dest.writeParcelable(this.committer, flags);
        dest.writeTypedList(this.parents);
        dest.writeParcelable(this.stats, flags);
        dest.writeTypedList(this.files);
        dest.writeString(this.htmlUrl);
    }

    public CommitModel() {}

    protected CommitModel(Parcel in) {
        this.url = in.readString();
        this.ref = in.readString();
        this.repo = in.readParcelable(RepoModel.class.getClassLoader());
        this.sha = in.readString();
        this.distinct = in.readByte() != 0;
        this.commit = in.readParcelable(GitCommitModel.class.getClassLoader());
        this.author = in.readParcelable(UserModel.class.getClassLoader());
        this.committer = in.readParcelable(UserModel.class.getClassLoader());
        this.parents = in.createTypedArrayList(CommitModel.CREATOR);
        this.stats = in.readParcelable(GithubState.class.getClassLoader());
        this.files = in.createTypedArrayList(CommitFileModel.CREATOR);
        this.htmlUrl = in.readString();
    }

    public static final Creator<CommitModel> CREATOR = new Creator<CommitModel>() {
        @Override public CommitModel createFromParcel(Parcel source) {return new CommitModel(source);}

        @Override public CommitModel[] newArray(int size) {return new CommitModel[size];}
    };
}



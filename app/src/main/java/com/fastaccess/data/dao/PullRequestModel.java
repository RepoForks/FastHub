package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.fastaccess.data.dao.types.IssueState;
import com.google.gson.annotations.SerializedName;

import org.objectweb.asm.Label;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 08 Dec 2016, 8:51 PM
 */

public class PullRequestModel implements Parcelable {
    private String url;
    private String body;
    private String title;
    private int id;
    private int comments;
    private int number;
    private int additions;
    private int commits;
    private int deletions;
    private boolean locked;
    private boolean mergable;
    private boolean merged;
    private IssueState state;
    private UserModel userModel;
    private UserModel assignee;
    private List<Label> labels;
    private MilestoneModel milestone;
    private CommitModel base;
    private CommitModel head;
    @SerializedName("body_html") private String bodyHtml;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("pull_request") private PullRequestModel pullRequest;
    @SerializedName("closed_at") private String closedAt;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("closed_by") private UserModel closedBy;
    @SerializedName("changed_files") private int changedFiles;
    @SerializedName("diff_url") private String diffUrl;
    @SerializedName("patch_url") private String patchUrl;
    @SerializedName("merge_commit_sha") private String mergeCommitSha;
    @SerializedName("merged_at") private String mergedAt;
    @SerializedName("merged_by") private UserModel mergedBy;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getAdditions() {
        return additions;
    }

    public void setAdditions(int additions) {
        this.additions = additions;
    }

    public int getCommits() {
        return commits;
    }

    public void setCommits(int commits) {
        this.commits = commits;
    }

    public int getDeletions() {
        return deletions;
    }

    public void setDeletions(int deletions) {
        this.deletions = deletions;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isMergable() {
        return mergable;
    }

    public void setMergable(boolean mergable) {
        this.mergable = mergable;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public IssueState getState() {
        return state;
    }

    public void setState(IssueState state) {
        this.state = state;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public UserModel getAssignee() {
        return assignee;
    }

    public void setAssignee(com.fastaccess.data.dao.UserModel assignee) {
        this.assignee = assignee;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public MilestoneModel getMilestone() {
        return milestone;
    }

    public void setMilestone(MilestoneModel milestone) {
        this.milestone = milestone;
    }

    public CommitModel getBase() {
        return base;
    }

    public void setBase(CommitModel base) {
        this.base = base;
    }

    public CommitModel getHead() {
        return head;
    }

    public void setHead(CommitModel head) {
        this.head = head;
    }

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public PullRequestModel getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(PullRequestModel pullRequest) {
        this.pullRequest = pullRequest;
    }

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserModel getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(com.fastaccess.data.dao.UserModel closedBy) {
        this.closedBy = closedBy;
    }

    public int getChangedFiles() {
        return changedFiles;
    }

    public void setChangedFiles(int changedFiles) {
        this.changedFiles = changedFiles;
    }

    public String getDiffUrl() {
        return diffUrl;
    }

    public void setDiffUrl(String diffUrl) {
        this.diffUrl = diffUrl;
    }

    public String getPatchUrl() {
        return patchUrl;
    }

    public void setPatchUrl(String patchUrl) {
        this.patchUrl = patchUrl;
    }

    public String getMergeCommitSha() {
        return mergeCommitSha;
    }

    public void setMergeCommitSha(String mergeCommitSha) {
        this.mergeCommitSha = mergeCommitSha;
    }

    public String getMergedAt() {
        return mergedAt;
    }

    public void setMergedAt(String mergedAt) {
        this.mergedAt = mergedAt;
    }

    public UserModel getMergedBy() {
        return mergedBy;
    }

    public void setMergedBy(com.fastaccess.data.dao.UserModel mergedBy) {
        this.mergedBy = mergedBy;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.body);
        dest.writeString(this.title);
        dest.writeInt(this.id);
        dest.writeInt(this.comments);
        dest.writeInt(this.number);
        dest.writeInt(this.additions);
        dest.writeInt(this.commits);
        dest.writeInt(this.deletions);
        dest.writeByte(this.locked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mergable ? (byte) 1 : (byte) 0);
        dest.writeByte(this.merged ? (byte) 1 : (byte) 0);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeParcelable(this.userModel, flags);
        dest.writeParcelable(this.assignee, flags);
        dest.writeList(this.labels);
        dest.writeParcelable(this.milestone, flags);
        dest.writeParcelable(this.base, flags);
        dest.writeParcelable(this.head, flags);
        dest.writeString(this.bodyHtml);
        dest.writeString(this.htmlUrl);
        dest.writeParcelable(this.pullRequest, flags);
        dest.writeString(this.closedAt);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeParcelable(this.closedBy, flags);
        dest.writeInt(this.changedFiles);
        dest.writeString(this.diffUrl);
        dest.writeString(this.patchUrl);
        dest.writeString(this.mergeCommitSha);
        dest.writeString(this.mergedAt);
        dest.writeParcelable(this.mergedBy, flags);
    }

    public PullRequestModel() {}

    protected PullRequestModel(Parcel in) {
        this.url = in.readString();
        this.body = in.readString();
        this.title = in.readString();
        this.id = in.readInt();
        this.comments = in.readInt();
        this.number = in.readInt();
        this.additions = in.readInt();
        this.commits = in.readInt();
        this.deletions = in.readInt();
        this.locked = in.readByte() != 0;
        this.mergable = in.readByte() != 0;
        this.merged = in.readByte() != 0;
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : IssueState.values()[tmpState];
        this.userModel = in.readParcelable(UserModel.class.getClassLoader());
        this.assignee = in.readParcelable(UserModel.class.getClassLoader());
        this.labels = new ArrayList<Label>();
        in.readList(this.labels, Label.class.getClassLoader());
        this.milestone = in.readParcelable(MilestoneModel.class.getClassLoader());
        this.base = in.readParcelable(CommitModel.class.getClassLoader());
        this.head = in.readParcelable(CommitModel.class.getClassLoader());
        this.bodyHtml = in.readString();
        this.htmlUrl = in.readString();
        this.pullRequest = in.readParcelable(PullRequestModel.class.getClassLoader());
        this.closedAt = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.closedBy = in.readParcelable(UserModel.class.getClassLoader());
        this.changedFiles = in.readInt();
        this.diffUrl = in.readString();
        this.patchUrl = in.readString();
        this.mergeCommitSha = in.readString();
        this.mergedAt = in.readString();
        this.mergedBy = in.readParcelable(UserModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<PullRequestModel> CREATOR = new Parcelable.Creator<PullRequestModel>() {
        @Override public PullRequestModel createFromParcel(Parcel source) {return new PullRequestModel(source);}

        @Override public PullRequestModel[] newArray(int size) {return new PullRequestModel[size];}
    };
}

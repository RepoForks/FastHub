package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.fastaccess.data.dao.types.IssueState;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Kosh on 08 Dec 2016, 9:02 PM
 */

public class IssueModel implements Parcelable {

    private String url;
    private String body;
    @SerializedName("body_html") private String bodyHtml;
    private String title;
    private int id;
    private int comments;
    private int number;
    private boolean locked;
    private IssueState state;
    private ActorModel user;
    private ActorModel assignee;
    private List<ActorModel> assignees;
    private List<LabelModel> labels;
    private MilestoneModel milestone;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("pull_request") private PullRequestModel pullRequest;
    @SerializedName("closed_at") private String closedAt;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("closed_by") private ActorModel closedBy;
    private RepoModel repository;

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

    public String getBodyHtml() {
        return bodyHtml;
    }

    public void setBodyHtml(String bodyHtml) {
        this.bodyHtml = bodyHtml;
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public IssueState getState() {
        return state;
    }

    public void setState(IssueState state) {
        this.state = state;
    }

    public ActorModel getUser() {
        return user;
    }

    public void setUser(ActorModel user) {
        this.user = user;
    }

    public ActorModel getAssignee() {
        return assignee;
    }

    public void setAssignee(ActorModel assignee) {
        this.assignee = assignee;
    }

    public List<ActorModel> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<ActorModel> assignees) {
        this.assignees = assignees;
    }

    public List<LabelModel> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelModel> labels) {
        this.labels = labels;
    }

    public MilestoneModel getMilestone() {
        return milestone;
    }

    public void setMilestone(MilestoneModel milestone) {
        this.milestone = milestone;
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

    public ActorModel getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(ActorModel closedBy) {
        this.closedBy = closedBy;
    }

    public RepoModel getRepository() {
        return repository;
    }

    public void setRepository(RepoModel repository) {
        this.repository = repository;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.body);
        dest.writeString(this.bodyHtml);
        dest.writeString(this.title);
        dest.writeInt(this.id);
        dest.writeInt(this.comments);
        dest.writeInt(this.number);
        dest.writeByte(this.locked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.assignee, flags);
        dest.writeTypedList(this.assignees);
        dest.writeTypedList(this.labels);
        dest.writeParcelable(this.milestone, flags);
        dest.writeString(this.htmlUrl);
        dest.writeParcelable(this.pullRequest, flags);
        dest.writeString(this.closedAt);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeParcelable(this.closedBy, flags);
        dest.writeParcelable(this.repository, flags);
    }

    public IssueModel() {}

    protected IssueModel(Parcel in) {
        this.url = in.readString();
        this.body = in.readString();
        this.bodyHtml = in.readString();
        this.title = in.readString();
        this.id = in.readInt();
        this.comments = in.readInt();
        this.number = in.readInt();
        this.locked = in.readByte() != 0;
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : IssueState.values()[tmpState];
        this.user = in.readParcelable(ActorModel.class.getClassLoader());
        this.assignee = in.readParcelable(ActorModel.class.getClassLoader());
        this.assignees = in.createTypedArrayList(ActorModel.CREATOR);
        this.labels = in.createTypedArrayList(LabelModel.CREATOR);
        this.milestone = in.readParcelable(MilestoneModel.class.getClassLoader());
        this.htmlUrl = in.readString();
        this.pullRequest = in.readParcelable(PullRequestModel.class.getClassLoader());
        this.closedAt = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.closedBy = in.readParcelable(ActorModel.class.getClassLoader());
        this.repository = in.readParcelable(RepoModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<IssueModel> CREATOR = new Parcelable.Creator<IssueModel>() {
        @Override public IssueModel createFromParcel(Parcel source) {return new IssueModel(source);}

        @Override public IssueModel[] newArray(int size) {return new IssueModel[size];}
    };
}

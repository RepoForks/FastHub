package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fastaccess.data.dao.types.IssueState;
import com.fastaccess.provider.paperdb.RxPaperBook;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import rx.Completable;
import rx.Single;

/**
 * Created by Kosh on 08 Dec 2016, 9:02 PM
 */

public class IssueModel implements Parcelable {

    private final static transient String BOOK_NAME = IssueModel.class.getSimpleName();

    private String url;
    private String body;
    private String title;
    private long id;
    private int comments;
    private int number;
    private boolean locked;
    private IssueState state;
    private UserModel user;
    private UserModel assignee;
    private List<UserModel> assignees;
    private List<LabelModel> labels;
    private MilestoneModel milestone;
    private RepoModel repository;
    @SerializedName("repository_url") private String repoUrl;
    @SerializedName("body_html") private String bodyHtml;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("pull_request") private PullRequestModel pullRequest;
    @SerializedName("closed_at") private String closedAt;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("closed_by") private UserModel closedBy;
    private String repoId;
    private String login;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserModel getAssignee() {
        return assignee;
    }

    public void setAssignee(UserModel assignee) {
        this.assignee = assignee;
    }

    public List<UserModel> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<UserModel> assignees) {
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

    public UserModel getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(UserModel closedBy) {
        this.closedBy = closedBy;
    }

    public RepoModel getRepository() {
        return repository;
    }

    public void setRepository(RepoModel repository) {
        this.repository = repository;
    }

    public IssueModel() {}

    public static Completable save(@NonNull IssueModel issueModel) {
        return RxPaperBook.with(BOOK_NAME).write(issueModel.getId() + "", issueModel);
    }

    public static Single<IssueModel> getIssue(long issueNumber, @NonNull String login, @NonNull String repoId) {
        return RxPaperBook.with(BOOK_NAME).read(getSafeKey(issueNumber, login, repoId));
    }

    private static String getSafeKey(long issueNumber, @NonNull String login, @NonNull String repoId) {
        String key = login + "_" + repoId + "_" + issueNumber;
        return key.length() < 80 ? key : key.substring(0, 80);
    }


    public String getRepoUrl() {
        return repoUrl;
    }

    public void setRepoUrl(String repoUrl) {
        this.repoUrl = repoUrl;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public String getRepoId() {
        return repoId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.body);
        dest.writeString(this.title);
        dest.writeLong(this.id);
        dest.writeInt(this.comments);
        dest.writeInt(this.number);
        dest.writeByte(this.locked ? (byte) 1 : (byte) 0);
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.assignee, flags);
        dest.writeTypedList(this.assignees);
        dest.writeTypedList(this.labels);
        dest.writeParcelable(this.milestone, flags);
        dest.writeParcelable(this.repository, flags);
        dest.writeString(this.repoUrl);
        dest.writeString(this.bodyHtml);
        dest.writeString(this.htmlUrl);
        dest.writeParcelable(this.pullRequest, flags);
        dest.writeString(this.closedAt);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeParcelable(this.closedBy, flags);
        dest.writeString(this.repoId);
        dest.writeString(this.login);
    }

    protected IssueModel(Parcel in) {
        this.url = in.readString();
        this.body = in.readString();
        this.title = in.readString();
        this.id = in.readLong();
        this.comments = in.readInt();
        this.number = in.readInt();
        this.locked = in.readByte() != 0;
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : IssueState.values()[tmpState];
        this.user = in.readParcelable(UserModel.class.getClassLoader());
        this.assignee = in.readParcelable(UserModel.class.getClassLoader());
        this.assignees = in.createTypedArrayList(UserModel.CREATOR);
        this.labels = in.createTypedArrayList(LabelModel.CREATOR);
        this.milestone = in.readParcelable(MilestoneModel.class.getClassLoader());
        this.repository = in.readParcelable(RepoModel.class.getClassLoader());
        this.repoUrl = in.readString();
        this.bodyHtml = in.readString();
        this.htmlUrl = in.readString();
        this.pullRequest = in.readParcelable(PullRequestModel.class.getClassLoader());
        this.closedAt = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.closedBy = in.readParcelable(UserModel.class.getClassLoader());
        this.repoId = in.readString();
        this.login = in.readString();
    }

    public static final Creator<IssueModel> CREATOR = new Creator<IssueModel>() {
        @Override public IssueModel createFromParcel(Parcel source) {return new IssueModel(source);}

        @Override public IssueModel[] newArray(int size) {return new IssueModel[size];}
    };
}

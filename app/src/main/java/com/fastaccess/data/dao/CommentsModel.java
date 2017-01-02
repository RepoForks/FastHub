package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fastaccess.provider.paperdb.RxPaperBook;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.Single;

/**
 * Created by Kosh on 20 Nov 2016, 10:34 AM
 */

public class CommentsModel implements Parcelable {

    private final static transient String BOOK_NAME = CommentsModel.class.getSimpleName();

    private long id;
    private UserModel user;
    private String url;
    private String body;
    @SerializedName("body_html") private String bodyHtml;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    private int position;
    private int line;
    private String path;
    @SerializedName("commit_id") private String commitId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

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

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
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

    public CommentsModel() {}

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommentsModel that = (CommentsModel) o;

        return id == that.id;

    }

    @Override public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public static Completable save(@NonNull String gistId, List<CommentsModel> eventsModels) {
        if (eventsModels != null) {
            return RxPaperBook.with(BOOK_NAME).write(gistId, eventsModels);
        }
        return null;
    }

    @NonNull public static Single<ArrayList<CommentsModel>> getComments(@NonNull String gistId) {
        return RxPaperBook.with(BOOK_NAME).read(gistId, new ArrayList<>());
    }

    public static Completable delete() {
        return RxPaperBook.with(BOOK_NAME).destroy();
    }

    public static Completable delete(String gistId) {
        return RxPaperBook.with(BOOK_NAME).delete(gistId);
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.url);
        dest.writeString(this.body);
        dest.writeString(this.bodyHtml);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeInt(this.position);
        dest.writeInt(this.line);
        dest.writeString(this.path);
        dest.writeString(this.commitId);
    }

    protected CommentsModel(Parcel in) {
        this.id = in.readLong();
        this.user = in.readParcelable(UserModel.class.getClassLoader());
        this.url = in.readString();
        this.body = in.readString();
        this.bodyHtml = in.readString();
        this.htmlUrl = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.position = in.readInt();
        this.line = in.readInt();
        this.path = in.readString();
        this.commitId = in.readString();
    }

    public static final Creator<CommentsModel> CREATOR = new Creator<CommentsModel>() {
        @Override public CommentsModel createFromParcel(Parcel source) {return new CommentsModel(source);}

        @Override public CommentsModel[] newArray(int size) {return new CommentsModel[size];}
    };
}

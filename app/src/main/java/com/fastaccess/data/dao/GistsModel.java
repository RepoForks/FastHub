package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.helper.InputHelper;
import com.fastaccess.helper.Logger;
import com.fastaccess.provider.paperdb.RxPaperBook;
import com.fastaccess.ui.widgets.SpannableBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.Single;

/**
 * Created by Kosh on 12 Nov 2016, 1:38 AM
 */

public class GistsModel implements Parcelable {

    private final static transient String BOOK_NAME = GistsModel.class.getSimpleName();
    private final static transient String BOOK_KEY = "gists";
    private final static transient String USER_GISTS_BOOK_NAME = UserModel.class.getSimpleName() + "-" + GistsModel.class.getSimpleName();

    @SerializedName("url") private String url;
    @SerializedName("forks_url") private String forksUrl;
    @SerializedName("commits_url") private String commitsUrl;
    @SerializedName("id") private String gistId;
    @SerializedName("git_pull_url") private String gitPullUrl;
    @SerializedName("git_push_url") private String gitPushUrl;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("files") private HashMap<String, FilesListModel> files;
    @SerializedName("public") private boolean publicX;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("description") private String description;
    @SerializedName("comments") private int comments;
    @SerializedName("user") private UserModel user;
    @SerializedName("comments_url") private String commentsUrl;
    @SerializedName("owner") private UserModel owner;
    @SerializedName("truncated") private boolean truncated;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getForksUrl() {
        return forksUrl;
    }

    public void setForksUrl(String forksUrl) {
        this.forksUrl = forksUrl;
    }

    public String getCommitsUrl() {
        return commitsUrl;
    }

    public void setCommitsUrl(String commitsUrl) {
        this.commitsUrl = commitsUrl;
    }

    public String getGistId() {
        return gistId;
    }

    public void setGistId(String gistId) {
        this.gistId = gistId;
    }

    public String getGitPullUrl() {
        return gitPullUrl;
    }

    public void setGitPullUrl(String gitPullUrl) {
        this.gitPullUrl = gitPullUrl;
    }

    public String getGitPushUrl() {
        return gitPushUrl;
    }

    public void setGitPushUrl(String gitPushUrl) {
        this.gitPushUrl = gitPushUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public HashMap<String, FilesListModel> getFiles() {
        return files;
    }

    public void setFiles(HashMap<String, FilesListModel> files) {
        this.files = files;
    }

    public boolean isPublicX() {
        return publicX;
    }

    public void setPublicX(boolean publicX) {
        this.publicX = publicX;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    @NonNull public static Single<ArrayList<GistsModel>> getGists() {
        return RxPaperBook.with(BOOK_NAME).read(BOOK_KEY, new ArrayList<>());
    }

    @Nullable public static Observable<GistsModel> getGist(@NonNull String gistId) {
        return getGists().toObservable()
                .map(eventsModels -> {
                    if (eventsModels != null && !eventsModels.isEmpty()) {
                        int index = eventsModels.indexOf(new GistsModel(gistId));
                        Logger.e(index, eventsModels.size());
                        return eventsModels.get(index);
                    }
                    return null;
                });

    }

    public static Observable<GistsModel> save(List<GistsModel> gists) {
        if (gists != null) {
            return RxPaperBook.with(BOOK_NAME).write(BOOK_KEY, gists).toObservable();
        }
        return null;
    }

    public static Completable saveUserGists(@NonNull String login, List<GistsModel> gists) {
        return RxPaperBook.with(USER_GISTS_BOOK_NAME).write(login, gists);
    }

    @NonNull public static Single<ArrayList<GistsModel>> getUserGists(String login) {
        return RxPaperBook.with(USER_GISTS_BOOK_NAME).read(login, new ArrayList<>());
    }

    @Nullable public static Observable<GistsModel> getUserGist(@NonNull String gistId, @NonNull String login) {
        return getUserGists(login).toObservable()
                .map(eventsModels -> {
                    if (eventsModels != null && !eventsModels.isEmpty()) {
                        int index = eventsModels.indexOf(new GistsModel(gistId));
                        Logger.e(index, eventsModels.size());
                        if (index != -1) return eventsModels.get(index);
                    }
                    return null;
                });

    }

    public static void delete() {
        RxPaperBook.with(BOOK_NAME).destroy();
    }

    public static void deleteUserGists(@NonNull String login) {
        RxPaperBook.with(USER_GISTS_BOOK_NAME).delete(login);
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.forksUrl);
        dest.writeString(this.commitsUrl);
        dest.writeString(this.gistId);
        dest.writeString(this.gitPullUrl);
        dest.writeString(this.gitPushUrl);
        dest.writeString(this.htmlUrl);
        dest.writeSerializable(this.files);
        dest.writeByte(this.publicX ? (byte) 1 : (byte) 0);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.description);
        dest.writeInt(this.comments);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.commentsUrl);
        dest.writeParcelable(this.owner, flags);
        dest.writeByte(this.truncated ? (byte) 1 : (byte) 0);
    }

    public GistsModel() {}

    public GistsModel(String gistId) {
        this.gistId = gistId;
    }

    protected GistsModel(Parcel in) {
        this.url = in.readString();
        this.forksUrl = in.readString();
        this.commitsUrl = in.readString();
        this.gistId = in.readString();
        this.gitPullUrl = in.readString();
        this.gitPushUrl = in.readString();
        this.htmlUrl = in.readString();
        this.files = (HashMap<String, FilesListModel>) in.readSerializable();
        this.publicX = in.readByte() != 0;
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.description = in.readString();
        this.comments = in.readInt();
        this.user = in.readParcelable(UserModel.class.getClassLoader());
        this.commentsUrl = in.readString();
        this.owner = in.readParcelable(UserModel.class.getClassLoader());
        this.truncated = in.readByte() != 0;
    }

    public static final Creator<GistsModel> CREATOR = new Creator<GistsModel>() {
        @Override public GistsModel createFromParcel(Parcel source) {return new GistsModel(source);}

        @Override public GistsModel[] newArray(int size) {return new GistsModel[size];}
    };

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GistsModel that = (GistsModel) o;

        return url != null ? url.equals(that.url) : that.url == null;

    }

    @Override public int hashCode() {
        return url != null ? url.hashCode() : 0;
    }

    @NonNull public SpannableBuilder getDisplayTitle(boolean isFromProfile) {
        SpannableBuilder spannableBuilder = SpannableBuilder.builder();
        if (!isFromProfile) {
            if (getOwner() != null) spannableBuilder.bold(getOwner().getLogin());
            else if (getUser() != null) spannableBuilder.bold(getUser().getLogin());
        }
        if (!InputHelper.isEmpty(getDescription())) {
            if (!InputHelper.isEmpty(spannableBuilder.toString())) {
                spannableBuilder.append("/");
            }
            spannableBuilder.append(getDescription());
        }
        if (InputHelper.isEmpty(spannableBuilder.toString())) {
            if (!isFromProfile) spannableBuilder.bold("Anonymous");
            else spannableBuilder.bold("N/A");
        }
        return spannableBuilder;
    }

    public long getSize() {
        long size = 0;
        if (getFiles() != null && !getFiles().isEmpty()) {
            for (FilesListModel filesListModel : getFiles().values()) {
                if (filesListModel != null) {
                    size += filesListModel.getSize();
                }
            }
        }
        return size;
    }
}

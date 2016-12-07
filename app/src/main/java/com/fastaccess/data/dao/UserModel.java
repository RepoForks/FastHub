package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.helper.Logger;
import com.fastaccess.provider.paperdb.RxPaperBook;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.Single;

/**
 * Created by Kosh on 10 Nov 2016, 10:58 PM
 */

public class UserModel implements Parcelable {

    private final static transient String BOOK_NAME = UserModel.class.getSimpleName();
    private final static transient String BOOK_KEY = "user";

    private final static transient String OTHER_USER_BOOK_NAME = UserModel.class.getSimpleName();
    private final static transient String FOLLOWING_BOOK_NAME = "Following-" + UserModel.class.getSimpleName();
    private final static transient String FOLLOWERS_BOOK_NAME = "Follwers-" + UserModel.class.getSimpleName();


    @SerializedName("login") private String login;
    @SerializedName("id") private long userId;
    @SerializedName("avatar_url") private String avatarUrl;
    @SerializedName("gravatar_id") private String gravatarId;
    @SerializedName("url") private String url;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("followers_url") private String followersUrl;
    @SerializedName("following_url") private String followingUrl;
    @SerializedName("gists_url") private String gistsUrl;
    @SerializedName("starred_url") private String starredUrl;
    @SerializedName("subscriptions_url") private String subscriptionsUrl;
    @SerializedName("organizations_url") private String organizationsUrl;
    @SerializedName("repos_url") private String reposUrl;
    @SerializedName("events_url") private String eventsUrl;
    @SerializedName("received_events_url") private String receivedEventsUrl;
    @SerializedName("type") private String type;
    @SerializedName("name") private String name;
    @SerializedName("company") private String company;
    @SerializedName("blog") private String blog;
    @SerializedName("location") private String location;
    @SerializedName("email") private String email;
    @SerializedName("hireable") private boolean hireable;
    @SerializedName("bio") private String bio;
    @SerializedName("public_repos") private long publicRepos;
    @SerializedName("public_gists") private long publicGists;
    @SerializedName("followers") private long followers;
    @SerializedName("following") private long following;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("private_gists") private long privateGists;
    @SerializedName("total_private_repos") private long totalPrivateRepos;
    @SerializedName("owned_private_repos") private long ownedPrivateRepos;
    @SerializedName("disk_usage") private long diskUsage;
    @SerializedName("collaborators") private long collaborators;
    @SerializedName("site_admin") private boolean siteAdmin;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getGravatarId() {
        return gravatarId;
    }

    public void setGravatarId(String gravatarId) {
        this.gravatarId = gravatarId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getFollowersUrl() {
        return followersUrl;
    }

    public void setFollowersUrl(String followersUrl) {
        this.followersUrl = followersUrl;
    }

    public String getFollowingUrl() {
        return followingUrl;
    }

    public void setFollowingUrl(String followingUrl) {
        this.followingUrl = followingUrl;
    }

    public String getGistsUrl() {
        return gistsUrl;
    }

    public void setGistsUrl(String gistsUrl) {
        this.gistsUrl = gistsUrl;
    }

    public String getStarredUrl() {
        return starredUrl;
    }

    public void setStarredUrl(String starredUrl) {
        this.starredUrl = starredUrl;
    }

    public String getSubscriptionsUrl() {
        return subscriptionsUrl;
    }

    public void setSubscriptionsUrl(String subscriptionsUrl) {
        this.subscriptionsUrl = subscriptionsUrl;
    }

    public String getOrganizationsUrl() {
        return organizationsUrl;
    }

    public void setOrganizationsUrl(String organizationsUrl) {
        this.organizationsUrl = organizationsUrl;
    }

    public String getReposUrl() {
        return reposUrl;
    }

    public void setReposUrl(String reposUrl) {
        this.reposUrl = reposUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public String getReceivedEventsUrl() {
        return receivedEventsUrl;
    }

    public void setReceivedEventsUrl(String receivedEventsUrl) {
        this.receivedEventsUrl = receivedEventsUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getSiteAdmin() {
        return siteAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getHireable() {
        return hireable;
    }

    public void setHireable(boolean hireable) {
        this.hireable = hireable;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public long getPublicRepos() {
        return publicRepos;
    }

    public void setPublicRepos(long publicRepos) {
        this.publicRepos = publicRepos;
    }

    public long getPublicGists() {
        return publicGists;
    }

    public void setPublicGists(long publicGists) {
        this.publicGists = publicGists;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
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

    public long getPrivateGists() {
        return privateGists;
    }

    public void setPrivateGists(long privateGists) {
        this.privateGists = privateGists;
    }

    public long getTotalPrivateRepos() {
        return totalPrivateRepos;
    }

    public void setTotalPrivateRepos(long totalPrivateRepos) {
        this.totalPrivateRepos = totalPrivateRepos;
    }

    public long getOwnedPrivateRepos() {
        return ownedPrivateRepos;
    }

    public void setOwnedPrivateRepos(long ownedPrivateRepos) {
        this.ownedPrivateRepos = ownedPrivateRepos;
    }

    public long getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(long diskUsage) {
        this.diskUsage = diskUsage;
    }

    public long getCollaborators() {
        return collaborators;
    }

    public void setCollaborators(long collaborators) {
        this.collaborators = collaborators;
    }

    public boolean isSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(boolean siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public boolean isHireable() {
        return hireable;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.login);
        dest.writeLong(this.userId);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.gravatarId);
        dest.writeString(this.url);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.followersUrl);
        dest.writeString(this.followingUrl);
        dest.writeString(this.gistsUrl);
        dest.writeString(this.starredUrl);
        dest.writeString(this.subscriptionsUrl);
        dest.writeString(this.organizationsUrl);
        dest.writeString(this.reposUrl);
        dest.writeString(this.eventsUrl);
        dest.writeString(this.receivedEventsUrl);
        dest.writeString(this.type);
        dest.writeValue(this.siteAdmin);
        dest.writeString(this.name);
        dest.writeString(this.company);
        dest.writeString(this.blog);
        dest.writeString(this.location);
        dest.writeString(this.email);
        dest.writeValue(this.hireable);
        dest.writeString(this.bio);
        dest.writeLong(this.publicRepos);
        dest.writeLong(this.publicGists);
        dest.writeLong(this.followers);
        dest.writeLong(this.following);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeLong(this.privateGists);
        dest.writeLong(this.totalPrivateRepos);
        dest.writeLong(this.ownedPrivateRepos);
        dest.writeLong(this.diskUsage);
        dest.writeLong(this.collaborators);
    }

    public UserModel() {}

    public UserModel(String login) {
        this.login = login;
    }

    protected UserModel(Parcel in) {
        this.login = in.readString();
        this.userId = in.readLong();
        this.avatarUrl = in.readString();
        this.gravatarId = in.readString();
        this.url = in.readString();
        this.htmlUrl = in.readString();
        this.followersUrl = in.readString();
        this.followingUrl = in.readString();
        this.gistsUrl = in.readString();
        this.starredUrl = in.readString();
        this.subscriptionsUrl = in.readString();
        this.organizationsUrl = in.readString();
        this.reposUrl = in.readString();
        this.eventsUrl = in.readString();
        this.receivedEventsUrl = in.readString();
        this.type = in.readString();
        this.siteAdmin = (boolean) in.readValue(boolean.class.getClassLoader());
        this.name = in.readString();
        this.company = in.readString();
        this.blog = in.readString();
        this.location = in.readString();
        this.email = in.readString();
        this.hireable = (boolean) in.readValue(boolean.class.getClassLoader());
        this.bio = in.readString();
        this.publicRepos = in.readLong();
        this.publicGists = in.readLong();
        this.followers = in.readLong();
        this.following = in.readLong();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.privateGists = in.readLong();
        this.totalPrivateRepos = in.readLong();
        this.ownedPrivateRepos = in.readLong();
        this.diskUsage = in.readLong();
        this.collaborators = in.readLong();
    }

    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override public UserModel createFromParcel(Parcel source) {return new UserModel(source);}

        @Override public UserModel[] newArray(int size) {return new UserModel[size];}
    };

    @Override public String toString() {
        return "UserModel{" +
                "login='" + login + '\'' +
                ", userId=" + userId +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", gravatarId='" + gravatarId + '\'' +
                ", url='" + url + '\'' +
                ", htmlUrl='" + htmlUrl + '\'' +
                ", followersUrl='" + followersUrl + '\'' +
                ", followingUrl='" + followingUrl + '\'' +
                ", gistsUrl='" + gistsUrl + '\'' +
                ", starredUrl='" + starredUrl + '\'' +
                ", subscriptionsUrl='" + subscriptionsUrl + '\'' +
                ", organizationsUrl='" + organizationsUrl + '\'' +
                ", reposUrl='" + reposUrl + '\'' +
                ", eventsUrl='" + eventsUrl + '\'' +
                ", receivedEventsUrl='" + receivedEventsUrl + '\'' +
                ", type='" + type + '\'' +
                ", siteAdmin=" + siteAdmin +
                ", name='" + name + '\'' +
                ", company='" + company + '\'' +
                ", blog='" + blog + '\'' +
                ", location='" + location + '\'' +
                ", email='" + email + '\'' +
                ", hireable=" + hireable +
                ", bio='" + bio + '\'' +
                ", publicRepos=" + publicRepos +
                ", publicGists=" + publicGists +
                ", followers=" + followers +
                ", following=" + following +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", privateGists=" + privateGists +
                ", totalPrivateRepos=" + totalPrivateRepos +
                ", ownedPrivateRepos=" + ownedPrivateRepos +
                ", diskUsage=" + diskUsage +
                ", collaborators=" + collaborators +
                '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserModel userModel = (UserModel) o;

        return login != null ? login.equals(userModel.login) : userModel.login == null;

    }

    @Override public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }

    @Nullable public static UserModel getUser() {
        return RxPaperBook.with(BOOK_NAME).getBook().read(BOOK_KEY);
    }

    public static Single<UserModel> getUser(@NonNull String login) {
        return RxPaperBook.with(OTHER_USER_BOOK_NAME).read(login);
    }

    public static Observable<UserModel> getUserFromFollowers(@NonNull String login, @NonNull String userLoggedId) {
        return getFollowers(userLoggedId).toObservable().map(models -> {
            if (models != null && !models.isEmpty()) {
                int index = models.indexOf(new UserModel(login));
                Logger.e(index, models.size(), "following");
                if (index != -1) return models.get(index);
            }
            return null;
        });
    }

    public static Observable<UserModel> getUserFromFollowing(@NonNull String login, @NonNull String userLoggedId) {
        return getFollowing(userLoggedId).toObservable().map(models -> {
            if (models != null && !models.isEmpty()) {
                int index = models.indexOf(new UserModel(login));
                Logger.e(index, models.size(), "following");
                if (index != -1) return models.get(index);
            }
            return null;
        });
    }

    public static Single<List<UserModel>> getFollowers(@NonNull String login) {
        return RxPaperBook.with(FOLLOWERS_BOOK_NAME).read(login);
    }

    public static Single<List<UserModel>> getFollowing(@NonNull String login) {
        return RxPaperBook.with(FOLLOWING_BOOK_NAME).read(login);
    }

    public void save() {
        save(this);
    }

    public static void save(UserModel userModel) {
        RxPaperBook.with(BOOK_NAME).getBook().write(BOOK_KEY, userModel);
    }

    public static void delete() {
        RxPaperBook.with(BOOK_NAME).getBook().destroy();
    }

    public static void saveOtherUsers(@NonNull UserModel userModel) {
        RxPaperBook.with(OTHER_USER_BOOK_NAME).getBook().write(userModel.getLogin(), userModel);
    }

    public static void delete(@NonNull String login) {
        RxPaperBook.with(OTHER_USER_BOOK_NAME).getBook().delete(String.valueOf(login));
    }

    public static Completable saveFollowers(@NonNull String login, List<UserModel> models) {
        return RxPaperBook.with(FOLLOWERS_BOOK_NAME).write(login, models);
    }

    public static Completable saveFollowing(@NonNull String login, List<UserModel> models) {
        return RxPaperBook.with(FOLLOWING_BOOK_NAME).write(login, models);
    }

    public static Completable deleteFollowers(@NonNull String login) {
        return RxPaperBook.with(FOLLOWERS_BOOK_NAME).delete(String.valueOf(login));
    }

    public static Completable deleteFollowing(@NonNull String login) {
        return RxPaperBook.with(FOLLOWING_BOOK_NAME).delete(String.valueOf(login));
    }
}

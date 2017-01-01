package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fastaccess.helper.Logger;
import com.fastaccess.provider.paperdb.RxPaperBook;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import rx.Completable;
import rx.Observable;
import rx.Single;

public class RepoModel implements Parcelable {
    private final static transient String BOOK_NAME = RepoModel.class.getSimpleName();

    private final static transient String STARRED_BOOK_NAME = "Starred-" + RepoModel.class.getSimpleName();

    @SerializedName("id") private long repoId;
    @SerializedName("name") private String name;
    @SerializedName("full_name") private String fullName;
    @SerializedName("owner") private UserModel owner;
    @SerializedName("private") private boolean privateX;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("description") private String description;
    @SerializedName("fork") private boolean fork;
    @SerializedName("url") private String url;
    @SerializedName("forks_url") private String forksUrl;
    @SerializedName("keys_url") private String keysUrl;
    @SerializedName("collaborators_url") private String collaboratorsUrl;
    @SerializedName("teams_url") private String teamsUrl;
    @SerializedName("hooks_url") private String hooksUrl;
    @SerializedName("issue_events_url") private String issueEventsUrl;
    @SerializedName("events_url") private String eventsUrl;
    @SerializedName("assignees_url") private String assigneesUrl;
    @SerializedName("branches_url") private String branchesUrl;
    @SerializedName("tags_url") private String tagsUrl;
    @SerializedName("blobs_url") private String blobsUrl;
    @SerializedName("git_tags_url") private String gitTagsUrl;
    @SerializedName("git_refs_url") private String gitRefsUrl;
    @SerializedName("trees_url") private String treesUrl;
    @SerializedName("statuses_url") private String statusesUrl;
    @SerializedName("languages_url") private String languagesUrl;
    @SerializedName("stargazers_url") private String stargazersUrl;
    @SerializedName("contributors_url") private String contributorsUrl;
    @SerializedName("subscribers_url") private String subscribersUrl;
    @SerializedName("subscription_url") private String subscriptionUrl;
    @SerializedName("commits_url") private String commitsUrl;
    @SerializedName("git_commits_url") private String gitCommitsUrl;
    @SerializedName("comments_url") private String commentsUrl;
    @SerializedName("issue_comment_url") private String issueCommentUrl;
    @SerializedName("contents_url") private String contentsUrl;
    @SerializedName("compare_url") private String compareUrl;
    @SerializedName("merges_url") private String mergesUrl;
    @SerializedName("archive_url") private String archiveUrl;
    @SerializedName("downloads_url") private String downloadsUrl;
    @SerializedName("issues_url") private String issuesUrl;
    @SerializedName("pulls_url") private String pullsUrl;
    @SerializedName("milestones_url") private String milestonesUrl;
    @SerializedName("notifications_url") private String notificationsUrl;
    @SerializedName("labels_url") private String labelsUrl;
    @SerializedName("releases_url") private String releasesUrl;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("pushed_at") private String pushedAt;
    @SerializedName("git_url") private String gitUrl;
    @SerializedName("ssh_url") private String sshUrl;
    @SerializedName("clone_url") private String cloneUrl;
    @SerializedName("svn_url") private String svnUrl;
    @SerializedName("homepage") private String homepage;
    @SerializedName("size") private long size;
    @SerializedName("stargazers_count") private long stargazersCount;
    @SerializedName("watchers_count") private long watchersCount;
    @SerializedName("language") private String language;
    @SerializedName("has_issues") private boolean hasIssues;
    @SerializedName("has_downloads") private boolean hasDownloads;
    @SerializedName("has_wiki") private boolean hasWiki;
    @SerializedName("has_pages") private boolean hasPages;
    @SerializedName("forks_count") private long forksCount;
    @SerializedName("mirror_url") private String mirrorUrl;
    @SerializedName("open_issues_count") private long openIssuesCount;
    @SerializedName("forks") private long forks;
    @SerializedName("open_issues") private long openIssues;
    @SerializedName("watchers") private long watchers;
    @SerializedName("default_branch") private String defaultBranch;
    @SerializedName("permissions") private RepoPermissionsModel permissions;
    @SerializedName("organization") private UserModel organization;
    @SerializedName("parent") private RepoModel parent;
    @SerializedName("source") private RepoModel source;
    @SerializedName("network_count") private int networkCount;
    @SerializedName("subscribers_count") private int subsCount;
    @SerializedName("license") private LicenseModel license;

    public RepoModel(long repoId) {
        this.repoId = repoId;
    }

    public RepoModel(String name) {
        this.name = name;
    }

    public long getRepoId() {
        return repoId;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public UserModel getOwner() {
        return owner;
    }

    public void setOwner(UserModel owner) {
        this.owner = owner;
    }

    public boolean isPrivateX() {
        return privateX;
    }

    public void setPrivateX(boolean privateX) {
        this.privateX = privateX;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isFork() {
        return fork;
    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

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

    public String getKeysUrl() {
        return keysUrl;
    }

    public void setKeysUrl(String keysUrl) {
        this.keysUrl = keysUrl;
    }

    public String getCollaboratorsUrl() {
        return collaboratorsUrl;
    }

    public void setCollaboratorsUrl(String collaboratorsUrl) {
        this.collaboratorsUrl = collaboratorsUrl;
    }

    public String getTeamsUrl() {
        return teamsUrl;
    }

    public void setTeamsUrl(String teamsUrl) {
        this.teamsUrl = teamsUrl;
    }

    public String getHooksUrl() {
        return hooksUrl;
    }

    public void setHooksUrl(String hooksUrl) {
        this.hooksUrl = hooksUrl;
    }

    public String getIssueEventsUrl() {
        return issueEventsUrl;
    }

    public void setIssueEventsUrl(String issueEventsUrl) {
        this.issueEventsUrl = issueEventsUrl;
    }

    public String getEventsUrl() {
        return eventsUrl;
    }

    public void setEventsUrl(String eventsUrl) {
        this.eventsUrl = eventsUrl;
    }

    public String getAssigneesUrl() {
        return assigneesUrl;
    }

    public void setAssigneesUrl(String assigneesUrl) {
        this.assigneesUrl = assigneesUrl;
    }

    public String getBranchesUrl() {
        return branchesUrl;
    }

    public void setBranchesUrl(String branchesUrl) {
        this.branchesUrl = branchesUrl;
    }

    public String getTagsUrl() {
        return tagsUrl;
    }

    public void setTagsUrl(String tagsUrl) {
        this.tagsUrl = tagsUrl;
    }

    public String getBlobsUrl() {
        return blobsUrl;
    }

    public void setBlobsUrl(String blobsUrl) {
        this.blobsUrl = blobsUrl;
    }

    public String getGitTagsUrl() {
        return gitTagsUrl;
    }

    public void setGitTagsUrl(String gitTagsUrl) {
        this.gitTagsUrl = gitTagsUrl;
    }

    public String getGitRefsUrl() {
        return gitRefsUrl;
    }

    public void setGitRefsUrl(String gitRefsUrl) {
        this.gitRefsUrl = gitRefsUrl;
    }

    public String getTreesUrl() {
        return treesUrl;
    }

    public void setTreesUrl(String treesUrl) {
        this.treesUrl = treesUrl;
    }

    public String getStatusesUrl() {
        return statusesUrl;
    }

    public void setStatusesUrl(String statusesUrl) {
        this.statusesUrl = statusesUrl;
    }

    public String getLanguagesUrl() {
        return languagesUrl;
    }

    public void setLanguagesUrl(String languagesUrl) {
        this.languagesUrl = languagesUrl;
    }

    public String getStargazersUrl() {
        return stargazersUrl;
    }

    public void setStargazersUrl(String stargazersUrl) {
        this.stargazersUrl = stargazersUrl;
    }

    public String getContributorsUrl() {
        return contributorsUrl;
    }

    public void setContributorsUrl(String contributorsUrl) {
        this.contributorsUrl = contributorsUrl;
    }

    public String getSubscribersUrl() {
        return subscribersUrl;
    }

    public void setSubscribersUrl(String subscribersUrl) {
        this.subscribersUrl = subscribersUrl;
    }

    public String getSubscriptionUrl() {
        return subscriptionUrl;
    }

    public void setSubscriptionUrl(String subscriptionUrl) {
        this.subscriptionUrl = subscriptionUrl;
    }

    public String getCommitsUrl() {
        return commitsUrl;
    }

    public void setCommitsUrl(String commitsUrl) {
        this.commitsUrl = commitsUrl;
    }

    public String getGitCommitsUrl() {
        return gitCommitsUrl;
    }

    public void setGitCommitsUrl(String gitCommitsUrl) {
        this.gitCommitsUrl = gitCommitsUrl;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public String getIssueCommentUrl() {
        return issueCommentUrl;
    }

    public void setIssueCommentUrl(String issueCommentUrl) {
        this.issueCommentUrl = issueCommentUrl;
    }

    public String getContentsUrl() {
        return contentsUrl;
    }

    public void setContentsUrl(String contentsUrl) {
        this.contentsUrl = contentsUrl;
    }

    public String getCompareUrl() {
        return compareUrl;
    }

    public void setCompareUrl(String compareUrl) {
        this.compareUrl = compareUrl;
    }

    public String getMergesUrl() {
        return mergesUrl;
    }

    public void setMergesUrl(String mergesUrl) {
        this.mergesUrl = mergesUrl;
    }

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
    }

    public String getDownloadsUrl() {
        return downloadsUrl;
    }

    public void setDownloadsUrl(String downloadsUrl) {
        this.downloadsUrl = downloadsUrl;
    }

    public String getIssuesUrl() {
        return issuesUrl;
    }

    public void setIssuesUrl(String issuesUrl) {
        this.issuesUrl = issuesUrl;
    }

    public String getPullsUrl() {
        return pullsUrl;
    }

    public void setPullsUrl(String pullsUrl) {
        this.pullsUrl = pullsUrl;
    }

    public String getMilestonesUrl() {
        return milestonesUrl;
    }

    public void setMilestonesUrl(String milestonesUrl) {
        this.milestonesUrl = milestonesUrl;
    }

    public String getNotificationsUrl() {
        return notificationsUrl;
    }

    public void setNotificationsUrl(String notificationsUrl) {
        this.notificationsUrl = notificationsUrl;
    }

    public String getLabelsUrl() {
        return labelsUrl;
    }

    public void setLabelsUrl(String labelsUrl) {
        this.labelsUrl = labelsUrl;
    }

    public String getReleasesUrl() {
        return releasesUrl;
    }

    public void setReleasesUrl(String releasesUrl) {
        this.releasesUrl = releasesUrl;
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

    public String getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(String pushedAt) {
        this.pushedAt = pushedAt;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }

    public String getSshUrl() {
        return sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        this.sshUrl = sshUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getSvnUrl() {
        return svnUrl;
    }

    public void setSvnUrl(String svnUrl) {
        this.svnUrl = svnUrl;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public long getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public long getWatchersCount() {
        return watchersCount;
    }

    public void setWatchersCount(int watchersCount) {
        this.watchersCount = watchersCount;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isHasIssues() {
        return hasIssues;
    }

    public void setHasIssues(boolean hasIssues) {
        this.hasIssues = hasIssues;
    }

    public boolean isHasDownloads() {
        return hasDownloads;
    }

    public void setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    public boolean isHasWiki() {
        return hasWiki;
    }

    public void setHasWiki(boolean hasWiki) {
        this.hasWiki = hasWiki;
    }

    public boolean isHasPages() {
        return hasPages;
    }

    public void setHasPages(boolean hasPages) {
        this.hasPages = hasPages;
    }

    public long getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public String getMirrorUrl() {
        return mirrorUrl;
    }

    public void setMirrorUrl(String mirrorUrl) {
        this.mirrorUrl = mirrorUrl;
    }

    public long getOpenIssuesCount() {
        return openIssuesCount;
    }

    public void setOpenIssuesCount(int openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public long getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public long getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }

    public long getWatchers() {
        return watchers;
    }

    public void setWatchers(int watchers) {
        this.watchers = watchers;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public RepoPermissionsModel getPermissions() {
        return permissions;
    }

    public void setPermissions(RepoPermissionsModel permissions) {
        this.permissions = permissions;
    }

    public RepoModel() {}

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepoModel repoModel = (RepoModel) o;

        return repoId == repoModel.repoId || (name != null && name.equals(repoModel.name));

    }

    @Override public int hashCode() {
        return (int) (repoId ^ (repoId >>> 32));
    }

    public static Completable save(@NonNull String login, List<RepoModel> repos) {
        return RxPaperBook.with(BOOK_NAME).write(login, repos);
    }

    @NonNull public static Single<ArrayList<RepoModel>> getRepos(String login) {
        return RxPaperBook.with(BOOK_NAME).read(login, new ArrayList<>());
    }

    @Nullable public static Observable<RepoModel> getRepo(long repoId, @NonNull String login) {
        return getRepos(login).toObservable()
                .map(gists -> {
                    if (gists != null && !gists.isEmpty()) {
                        int index = gists.indexOf(new RepoModel(repoId));
                        Logger.e(index, gists.size());
                        if (index != -1) return gists.get(index);
                    }
                    return null;
                });

    }

    @Nullable public static Observable<RepoModel> getRepo(@NonNull String repoId, @NonNull String login) {
        return getRepos(login).toObservable()
                .map(gists -> {
                    if (gists != null && !gists.isEmpty()) {
                        int index = gists.indexOf(new RepoModel(repoId));
                        Logger.e(index, gists.size());
                        if (index != -1) return gists.get(index);
                    }
                    return null;
                });

    }

    public static Completable deleteRepo(@NonNull String login) {
        return RxPaperBook.with(BOOK_NAME).delete(login);
    }

    public static Completable saveStarred(@NonNull String login, List<RepoModel> repos) {
        return RxPaperBook.with(STARRED_BOOK_NAME).write(login, repos);
    }

    @NonNull public static Single<ArrayList<RepoModel>> getStarredRepos(String login) {
        return RxPaperBook.with(STARRED_BOOK_NAME).read(login, new ArrayList<>());
    }

    @Nullable public static Observable<RepoModel> getStarredRepo(long repoId, @NonNull String login) {
        return getStarredRepos(login).toObservable()
                .map(gists -> {
                    if (gists != null && !gists.isEmpty()) {
                        int index = gists.indexOf(new RepoModel(repoId));
                        if (index != -1) {
                            return gists.get(index);
                        }
                    }
                    return null;
                });

    }

    @Nullable public static Observable<RepoModel> getStarredRepo(@NonNull String repoId, @NonNull String login) {
        return getStarredRepos(login).toObservable()
                .map(gists -> {
                    if (gists != null && !gists.isEmpty()) {
                        int index = gists.indexOf(new RepoModel(repoId));
                        if (index != -1) {
                            return gists.get(index);
                        }
                    }
                    return null;
                });

    }

    public static Completable deleteStarredRepo(@NonNull String login) {
        return RxPaperBook.with(STARRED_BOOK_NAME).delete(login);
    }

    public UserModel getOrganization() {
        return organization;
    }

    public void setOrganization(UserModel organization) {
        this.organization = organization;
    }

    public RepoModel getParent() {
        return parent;
    }

    public void setParent(RepoModel parent) {
        this.parent = parent;
    }

    public RepoModel getSource() {
        return source;
    }

    public void setSource(RepoModel source) {
        this.source = source;
    }

    public int getSubsCount() {
        return subsCount;
    }

    public void setSubsCount(int subsCount) {
        this.subsCount = subsCount;
    }

    public int getNetworkCount() {
        return networkCount;
    }

    public void setNetworkCount(int networkCount) {
        this.networkCount = networkCount;
    }

    public LicenseModel getLicense() {
        return license;
    }

    public void setLicense(LicenseModel license) {
        this.license = license;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.repoId);
        dest.writeString(this.name);
        dest.writeString(this.fullName);
        dest.writeParcelable(this.owner, flags);
        dest.writeByte(this.privateX ? (byte) 1 : (byte) 0);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.description);
        dest.writeByte(this.fork ? (byte) 1 : (byte) 0);
        dest.writeString(this.url);
        dest.writeString(this.forksUrl);
        dest.writeString(this.keysUrl);
        dest.writeString(this.collaboratorsUrl);
        dest.writeString(this.teamsUrl);
        dest.writeString(this.hooksUrl);
        dest.writeString(this.issueEventsUrl);
        dest.writeString(this.eventsUrl);
        dest.writeString(this.assigneesUrl);
        dest.writeString(this.branchesUrl);
        dest.writeString(this.tagsUrl);
        dest.writeString(this.blobsUrl);
        dest.writeString(this.gitTagsUrl);
        dest.writeString(this.gitRefsUrl);
        dest.writeString(this.treesUrl);
        dest.writeString(this.statusesUrl);
        dest.writeString(this.languagesUrl);
        dest.writeString(this.stargazersUrl);
        dest.writeString(this.contributorsUrl);
        dest.writeString(this.subscribersUrl);
        dest.writeString(this.subscriptionUrl);
        dest.writeString(this.commitsUrl);
        dest.writeString(this.gitCommitsUrl);
        dest.writeString(this.commentsUrl);
        dest.writeString(this.issueCommentUrl);
        dest.writeString(this.contentsUrl);
        dest.writeString(this.compareUrl);
        dest.writeString(this.mergesUrl);
        dest.writeString(this.archiveUrl);
        dest.writeString(this.downloadsUrl);
        dest.writeString(this.issuesUrl);
        dest.writeString(this.pullsUrl);
        dest.writeString(this.milestonesUrl);
        dest.writeString(this.notificationsUrl);
        dest.writeString(this.labelsUrl);
        dest.writeString(this.releasesUrl);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.pushedAt);
        dest.writeString(this.gitUrl);
        dest.writeString(this.sshUrl);
        dest.writeString(this.cloneUrl);
        dest.writeString(this.svnUrl);
        dest.writeString(this.homepage);
        dest.writeLong(this.size);
        dest.writeLong(this.stargazersCount);
        dest.writeLong(this.watchersCount);
        dest.writeString(this.language);
        dest.writeByte(this.hasIssues ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasDownloads ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasWiki ? (byte) 1 : (byte) 0);
        dest.writeByte(this.hasPages ? (byte) 1 : (byte) 0);
        dest.writeLong(this.forksCount);
        dest.writeString(this.mirrorUrl);
        dest.writeLong(this.openIssuesCount);
        dest.writeLong(this.forks);
        dest.writeLong(this.openIssues);
        dest.writeLong(this.watchers);
        dest.writeString(this.defaultBranch);
        dest.writeParcelable(this.permissions, flags);
        dest.writeParcelable(this.organization, flags);
        dest.writeParcelable(this.parent, flags);
        dest.writeParcelable(this.source, flags);
        dest.writeInt(this.networkCount);
        dest.writeInt(this.subsCount);
        dest.writeParcelable(this.license, flags);
    }

    protected RepoModel(Parcel in) {
        this.repoId = in.readLong();
        this.name = in.readString();
        this.fullName = in.readString();
        this.owner = in.readParcelable(UserModel.class.getClassLoader());
        this.privateX = in.readByte() != 0;
        this.htmlUrl = in.readString();
        this.description = in.readString();
        this.fork = in.readByte() != 0;
        this.url = in.readString();
        this.forksUrl = in.readString();
        this.keysUrl = in.readString();
        this.collaboratorsUrl = in.readString();
        this.teamsUrl = in.readString();
        this.hooksUrl = in.readString();
        this.issueEventsUrl = in.readString();
        this.eventsUrl = in.readString();
        this.assigneesUrl = in.readString();
        this.branchesUrl = in.readString();
        this.tagsUrl = in.readString();
        this.blobsUrl = in.readString();
        this.gitTagsUrl = in.readString();
        this.gitRefsUrl = in.readString();
        this.treesUrl = in.readString();
        this.statusesUrl = in.readString();
        this.languagesUrl = in.readString();
        this.stargazersUrl = in.readString();
        this.contributorsUrl = in.readString();
        this.subscribersUrl = in.readString();
        this.subscriptionUrl = in.readString();
        this.commitsUrl = in.readString();
        this.gitCommitsUrl = in.readString();
        this.commentsUrl = in.readString();
        this.issueCommentUrl = in.readString();
        this.contentsUrl = in.readString();
        this.compareUrl = in.readString();
        this.mergesUrl = in.readString();
        this.archiveUrl = in.readString();
        this.downloadsUrl = in.readString();
        this.issuesUrl = in.readString();
        this.pullsUrl = in.readString();
        this.milestonesUrl = in.readString();
        this.notificationsUrl = in.readString();
        this.labelsUrl = in.readString();
        this.releasesUrl = in.readString();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.pushedAt = in.readString();
        this.gitUrl = in.readString();
        this.sshUrl = in.readString();
        this.cloneUrl = in.readString();
        this.svnUrl = in.readString();
        this.homepage = in.readString();
        this.size = in.readLong();
        this.stargazersCount = in.readLong();
        this.watchersCount = in.readLong();
        this.language = in.readString();
        this.hasIssues = in.readByte() != 0;
        this.hasDownloads = in.readByte() != 0;
        this.hasWiki = in.readByte() != 0;
        this.hasPages = in.readByte() != 0;
        this.forksCount = in.readLong();
        this.mirrorUrl = in.readString();
        this.openIssuesCount = in.readLong();
        this.forks = in.readLong();
        this.openIssues = in.readLong();
        this.watchers = in.readLong();
        this.defaultBranch = in.readString();
        this.permissions = in.readParcelable(RepoPermissionsModel.class.getClassLoader());
        this.organization = in.readParcelable(UserModel.class.getClassLoader());
        this.parent = in.readParcelable(RepoModel.class.getClassLoader());
        this.source = in.readParcelable(RepoModel.class.getClassLoader());
        this.networkCount = in.readInt();
        this.subsCount = in.readInt();
        this.license = in.readParcelable(LicenseModel.class.getClassLoader());
    }

    public static final Creator<RepoModel> CREATOR = new Creator<RepoModel>() {
        @Override public RepoModel createFromParcel(Parcel source) {return new RepoModel(source);}

        @Override public RepoModel[] newArray(int size) {return new RepoModel[size];}
    };
}
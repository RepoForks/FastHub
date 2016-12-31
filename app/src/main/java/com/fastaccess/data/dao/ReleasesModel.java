package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 31 Dec 2016, 1:28 PM
 */

public class ReleasesModel implements Parcelable {


    @SerializedName("url") private String url;
    @SerializedName("html_url") private String htmlUrl;
    @SerializedName("assets_url") private String assetsUrl;
    @SerializedName("upload_url") private String uploadUrl;
    @SerializedName("tarball_url") private String tarballUrl;
    @SerializedName("zipball_url") private String zipBallUrl;
    @SerializedName("id") private int id;
    @SerializedName("tag_name") private String tagName;
    @SerializedName("target_commitish") private String targetCommitish;
    @SerializedName("name") private String name;
    @SerializedName("body") private String body;
    @SerializedName("draft") private boolean draft;
    @SerializedName("prerelease") private boolean preRelease;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("published_at") private String publishedAt;
    @SerializedName("author") private UserModel author;
    @SerializedName("assets") private List<ReleasesAssetsModel> assets;

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url;}

    public String getHtmlUrl() { return htmlUrl;}

    public void setHtmlUrl(String htmlUrl) { this.htmlUrl = htmlUrl;}

    public String getAssetsUrl() { return assetsUrl;}

    public void setAssetsUrl(String assetsUrl) { this.assetsUrl = assetsUrl;}

    public String getUploadUrl() { return uploadUrl;}

    public void setUploadUrl(String uploadUrl) { this.uploadUrl = uploadUrl;}

    public String getTarballUrl() { return tarballUrl;}

    public void setTarballUrl(String tarballUrl) { this.tarballUrl = tarballUrl;}

    public String getZipBallUrl() { return zipBallUrl;}

    public void setZipBallUrl(String zipBallUrl) { this.zipBallUrl = zipBallUrl;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getTagName() { return tagName;}

    public void setTagName(String tagName) { this.tagName = tagName;}

    public String getTargetCommitish() { return targetCommitish;}

    public void setTargetCommitish(String targetCommitish) { this.targetCommitish = targetCommitish;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getBody() { return body;}

    public void setBody(String body) { this.body = body;}

    public boolean isDraft() { return draft;}

    public void setDraft(boolean draft) { this.draft = draft;}

    public boolean isPreRelease() { return preRelease;}

    public void setPreRelease(boolean preRelease) { this.preRelease = preRelease;}

    public String getCreatedAt() { return createdAt;}

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt;}

    public String getPublishedAt() { return publishedAt;}

    public void setPublishedAt(String publishedAt) { this.publishedAt = publishedAt;}

    public UserModel getAuthor() { return author;}

    public void setAuthor(UserModel author) { this.author = author;}

    public List<ReleasesAssetsModel> getAssets() { return assets;}

    public void setAssets(List<ReleasesAssetsModel> assets) { this.assets = assets;}

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.htmlUrl);
        dest.writeString(this.assetsUrl);
        dest.writeString(this.uploadUrl);
        dest.writeString(this.tarballUrl);
        dest.writeString(this.zipBallUrl);
        dest.writeInt(this.id);
        dest.writeString(this.tagName);
        dest.writeString(this.targetCommitish);
        dest.writeString(this.name);
        dest.writeString(this.body);
        dest.writeByte(this.draft ? (byte) 1 : (byte) 0);
        dest.writeByte(this.preRelease ? (byte) 1 : (byte) 0);
        dest.writeString(this.createdAt);
        dest.writeString(this.publishedAt);
        dest.writeParcelable(this.author, flags);
        dest.writeList(this.assets);
    }

    public ReleasesModel() {}

    protected ReleasesModel(Parcel in) {
        this.url = in.readString();
        this.htmlUrl = in.readString();
        this.assetsUrl = in.readString();
        this.uploadUrl = in.readString();
        this.tarballUrl = in.readString();
        this.zipBallUrl = in.readString();
        this.id = in.readInt();
        this.tagName = in.readString();
        this.targetCommitish = in.readString();
        this.name = in.readString();
        this.body = in.readString();
        this.draft = in.readByte() != 0;
        this.preRelease = in.readByte() != 0;
        this.createdAt = in.readString();
        this.publishedAt = in.readString();
        this.author = in.readParcelable(UserModel.class.getClassLoader());
        this.assets = new ArrayList<ReleasesAssetsModel>();
        in.readList(this.assets, ReleasesAssetsModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<ReleasesModel> CREATOR = new Parcelable.Creator<ReleasesModel>() {
        @Override public ReleasesModel createFromParcel(Parcel source) {return new ReleasesModel(source);}

        @Override public ReleasesModel[] newArray(int size) {return new ReleasesModel[size];}
    };
}

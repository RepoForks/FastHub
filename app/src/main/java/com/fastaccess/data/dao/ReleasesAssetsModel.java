package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kosh on 31 Dec 2016, 1:28 PM
 */

public class ReleasesAssetsModel implements Parcelable {
    @SerializedName("url") private String url;
    @SerializedName("browser_download_url") private String browserDownloadUrl;
    @SerializedName("id") private int id;
    @SerializedName("name") private String name;
    @SerializedName("label") private String label;
    @SerializedName("state") private String state;
    @SerializedName("content_type") private String contentType;
    @SerializedName("size") private int size;
    @SerializedName("download_count") private int downloadCount;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("uploader") private UserModel uploader;

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url;}

    public String getBrowserDownloadUrl() { return browserDownloadUrl;}

    public void setBrowserDownloadUrl(String browserDownloadUrl) { this.browserDownloadUrl = browserDownloadUrl;}

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getName() { return name;}

    public void setName(String name) { this.name = name;}

    public String getLabel() { return label;}

    public void setLabel(String label) { this.label = label;}

    public String getState() { return state;}

    public void setState(String state) { this.state = state;}

    public String getContentType() { return contentType;}

    public void setContentType(String contentType) { this.contentType = contentType;}

    public int getSize() { return size;}

    public void setSize(int size) { this.size = size;}

    public int getDownloadCount() { return downloadCount;}

    public void setDownloadCount(int downloadCount) { this.downloadCount = downloadCount;}

    public String getCreatedAt() { return createdAt;}

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt;}

    public String getUpdatedAt() { return updatedAt;}

    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt;}

    public UserModel getUploader() { return uploader;}

    public void setUploader(UserModel uploader) { this.uploader = uploader;}

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.browserDownloadUrl);
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.label);
        dest.writeString(this.state);
        dest.writeString(this.contentType);
        dest.writeInt(this.size);
        dest.writeInt(this.downloadCount);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeParcelable(this.uploader, flags);
    }

    public ReleasesAssetsModel() {}

    protected ReleasesAssetsModel(Parcel in) {
        this.url = in.readString();
        this.browserDownloadUrl = in.readString();
        this.id = in.readInt();
        this.name = in.readString();
        this.label = in.readString();
        this.state = in.readString();
        this.contentType = in.readString();
        this.size = in.readInt();
        this.downloadCount = in.readInt();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.uploader = in.readParcelable(UserModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<ReleasesAssetsModel> CREATOR = new Parcelable.Creator<ReleasesAssetsModel>() {
        @Override public ReleasesAssetsModel createFromParcel(Parcel source) {return new ReleasesAssetsModel(source);}

        @Override public ReleasesAssetsModel[] newArray(int size) {return new ReleasesAssetsModel[size];}
    };
}

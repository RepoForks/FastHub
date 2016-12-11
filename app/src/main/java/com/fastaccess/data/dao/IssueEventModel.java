package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.fastaccess.data.dao.types.IssueEventType;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kosh on 10 Dec 2016, 3:34 PM
 */

public class IssueEventModel implements Parcelable {

    private long id;
    private String url;
    private UserModel actor;
    private IssueEventType event;
    @SerializedName("commit_id") private String commitId;
    @SerializedName("commit_url") private String commitUrl;
    @SerializedName("created_at") private String createdAt;

    public long getId() { return id;}

    public void setId(long id) { this.id = id;}

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url;}

    public UserModel getActor() { return actor;}

    public void setActor(UserModel actor) { this.actor = actor;}

    public IssueEventType getEvent() { return event;}

    public void setEvent(IssueEventType event) { this.event = event;}

    public String getCommitId() { return commitId;}

    public void setCommitId(String commitId) { this.commitId = commitId;}

    public String getCommitUrl() { return commitUrl;}

    public void setCommitUrl(String commitUrl) { this.commitUrl = commitUrl;}

    public String getCreatedAt() { return createdAt;}

    public void setCreatedAt(String createdAt) { this.createdAt = createdAt;}


    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.url);
        dest.writeParcelable(this.actor, flags);
        dest.writeInt(this.event == null ? -1 : this.event.ordinal());
        dest.writeString(this.commitId);
        dest.writeString(this.commitUrl);
        dest.writeString(this.createdAt);
    }

    public IssueEventModel() {}

    protected IssueEventModel(Parcel in) {
        this.id = in.readLong();
        this.url = in.readString();
        this.actor = in.readParcelable(UserModel.class.getClassLoader());
        int tmpEvent = in.readInt();
        this.event = tmpEvent == -1 ? null : IssueEventType.values()[tmpEvent];
        this.commitId = in.readString();
        this.commitUrl = in.readString();
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<IssueEventModel> CREATOR = new Parcelable.Creator<IssueEventModel>() {
        @Override public IssueEventModel createFromParcel(Parcel source) {return new IssueEventModel(source);}

        @Override public IssueEventModel[] newArray(int size) {return new IssueEventModel[size];}
    };
}

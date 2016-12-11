package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kosh on 08 Dec 2016, 8:47 PM
 */

public class MilestoneModel implements Parcelable {

    private String url;
    private String title;
    private String state;
    private String description;
    private int id;
    private int number;
    private UserModel creator;
    @SerializedName("html_url") private String htmlUr;
    @SerializedName("open_issues") private int openIssues;
    @SerializedName("closed_issues") private int closedIssues;
    @SerializedName("created_at") private String createdAt;
    @SerializedName("updated_at") private String updatedAt;
    @SerializedName("closed_at") private String closedAt;
    @SerializedName("due_on") private String dueOn;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public UserModel getCreator() {
        return creator;
    }

    public void setCreator(UserModel creator) {
        this.creator = creator;
    }

    public String getHtmlUr() {
        return htmlUr;
    }

    public void setHtmlUr(String htmlUr) {
        this.htmlUr = htmlUr;
    }

    public int getOpenIssues() {
        return openIssues;
    }

    public void setOpenIssues(int openIssues) {
        this.openIssues = openIssues;
    }

    public int getClosedIssues() {
        return closedIssues;
    }

    public void setClosedIssues(int closedIssues) {
        this.closedIssues = closedIssues;
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

    public String getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(String closedAt) {
        this.closedAt = closedAt;
    }

    public String getDueOn() {
        return dueOn;
    }

    public void setDueOn(String dueOn) {
        this.dueOn = dueOn;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.state);
        dest.writeString(this.description);
        dest.writeInt(this.id);
        dest.writeInt(this.number);
        dest.writeParcelable(this.creator, flags);
        dest.writeString(this.htmlUr);
        dest.writeInt(this.openIssues);
        dest.writeInt(this.closedIssues);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.closedAt);
        dest.writeString(this.dueOn);
    }

    public MilestoneModel() {}

    protected MilestoneModel(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.state = in.readString();
        this.description = in.readString();
        this.id = in.readInt();
        this.number = in.readInt();
        this.creator = in.readParcelable(UserModel.class.getClassLoader());
        this.htmlUr = in.readString();
        this.openIssues = in.readInt();
        this.closedIssues = in.readInt();
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.closedAt = in.readString();
        this.dueOn = in.readString();
    }

    public static final Parcelable.Creator<MilestoneModel> CREATOR = new Parcelable.Creator<MilestoneModel>() {
        @Override public MilestoneModel createFromParcel(Parcel source) {return new MilestoneModel(source);}

        @Override public MilestoneModel[] newArray(int size) {return new MilestoneModel[size];}
    };
}

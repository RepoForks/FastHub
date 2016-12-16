package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 10 Dec 2016, 3:34 PM
 */

public class PullRequestAdapterModel implements Parcelable {

    public static final int HEADER = 1;
    public static final int ROW = 2;
    private int type;

    private IssueEventModel issueEvent;
    private PullRequestModel pullRequest;

    public PullRequestAdapterModel(int type, IssueEventModel model) {
        this.type = type;
        this.issueEvent = model;
    }

    public PullRequestAdapterModel(int type, PullRequestModel pullRequest) {
        this.type = type;
        this.pullRequest = pullRequest;
    }

    public static int getHEADER() {
        return HEADER;
    }

    public static int getROW() {
        return ROW;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public IssueEventModel getIssueEvent() {
        return issueEvent;
    }

    public void setIssueEvent(IssueEventModel issueEvent) {
        this.issueEvent = issueEvent;
    }

    public PullRequestModel getPullRequest() {
        return pullRequest;
    }

    public void setPullRequest(PullRequestModel pullRequest) {
        this.pullRequest = pullRequest;
    }

    public static ArrayList<PullRequestAdapterModel> addEvents(@Nullable List<IssueEventModel> modelList) {
        ArrayList<PullRequestAdapterModel> models = new ArrayList<>();
        if (modelList == null || modelList.isEmpty()) return models;
        Stream.of(modelList).forEach(issueEventModel -> models.add(new PullRequestAdapterModel(ROW, issueEventModel)));
        return models;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeParcelable(this.issueEvent, flags);
        dest.writeParcelable(this.pullRequest, flags);
    }

    public PullRequestAdapterModel() {}

    protected PullRequestAdapterModel(Parcel in) {
        this.type = in.readInt();
        this.issueEvent = in.readParcelable(IssueEventModel.class.getClassLoader());
        this.pullRequest = in.readParcelable(IssueModel.class.getClassLoader());
    }

    public static final Creator<PullRequestAdapterModel> CREATOR = new Creator<PullRequestAdapterModel>() {
        @Override public PullRequestAdapterModel createFromParcel(Parcel source) {return new PullRequestAdapterModel(source);}

        @Override public PullRequestAdapterModel[] newArray(int size) {return new PullRequestAdapterModel[size];}
    };
}

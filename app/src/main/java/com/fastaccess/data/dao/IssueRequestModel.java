package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.fastaccess.data.dao.types.IssueState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kosh on 10 Dec 2016, 8:53 AM
 */

public class IssueRequestModel implements Parcelable {


    private IssueState state;
    private String title;
    private String body;
    private int milestone;
    private String assignee;
    private List<String> labels;

    public IssueState getState() {
        return state;
    }

    public void setState(IssueState state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getMilestone() {
        return milestone;
    }

    public void setMilestone(int milestone) {
        this.milestone = milestone;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public IssueRequestModel() {}

    public static IssueRequestModel clone(@NonNull IssueModel issue) {
        IssueRequestModel model = new IssueRequestModel();
        List<String> labels = new ArrayList<>();
        if (issue.getLabels() != null) {
            Stream.of(issue.getLabels()).map(LabelModel::getName).collect(Collectors.toList());
            model.setLabels(labels);
        }
        model.setAssignee(issue.getAssignee() != null ? issue.getAssignee().getLogin() : null);
        model.setBody(issue.getBody());
        model.setMilestone(issue.getMilestone() != null ? issue.getMilestone().getNumber() : 0);
        model.setState(issue.getState() == IssueState.closed ? IssueState.open : IssueState.closed);
        model.setTitle(issue.getTitle());
        return model;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.state == null ? -1 : this.state.ordinal());
        dest.writeString(this.title);
        dest.writeString(this.body);
        dest.writeInt(this.milestone);
        dest.writeString(this.assignee);
        dest.writeStringList(this.labels);
    }

    protected IssueRequestModel(Parcel in) {
        int tmpState = in.readInt();
        this.state = tmpState == -1 ? null : IssueState.values()[tmpState];
        this.title = in.readString();
        this.body = in.readString();
        this.milestone = in.readInt();
        this.assignee = in.readString();
        this.labels = in.createStringArrayList();
    }

    public static final Creator<IssueRequestModel> CREATOR = new Creator<IssueRequestModel>() {
        @Override public IssueRequestModel createFromParcel(Parcel source) {return new IssueRequestModel(source);}

        @Override public IssueRequestModel[] newArray(int size) {return new IssueRequestModel[size];}
    };
}

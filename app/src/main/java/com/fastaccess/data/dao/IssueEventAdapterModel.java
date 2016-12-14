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

public class IssueEventAdapterModel implements Parcelable {

    public static final int HEADER = 1;
    public static final int ROW = 2;
    private int type;

    private IssueEventModel issueEvent;
    private IssueModel issueModel;

    public IssueEventAdapterModel(int type, IssueEventModel model) {
        this.type = type;
        this.issueEvent = model;
    }

    public IssueEventAdapterModel(int type, IssueModel issueModel) {
        this.type = type;
        this.issueModel = issueModel;
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

    public IssueModel getIssueModel() {
        return issueModel;
    }

    public void setIssueModel(IssueModel issueModel) {
        this.issueModel = issueModel;
    }

    public static ArrayList<IssueEventAdapterModel> addEvents(@Nullable List<IssueEventModel> modelList) {
        ArrayList<IssueEventAdapterModel> models = new ArrayList<>();
        if (modelList == null || modelList.isEmpty()) return models;
        Stream.of(modelList).forEach(issueEventModel -> models.add(new IssueEventAdapterModel(ROW, issueEventModel)));
        return models;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeParcelable(this.issueEvent, flags);
        dest.writeParcelable(this.issueModel, flags);
    }

    public IssueEventAdapterModel() {}

    protected IssueEventAdapterModel(Parcel in) {
        this.type = in.readInt();
        this.issueEvent = in.readParcelable(IssueEventModel.class.getClassLoader());
        this.issueModel = in.readParcelable(IssueModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<IssueEventAdapterModel> CREATOR = new Parcelable.Creator<IssueEventAdapterModel>() {
        @Override public IssueEventAdapterModel createFromParcel(Parcel source) {return new IssueEventAdapterModel(source);}

        @Override public IssueEventAdapterModel[] newArray(int size) {return new IssueEventAdapterModel[size];}
    };
}

package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kosh on 10 Dec 2016, 7:53 PM
 */

public class ContentCommitModel implements Parcelable {
    private ContentModel content;
    private CommitModel commit;

    public ContentModel getContent() {
        return content;
    }

    public void setContent(ContentModel content) {
        this.content = content;
    }

    public CommitModel getCommit() {
        return commit;
    }

    public void setCommit(CommitModel commit) {
        this.commit = commit;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.content, flags);
        dest.writeParcelable(this.commit, flags);
    }

    public ContentCommitModel() {}

    protected ContentCommitModel(Parcel in) {
        this.content = in.readParcelable(ContentModel.class.getClassLoader());
        this.commit = in.readParcelable(CommitModel.class.getClassLoader());
    }

    public static final Parcelable.Creator<ContentCommitModel> CREATOR = new Parcelable.Creator<ContentCommitModel>() {
        @Override public ContentCommitModel createFromParcel(Parcel source) {return new ContentCommitModel(source);}

        @Override public ContentCommitModel[] newArray(int size) {return new ContentCommitModel[size];}
    };
}

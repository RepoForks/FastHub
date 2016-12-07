package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by Kosh on 27 Nov 2016, 7:20 PM
 */

public class CreateGistModel implements Parcelable {

    private HashMap<String, FilesListModel> files;
    private String description;
    @SerializedName("public") private boolean publicGist;

    public HashMap<String, FilesListModel> getFiles() {
        return files;
    }

    public void setFiles(HashMap<String, FilesListModel> files) {
        this.files = files;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublicGist() {
        return publicGist;
    }

    public void setPublicGist(boolean publicGist) {
        this.publicGist = publicGist;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.files);
        dest.writeString(this.description);
        dest.writeByte(this.publicGist ? (byte) 1 : (byte) 0);
    }

    public CreateGistModel() {}

    protected CreateGistModel(Parcel in) {
        this.files = (HashMap<String, FilesListModel>) in.readSerializable();
        this.description = in.readString();
        this.publicGist = in.readByte() != 0;
    }

    public static final Parcelable.Creator<CreateGistModel> CREATOR = new Parcelable.Creator<CreateGistModel>() {
        @Override public CreateGistModel createFromParcel(Parcel source) {return new CreateGistModel(source);}

        @Override public CreateGistModel[] newArray(int size) {return new CreateGistModel[size];}
    };
}

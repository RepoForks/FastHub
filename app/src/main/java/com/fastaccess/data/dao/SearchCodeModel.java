package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kosh on 08 Dec 2016, 8:45 PM
 */

public class SearchCodeModel implements Parcelable {

    private String name;
    private String path;
    private String sha;
    private String url;
    private RepoModel repository;
    private double score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RepoModel getRepository() {
        return repository;
    }

    public void setRepository(RepoModel repository) {
        this.repository = repository;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeString(this.sha);
        dest.writeString(this.url);
        dest.writeParcelable(this.repository, flags);
        dest.writeDouble(this.score);
    }

    public SearchCodeModel() {}

    protected SearchCodeModel(Parcel in) {
        this.name = in.readString();
        this.path = in.readString();
        this.sha = in.readString();
        this.url = in.readString();
        this.repository = in.readParcelable(RepoModel.class.getClassLoader());
        this.score = in.readDouble();
    }

    public static final Parcelable.Creator<SearchCodeModel> CREATOR = new Parcelable.Creator<SearchCodeModel>() {
        @Override public SearchCodeModel createFromParcel(Parcel source) {return new SearchCodeModel(source);}

        @Override public SearchCodeModel[] newArray(int size) {return new SearchCodeModel[size];}
    };
}

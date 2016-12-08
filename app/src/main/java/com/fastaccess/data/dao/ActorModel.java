package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ActorModel implements Parcelable {
    @SerializedName("id") private int id;
    @SerializedName("login") private String login;
    @SerializedName("display_login") private String displayLogin;
    @SerializedName("gravatar_id") private String gravatarId;
    @SerializedName("url") private String url;
    @SerializedName("avatar_url") private String avatarUrl;
    private double score;

    public int getId() { return id;}

    public void setId(int id) { this.id = id;}

    public String getLogin() { return login;}

    public void setLogin(String login) { this.login = login;}

    public String getDisplayLogin() { return displayLogin;}

    public void setDisplayLogin(String displayLogin) { this.displayLogin = displayLogin;}

    public String getGravatarId() { return gravatarId;}

    public void setGravatarId(String gravatarId) { this.gravatarId = gravatarId;}

    public String getUrl() { return url;}

    public void setUrl(String url) { this.url = url;}

    public String getAvatarUrl() { return avatarUrl;}

    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl;}

    public ActorModel() {}

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.login);
        dest.writeString(this.displayLogin);
        dest.writeString(this.gravatarId);
        dest.writeString(this.url);
        dest.writeString(this.avatarUrl);
        dest.writeDouble(this.score);
    }

    protected ActorModel(Parcel in) {
        this.id = in.readInt();
        this.login = in.readString();
        this.displayLogin = in.readString();
        this.gravatarId = in.readString();
        this.url = in.readString();
        this.avatarUrl = in.readString();
        this.score = in.readDouble();
    }

    public static final Creator<ActorModel> CREATOR = new Creator<ActorModel>() {
        @Override public ActorModel createFromParcel(Parcel source) {return new ActorModel(source);}

        @Override public ActorModel[] newArray(int size) {return new ActorModel[size];}
    };
}

package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fastaccess.provider.paperdb.RxPaperBook;

import rx.Completable;
import rx.Single;

/**
 * Created by Kosh on 06 Dec 2016, 10:42 PM
 */

public class FileModel implements Parcelable {

    private final static transient String BOOK_NAME = FileModel.class.getSimpleName();

    private boolean isMarkdown;
    private String content;
    private String fileName;
    private String id;
    private boolean isRepo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isMarkdown() {
        return isMarkdown;
    }

    public void setMarkdown(boolean markdown) {
        isMarkdown = markdown;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FileModel() {}

    public static Completable save(@NonNull FileModel fileModel) {
        return RxPaperBook.with(BOOK_NAME).write(getSafeKey(fileModel.getId(), fileModel.getFileName()), fileModel);
    }

    public static Single<FileModel> getFile(@NonNull String id, @NonNull String fileName) {
        return RxPaperBook.with(BOOK_NAME).read(getSafeKey(id, fileName));
    }

    private static String getSafeKey(@NonNull String id, @NonNull String fileName) {
        String key = id + "_" + fileName;
        return key.length() < 80 ? key : key.substring(0, 80);
    }

    public boolean isRepo() {
        return isRepo;
    }

    public void setRepo(boolean repo) {
        isRepo = repo;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isMarkdown ? (byte) 1 : (byte) 0);
        dest.writeString(this.content);
        dest.writeString(this.fileName);
        dest.writeString(this.id);
        dest.writeByte(this.isRepo ? (byte) 1 : (byte) 0);
    }

    protected FileModel(Parcel in) {
        this.isMarkdown = in.readByte() != 0;
        this.content = in.readString();
        this.fileName = in.readString();
        this.id = in.readString();
        this.isRepo = in.readByte() != 0;
    }

    public static final Creator<FileModel> CREATOR = new Creator<FileModel>() {
        @Override public FileModel createFromParcel(Parcel source) {return new FileModel(source);}

        @Override public FileModel[] newArray(int size) {return new FileModel[size];}
    };
}

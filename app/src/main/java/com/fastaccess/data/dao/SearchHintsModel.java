package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fastaccess.provider.paperdb.RxPaperBook;

import java.util.List;

import rx.Completable;
import rx.Single;

/**
 * Created by Kosh on 01 Jan 2017, 11:20 PM
 */

public class SearchHintsModel implements Parcelable {

    private final static transient String BOOK_NAME = SearchHintsModel.class.getSimpleName();


    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {dest.writeString(this.text);}

    public SearchHintsModel() {}

    protected SearchHintsModel(Parcel in) {this.text = in.readString();}

    public static final Parcelable.Creator<SearchHintsModel> CREATOR = new Parcelable.Creator<SearchHintsModel>() {
        @Override public SearchHintsModel createFromParcel(Parcel source) {return new SearchHintsModel(source);}

        @Override public SearchHintsModel[] newArray(int size) {return new SearchHintsModel[size];}
    };


    public static Completable saveHints(@NonNull String text) {
        return RxPaperBook.with(BOOK_NAME).write(text, text);
    }

    public static Single<List<String>> getHints() {
        return RxPaperBook.with(BOOK_NAME).keys();
    }

}

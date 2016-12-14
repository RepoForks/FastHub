/*
 * Copyright 2015 Henrik Olsson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fastaccess.data.dao;

import android.os.Parcel;
import android.os.Parcelable;

public class MarkdownModel implements Parcelable {

    public static final String MODE_MARKDOWN = "markdown";
    public static final String MODE_GFM = "gfm";

    private String text;
    private String mode;
    private String context;

    public static String getModeMarkdown() {
        return MODE_MARKDOWN;
    }

    public static String getModeGfm() {
        return MODE_GFM;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMode() {
        return mode == null ? MODE_GFM : mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.mode);
        dest.writeString(this.context);
    }

    public MarkdownModel() {}

    protected MarkdownModel(Parcel in) {
        this.text = in.readString();
        this.mode = in.readString();
        this.context = in.readString();
    }

    public static final Creator<MarkdownModel> CREATOR = new Creator<MarkdownModel>() {
        @Override public MarkdownModel createFromParcel(Parcel source) {return new MarkdownModel(source);}

        @Override public MarkdownModel[] newArray(int size) {return new MarkdownModel[size];}
    };
}

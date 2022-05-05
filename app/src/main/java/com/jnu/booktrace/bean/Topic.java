package com.jnu.booktrace.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Topic implements Parcelable {
    private String title;
    private String content;
    public Topic(String title,String content){
        this.title = title;
        this.content = content;
    }

    protected Topic(Parcel in) {
        title = in.readString();
        content = in.readString();
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
    }
}

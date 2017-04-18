package io.keypunchers.xa.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class LatestScreenshot implements Parcelable {
    private String title;
    private String imageUrl;
    private String date;
    private String gamePermalink;

    public LatestScreenshot() {
    }

    protected LatestScreenshot(Parcel in) {
        title = in.readString();
        imageUrl = in.readString();
        date = in.readString();
        gamePermalink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageUrl);
        dest.writeString(date);
        dest.writeString(gamePermalink);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LatestScreenshot> CREATOR = new Creator<LatestScreenshot>() {
        @Override
        public LatestScreenshot createFromParcel(Parcel in) {
            return new LatestScreenshot(in);
        }

        @Override
        public LatestScreenshot[] newArray(int size) {
            return new LatestScreenshot[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGamePermalink() {
        return gamePermalink;
    }

    public void setGamePermalink(String gamePermalink) {
        this.gamePermalink = gamePermalink;
    }
}

package io.keypunchers.xa.models;

import android.os.Parcel;
import android.os.Parcelable;


public class UpcomingGame implements Parcelable {
    private String title;
    private String date;
    private String url;

    public UpcomingGame() {
    }

    protected UpcomingGame(Parcel in) {
        title = in.readString();
        date = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(date);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UpcomingGame> CREATOR = new Creator<UpcomingGame>() {
        @Override
        public UpcomingGame createFromParcel(Parcel in) {
            return new UpcomingGame(in);
        }

        @Override
        public UpcomingGame[] newArray(int size) {
            return new UpcomingGame[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

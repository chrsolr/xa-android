package me.christiansoler.xa.models;

import android.os.Parcel;
import android.os.Parcelable;


public class UpcomingGame implements Parcelable {
    private String mTitle;
    private String mReleaseDate;
    private String mGamePermalink;

    public UpcomingGame() {
    }

    protected UpcomingGame(Parcel in) {
        mTitle = in.readString();
        mReleaseDate = in.readString();
        mGamePermalink = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mGamePermalink);
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
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String date) {
        this.mReleaseDate = date;
    }

    public String getGamePermalink() {
        return mGamePermalink;
    }

    public void setGamePermalink(String url) {
        this.mGamePermalink = url.replace("/game/", "").replace("/overview/", "");
    }
}

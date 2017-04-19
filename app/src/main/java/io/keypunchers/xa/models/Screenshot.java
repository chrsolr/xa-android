package io.keypunchers.xa.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Screenshot implements Parcelable {
    private String mTitle;
    private String mDateAdded;
    private String mImageUrl;
    private String mGamePermalink;

    public Screenshot() {
    }

    public Screenshot(Parcel in) {
        mTitle = in.readString();
        mDateAdded = in.readString();
        mImageUrl = in.readString();
        mGamePermalink = in.readString();
    }

    public static final Creator<Screenshot> CREATOR = new Creator<Screenshot>() {
        @Override
        public Screenshot createFromParcel(Parcel in) {
            return new Screenshot(in);
        }

        @Override
        public Screenshot[] newArray(int size) {
            return new Screenshot[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mDateAdded);
        dest.writeString(mImageUrl);
        dest.writeString(mGamePermalink);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getDateAdded() {
        return mDateAdded;
    }

    public void setDateAdded(String date) {
        this.mDateAdded = date;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String url) {
        this.mImageUrl = url;
    }
	
	public String getGamePermalink() {
        return mGamePermalink;
    }

    public void setGamePermalink(String url) {
        this.mGamePermalink = url.replace("/game/", "").replace("/screenshots/", "");
    }
}

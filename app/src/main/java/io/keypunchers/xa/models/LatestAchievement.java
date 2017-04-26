package io.keypunchers.xa.models;

import android.os.Parcel;
import android.os.Parcelable;
import io.keypunchers.xa.misc.*;
import io.keypunchers.xa.misc.Enums.*;

public class LatestAchievement implements Parcelable {
    private String imageUrl;
    private String title;
    private String achievementsCount;
    private String gamerscoreCount;
    private String submittedBy;
    private String gamePermalink;
    private String commentCount;
    private String dateAdded;

    public LatestAchievement() {
    }

    protected LatestAchievement(Parcel in) {
        imageUrl = in.readString();
        title = in.readString();
        achievementsCount = in.readString();
        submittedBy = in.readString();
        gamePermalink = in.readString();
        commentCount = in.readString();
        dateAdded = in.readString();
        gamerscoreCount = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(achievementsCount);
        dest.writeString(submittedBy);
        dest.writeString(gamePermalink);
        dest.writeString(commentCount);
        dest.writeString(dateAdded);
        dest.writeString(gamerscoreCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LatestAchievement> CREATOR = new Creator<LatestAchievement>() {
        @Override
        public LatestAchievement createFromParcel(Parcel in) {
            return new LatestAchievement(in);
        }

        @Override
        public LatestAchievement[] newArray(int size) {
            return new LatestAchievement[size];
        }
    };

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAchievementsCount() {
        return achievementsCount;
    }

    public void setAchievementsCount(String achievementsCount) {
        this.achievementsCount = achievementsCount;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getGamerscoreCount() {
        return gamerscoreCount;
    }

    public void setGamerscoreCount(String gamerscoreCount) {
        this.gamerscoreCount = gamerscoreCount;
    }

	public String getGamePermalink() {
        return gamePermalink;
    }

    public void setGamePermalink(String url) {
        this.gamePermalink = Common.createPermalink(url, UrlType.LATEST_ACHIEVEMENTS);
    }
}

package io.keypunchers.xa.models;

import android.os.Parcel;
import android.os.Parcelable;

public class LatestAchievement implements Parcelable {
    private String imageUrl;
    private String title;
    private String subtitle;
    private String submittedBy;
    private String url;
    private String commentCount;
    private String dateAdded;

    public LatestAchievement() {
    }

    protected LatestAchievement(Parcel in) {
        imageUrl = in.readString();
        title = in.readString();
        subtitle = in.readString();
        submittedBy = in.readString();
        url = in.readString();
        commentCount = in.readString();
        dateAdded = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(submittedBy);
        dest.writeString(url);
        dest.writeString(commentCount);
        dest.writeString(dateAdded);
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

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubmittedBy() {
        return submittedBy;
    }

    public void setSubmittedBy(String submittedBy) {
        this.submittedBy = submittedBy;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}

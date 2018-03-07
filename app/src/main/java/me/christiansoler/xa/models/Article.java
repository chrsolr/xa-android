package me.christiansoler.xa.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Article implements Parcelable {
    private String authorProfileUrl;
    private String headerTitle;
    private String headerDate;
    private String authorProfileImageUrl;
    private String authorFirstName;
    private String authorLastName;
    private String bodyText;
    private ArrayList<String> imageUrls;
    private ArrayList<String> videoUrls;
    private ArrayList<Comment> comments;

    public Article() {

    }

    public Article(Parcel in) {
        authorProfileUrl = in.readString();
        headerTitle = in.readString();
        headerDate = in.readString();
        authorProfileImageUrl = in.readString();
        bodyText = in.readString();
        authorFirstName = in.readString();
        authorLastName = in.readString();
        imageUrls = in.readArrayList(null);
        videoUrls = in.readArrayList(null);
        comments = in.readArrayList(null);
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(authorProfileUrl);
        dest.writeString(headerTitle);
        dest.writeString(headerDate);
        dest.writeString(authorProfileImageUrl);
        dest.writeString(bodyText);
        dest.writeString(authorFirstName);
        dest.writeString(authorLastName);
        dest.writeStringList(imageUrls);
        dest.writeStringList(videoUrls);
        dest.writeTypedList(comments);
    }

    public String getAuthorProfileUrl() {
        return authorProfileUrl;
    }

    public void setAuthorProfileUrl(String authorProfileUrl) {
        this.authorProfileUrl = authorProfileUrl;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public String getHeaderDate() {
        return headerDate;
    }

    public void setHeaderDate(String headerDate) {
        this.headerDate = headerDate;
    }

    public String getAuthorProfileImageUrl() {
        return authorProfileImageUrl;
    }

    public void setAuthorProfileImageUrl(String authorProfileImageUrl) {
        this.authorProfileImageUrl = authorProfileImageUrl;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ArrayList<String> getVideoUrls() {
        return videoUrls;
    }

    public void setVideoUrls(ArrayList<String> videoUrls) {
        this.videoUrls = videoUrls;
    }

    public String getBodyText() {
        return bodyText;
    }

    public void setBodyText(String bodyText) {
        this.bodyText = bodyText;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }
}

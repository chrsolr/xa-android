package io.keypunchers.xa.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ArticleListItem implements Parcelable{
    private String title;
    private String author;
    private String desc;
    private String pageUrl;
    private String imageUrl;

    public ArticleListItem() {
    }

    public ArticleListItem(Parcel in) {
        title = in.readString();
        author = in.readString();
        desc = in.readString();
        pageUrl = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<ArticleListItem> CREATOR = new Creator<ArticleListItem>() {
        @Override
        public ArticleListItem createFromParcel(Parcel in) {
            return new ArticleListItem(in);
        }

        @Override
        public ArticleListItem[] newArray(int size) {
            return new ArticleListItem[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(desc);
        dest.writeString(pageUrl);
        dest.writeString(imageUrl);
    }
}

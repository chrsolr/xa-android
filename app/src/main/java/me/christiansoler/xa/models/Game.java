package me.christiansoler.xa.models;

import me.christiansoler.xa.misc.Common;
import me.christiansoler.xa.misc.Enums.*;

public class Game {
    private String artwork;
    private String title;
    private String achCount;
    private String gsCount;
    private String gamePermalink;

    public String getArtwork() {
        return artwork;
    }

    public void setArtwork(String artwork) {
        this.artwork = artwork;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAchCount() {
        return achCount;
    }

    public void setAchCount(String achCount) {
        this.achCount = achCount;
    }

    public String getGsCount() {
        return gsCount;
    }

    public void setGsCount(String gsCount) {
        this.gsCount = gsCount;
    }

    public String getGamePermalink() {
        return gamePermalink;
    }

    public void setGamePermalink(String url) {
        this.gamePermalink = Common.createPermalink(url, UrlType.BROWSE_GAMES);
    }
}
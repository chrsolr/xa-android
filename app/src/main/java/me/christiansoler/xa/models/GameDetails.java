package me.christiansoler.xa.models;

import java.util.ArrayList;
import java.util.Map;

import me.christiansoler.xa.misc.Enums;

public class GameDetails {
    private String title;
    private String imageUrl;
    private String banner;
    private String developer;
    private String publisher;
    private ArrayList<String> genres;
    private Map<Enums.Country, String> releaseDates;
	private ArrayList<Achievement> achievements;
	private boolean hasScreenshots;
	private boolean hasVideos;

	public void setHasVideos(boolean hasVideos) {
		this.hasVideos = hasVideos;
	}

	public boolean hasVideos() {
		return hasVideos;
	}

	public void setHasScreenshots(boolean hasScreenshots) {
		this.hasScreenshots = hasScreenshots;
	}

	public boolean hasScreenshots() {
		return hasScreenshots;
	}

	public void setAchievements(ArrayList<Achievement> achievements) {
		this.achievements = achievements;
	}

	public ArrayList<Achievement> getAchievements() {
		return achievements;
	}

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

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<String> genres) {
        this.genres = genres;
    }

    public Map<Enums.Country, String> getReleaseDates() {
        return releaseDates;
    }

    public void setReleaseDates(Map<Enums.Country, String> releaseDates) {
        this.releaseDates = releaseDates;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }
}

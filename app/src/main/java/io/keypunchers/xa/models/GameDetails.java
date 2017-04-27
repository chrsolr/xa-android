package io.keypunchers.xa.models;

import java.util.ArrayList;
import java.util.Map;

import io.keypunchers.xa.misc.Enums;

public class GameDetails {
    private String title;
    private String imageUrl;
    private String developer;
    private String publisher;
    private ArrayList<String> genres;
    private Map<Enums.Country, String> releaseDates;

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
}

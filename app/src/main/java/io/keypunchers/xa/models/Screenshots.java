package io.keypunchers.xa.models;

import java.util.ArrayList;

public class Screenshots {
    private String title;
    private ArrayList<String> imageUrls;

    public Screenshots() {
        title = "";
        imageUrls = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}

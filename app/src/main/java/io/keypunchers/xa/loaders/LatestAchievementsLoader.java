package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

import io.keypunchers.xa.models.LatestAchievement;

public class LatestAchievementsLoader extends AsyncTaskLoader<ArrayList<LatestAchievement>> {
    private final String BASE_URL;
    private ArrayList<LatestAchievement> mData = new ArrayList<>();

    public LatestAchievementsLoader(Context context, String url) {
        super(context);
        BASE_URL = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
		
		if (!mData.isEmpty())
			deliverResult(mData);
		else
        	forceLoad();
    }

    @Override
    public void deliverResult(ArrayList<LatestAchievement> data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }

    @Override
    public ArrayList<LatestAchievement> loadInBackground() {
        try {
            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Elements rows = document.getElementsByClass("divtext")
                    .first()
                    .getElementsByTag("table")
                    .first()
                    .getElementsByTag("tr");

            if (!rows.isEmpty()) {
                for (int i = 0; i < rows.size(); i++) {
                    String imageUrl = rows.get(i + 1).select("td:eq(0) > img").attr("abs:src").replace("/game/", "/achievements/");
                    String title = rows.get(i).select(".newsTitle").text().trim();
                    String url = rows.get(i + 1).select("td:eq(1) > a").attr("abs:href");
                    String dateAdded = rows.get(i).select(".newsNFO").text().trim();
                    String subtitle = "";
                    String submittedBy = "";
                    String comment_count = "";

                    title = title.replace("Game Added: ", "").replace("DLC Added: ", "").replace("DLCs Added: ", "");
                    dateAdded = dateAdded.replace("Added: ", "");

                    if (rows.get(i + 1).select("td:eq(1) > p").isEmpty()) {
                        subtitle = rows.get(i + 1).select("td:eq(1)").get(0).textNodes().get(0).text().trim();
                        submittedBy = rows.get(i + 1).select("td:eq(1) > strong").eq(0).text().trim();
                    } else {
                        subtitle = rows.get(i + 1).select("td:eq(1) > p").get(0).textNodes().get(0).text().trim();
                        submittedBy = rows.get(i + 1).select("td:eq(1) > p > strong").eq(0).text().trim();
                    }

                    LatestAchievement item = new LatestAchievement();
                    item.setImageUrl(imageUrl);
                    item.setTitle(title);
                    item.setAchievementsCount(subtitle.split(", ")[0].trim());
                    item.setGamerscoreCount(subtitle.split(", ")[1].replace(".", "").trim());
                    item.setSubmittedBy(submittedBy);
                    item.setDateAdded(dateAdded);
                    item.setUrl(url);

                    mData.add(item);

                    i = i + 2;
                }

                return mData;
            }

        } catch (Exception ex) {
            Log.e(LatestAchievement.class.getSimpleName(), ex.getMessage());
            return null;
        }
        return null;
    }
}

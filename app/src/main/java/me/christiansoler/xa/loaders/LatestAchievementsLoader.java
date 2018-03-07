package me.christiansoler.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import me.christiansoler.xa.misc.Common;
import me.christiansoler.xa.models.LatestAchievement;

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
            Document document = Jsoup.connect(BASE_URL).get();

            Elements rows = document.select(".bl_la_main .divtext table:first-child tbody tr");

            if (!rows.isEmpty()) {
                for (int i = 0; i < rows.size(); i++) {
                    String image_url = rows.get(i + 1).select("td:eq(0) > img").attr("abs:src");
                    String title = rows.get(i).select(".newsTitle").text().trim();
                    String ach_page_url = rows.get(i + 1).select("td:eq(1) > a").attr("href");
                    String date_added = rows.get(i).select(".newsNFO").text().trim();
                    String subtitle = "";
                    String submitted_by = "";
                    String comment_count = "";

                    title = title.replace("Game Added: ", "").replace("DLC Added: ", "").replace("DLCs Added: ", "");
                    date_added = date_added.replace("Added: ", "");

                    if (rows.get(i + 1).select("td:eq(1) > p").isEmpty()) {
                        subtitle = rows.get(i + 1).select("td:eq(1)").get(0).textNodes().get(0).text().trim();
                        submitted_by = rows.get(i + 1).select("td:eq(1) > strong").eq(0).text().trim();
                    } else {
                        subtitle = rows.get(i + 1).select("td:eq(1) > p").get(0).textNodes().get(0).text().trim();
                        submitted_by = rows.get(i + 1).select("td:eq(1) > p > strong").eq(0).text().trim();
                    }

                    LatestAchievement item = new LatestAchievement();
                    item.setImageUrl(Common.getCoverImage(image_url));
                    item.setTitle(title);
                    item.setAchievementsCount(subtitle.split(", ")[0].trim());
                    item.setGamerscoreCount(subtitle.split(", ")[1].replace(".", "").trim());
                    item.setSubmittedBy(submitted_by);
                    item.setDateAdded(date_added);
                    item.setGamePermalink(ach_page_url);

                    mData.add(item);

                    i = i + 2;
                }

                return mData;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return null;
    }
}

package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.models.Game;

public class GamesListLoader extends AsyncTaskLoader<ArrayList<Game>> {
    private final String BASE_URL;
    private ArrayList<Game> mData = new ArrayList<>();

    public GamesListLoader(Context context, String base_url) {
        super(context);
        BASE_URL = base_url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (!mData.isEmpty())
            super.deliverResult(mData);
        else
            forceLoad();
    }

    @Override
    public void deliverResult(ArrayList<Game> data) {
        mData = data;
        if (isStarted() && mData != null) {
            super.deliverResult(mData);
        }
    }

    @Override
    public ArrayList<Game> loadInBackground() {
        try {
            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Elements rows = document.select(".bl_la_main .divtext table tr[class~=(trA1|trA2)]");

            for (Element element : rows) {
                String artwork = element.select("img:eq(0)").attr("abs:src").replace("ico", "cover");
                String title = element.select("strong:eq(0)").text().trim();
                String ach_count = element.select("td:eq(2)").text().trim();
                String gs_count = element.select("td:eq(3)").text().trim();
                String page_url = element.select("td:eq(1) a").attr("href");

                Game game = new Game();
                game.setArtwork(Common.getHighResCoverImage(artwork, getContext()));
                game.setTitle(title);
                game.setAchCount(ach_count);
                game.setGsCount(gs_count);
                game.setGamePermalink(page_url);

                mData.add(game);
            }

            return mData;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

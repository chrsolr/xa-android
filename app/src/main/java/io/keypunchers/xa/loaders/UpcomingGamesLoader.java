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

import io.keypunchers.xa.models.UpcomingGame;


public class UpcomingGamesLoader extends AsyncTaskLoader<ArrayList<UpcomingGame>> {

    private final String BASE_URL;
    private ArrayList<UpcomingGame> mData;

    public UpcomingGamesLoader(Context context, String url, ArrayList<UpcomingGame> data) {
        super(context);
        BASE_URL = url;
        mData = data;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (mData != null)
            super.deliverResult(mData);
        else
            forceLoad();
    }

    @Override
    public void deliverResult(ArrayList<UpcomingGame> data) {
        mData = data;
        if (isStarted() && mData != null) {
            super.deliverResult(mData);
        }
    }

    @Override
    public ArrayList<UpcomingGame> loadInBackground() {
        ArrayList<UpcomingGame> games = new ArrayList<>();

        try {
            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Element root = document.getElementsByClass("divtext").first();

            Elements rows = root.select("table tbody tr");

            for (int i = 2; i < rows.size(); i++) {
                String title = rows.get(i).select("a").text();
                String date = rows.get(i).select("td").first().text();
                String url = rows.get(i).select("a").attr("abs:href");

                UpcomingGame game = new UpcomingGame();
                game.setTitle(title);
                game.setDate(date);
                game.setUrl(url);

                games.add(game);
            }

            return games;
        } catch (Exception e) {
            Log.e("ArticleListItem Loader", e.getMessage());
            return null;
        }
    }
}

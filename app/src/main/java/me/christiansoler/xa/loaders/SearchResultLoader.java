package me.christiansoler.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

import me.christiansoler.xa.misc.Common;
import me.christiansoler.xa.models.Game;
import org.jsoup.*;
import android.util.*;
import me.christiansoler.xa.misc.Enums.*;

public class SearchResultLoader extends AsyncTaskLoader<ArrayList<Game>> {
    private final String BASE_URL;
	private final String QUERY;
    private ArrayList<Game> mData = new ArrayList<>();

    public SearchResultLoader(Context context, String base_url, String query) {
        super(context);
        BASE_URL = base_url;
		QUERY = query;
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
			Connection.Response response = Jsoup.connect(BASE_URL)
			.data("search", QUERY)
			.method(Connection.Method.POST)
			.followRedirects(true)
			.execute();
			
			Document document = response.parse();
			
			Elements rows = document.select(".bl_la_main .divtext table tr[class~=(trA1|trA2)]");
			
			rows.remove(0);
			
			for (Element element : rows) {
                String artwork = element.select("img:eq(0)").attr("abs:src").replace("ico", "cover");
                String title = element.select("td:eq(1) a:eq(0)").text().trim();
				String platform = element.select("td:eq(3)").text().trim();
                String ach_gs_count = element.select("td:eq(2)").text().trim();
                String page_url = element.select("a:eq(0)").attr("href");

				String test[] = ach_gs_count.split(" / ");
				
                Game game = new Game();
                game.setArtwork(Common.getCoverImage(artwork));
                game.setTitle(title);
                game.setAchCount(test[0]);
                game.setGsCount(test[1]);
				game.setPlatform(platform);
                game.setGamePermalink(page_url, UrlType.SEARCH_GAMES);
				
                mData.add(game);
            }
			
			return mData;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

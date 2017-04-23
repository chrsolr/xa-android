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

import io.keypunchers.xa.models.ArticleListItem;
import android.widget.*;

public class ArticleListLoader extends AsyncTaskLoader<ArrayList<ArticleListItem>> {
    private final int COUNTER_PLUS;
    private final String BASE_URL;
    private ArrayList<ArticleListItem> mData = new ArrayList<>();

    public ArticleListLoader(Context context, String url, int counter) {
        super(context);
        BASE_URL = url;
        COUNTER_PLUS = counter;
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
    public void deliverResult(ArrayList<ArticleListItem> data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }

    @Override
    public ArrayList<ArticleListItem> loadInBackground() {
        try {
			Document document = Jsoup.connect(BASE_URL).get();
            //Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Elements root = document.select(".divtext").first().select("table tbody tr");

            if (!root.isEmpty()) {
				
                final int size = root.size() - 1;

                for (int i = 0; i < size; i++) {
                    String image_url = root.get(i).select("td:first-child a img").first().attr("abs:src");
                    String title = root.get(i).select("td:nth-child(2) a").first().text().trim();
                    String author = root.get(i).select("td:nth-child(2) .newsNFO").first().text().trim();
                    String desc = root.get(i).select("td:nth-child(2) div:nth-child(3)").text().trim();
                    String page_url = root.get(i).select("td:nth-child(2) a").first().attr("abs:href");

                    ArticleListItem item = new ArticleListItem();
                    item.setTitle(title);
                    item.setAuthor(author);
                    item.setDesc(desc);
                    item.setImageUrl(image_url);
                    item.setPageUrl(page_url);

                    mData.add(item);

                    i += COUNTER_PLUS;
                }
            }

            return mData;

        } catch (Exception ex) {
            Log.e("Article Loader", ex.getMessage());
            return null;
        }
    }
}

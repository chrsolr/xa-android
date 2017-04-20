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

public class ArticleListLoader extends AsyncTaskLoader<ArrayList<ArticleListItem>> {
    private final int COUNTER_PLUS;
    private final String BASE_URL;
    private ArrayList<ArticleListItem> mData;

    public ArticleListLoader(Context context, String url, ArrayList<ArticleListItem> data, int counter) {
        super(context);
        BASE_URL = url;
        mData = data;
        COUNTER_PLUS = counter;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public void deliverResult(ArrayList<ArticleListItem> data) {
        if (isStarted() && mData != null) {
            super.deliverResult(mData);
        }
    }

    @Override
    public ArrayList<ArticleListItem> loadInBackground() {
        try {
            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Elements root = document.select(".divtext:first-child tr");

            if (!root.isEmpty()) {

                int size = 50;
                int offset_counter = 1;

                for (int i = 0; i < size; i++) {
                    String image_url = root.get(i).select("td:first-child a img").first().attr("abs:src");
                    String title = root.get(i).select("td:nth-child(2) a").first().text().trim();
                    String author = root.get(i).select("td:nth-child(2) .newsNFO").first().text().trim();
                    String desc = root.get(i).select("td:nth-child(2) div:nth-child(3)").text().trim();
                    String page_url = root.get(i).select("td:nth-child(2) a").first().attr("abs:href");

                    offset_counter += COUNTER_PLUS;

                    ArticleListItem item = new ArticleListItem();
                    item.setTitle(title);
                    item.setAuthor(author);
                    item.setDesc(desc);
                    item.setImageUrl(image_url);
                    item.setPageUrl(page_url);

                    mData.add(item);

                    i++;
                }
            }

            return mData;

        } catch (Exception ex) {
            Log.e("Article Loader", ex.getMessage());
            return null;
        }
    }
}

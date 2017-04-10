package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.keypunchers.xa.models.Article;
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

        if (mData != null)
            super.deliverResult(mData);
        else
            forceLoad();
    }

    @Override
    public void deliverResult(ArrayList<ArticleListItem> data) {
        mData = data;
        if (isStarted() && mData != null) {
            super.deliverResult(mData);
        }
    }

    @Override
    public ArrayList<ArticleListItem> loadInBackground() {
        try {
            ArrayList<ArticleListItem> items = new ArrayList<>();

            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Element root = document.getElementsByClass("divtext").first();

            if (!root.toString().equals("")) {

                int size = 25;
                int offset_counter = 1;

                for(int i = 0; i < size; i++) {
                    String image_url = root.select("td[valign=top] a img").get(i).attr("abs:src");
                    String title = root.getElementsByClass("newsTitle").get(i).text();
                    String author = root.getElementsByClass("newsNFO").get(i).text();
                    String desc = root.select("td").get(offset_counter).select("div").get(2).text();
                    String page_url = root.getElementsByClass("newsTitle").get(i).select("a.linkB").attr("abs:href");

                    offset_counter += COUNTER_PLUS;

                    ArticleListItem item = new ArticleListItem();
                    item.setTitle(title);
                    item.setAuthor(author);
                    item.setDesc(desc);
                    item.setImageUrl(image_url);
                    item.setPageUrl(page_url);

                    items.add(item);
                }
            }

            return items;

        } catch (Exception ex){
            Log.e("Article Loader", ex.getMessage());
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}

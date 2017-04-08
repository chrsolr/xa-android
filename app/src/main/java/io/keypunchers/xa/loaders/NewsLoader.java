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

import io.keypunchers.xa.models.ArticleListItem;

public class NewsLoader extends AsyncTaskLoader<List<ArticleListItem>> {
    private final Context mContext;
    private final int COUNTER_PLUS = 3;

    public NewsLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public void deliverResult(List<ArticleListItem> data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }

    @Override
    public List<ArticleListItem> loadInBackground() {
        try {
            String BASE_URL = "http://www.xboxachievements.com/archive/gaming-news/1/";

            List<ArticleListItem> items = new ArrayList<>();

            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Element root = document.getElementsByClass("divtext").first();

            if (!root.toString().equals("")) {

                int size = 25;
                int offset_counter = 1;

                for(int i = 0; i < size; i++) {
                    // extract element details
                    String image_url = root.select("td[valign=top] a img").get(i).attr("abs:src");
                    String title = root.getElementsByClass("newsTitle").get(i).text();
                    String author = root.getElementsByClass("newsNFO").get(i).text();
                    String desc = root.select("td").get(offset_counter).select("div").get(2).text();
                    String page_url = root.getElementsByClass("newsTitle").get(i).select("a.linkB").attr("abs:href");

                    // increase counters
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
            Log.e("News Loader", ex.getMessage());
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}

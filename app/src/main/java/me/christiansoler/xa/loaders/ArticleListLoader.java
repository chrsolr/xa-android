package me.christiansoler.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import me.christiansoler.xa.models.ArticleListItem;

public class ArticleListLoader extends AsyncTaskLoader<ArrayList<ArticleListItem>> {
    private final String BASE_URL;
    private ArrayList<ArticleListItem> mData = new ArrayList<>();

    public ArticleListLoader(Context context, String url) {
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
    public void deliverResult(ArrayList<ArticleListItem> data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }

    @Override
    public ArrayList<ArticleListItem> loadInBackground() {
        try {
            Elements elements = Jsoup.connect(BASE_URL)
                    .get()
                    .getElementsByClass("divtext")
                    .eq(0)
                    .select("tr");

            for (Element element : elements) {
                if (element.children().size() < 2) continue;

                String image_url = element.select("td:first-child a img:eq(0)").attr("abs:src");
                String title = element.select("td:nth-child(2) a:eq(0)").text().trim();
                String author = element.select("td:nth-child(2) .newsNFO:eq(0)").text().trim();
                String desc = element.select("td:nth-child(2) div:nth-child(3)").text().trim();
                String page_url = element.select("td:nth-child(2) a:eq(0)").attr("abs:href");

                ArticleListItem item = new ArticleListItem();
                item.setTitle(title);
                item.setAuthor(author);
                item.setDesc(desc);
                item.setImageUrl(image_url);
                item.setPageUrl(page_url);

                mData.add(item);
            }

            return mData;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

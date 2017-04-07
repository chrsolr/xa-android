package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class NewsLoader extends AsyncTaskLoader<JSONArray> {
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
    public void deliverResult(JSONArray data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }

    @Override
    public JSONArray loadInBackground() {
        try {
            String BASE_URL = "http://www.xboxachievements.com/archive/gaming-news/1/";

            JSONArray list = new JSONArray();

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

                    JSONObject json = new JSONObject();
                    json.put("image_url", image_url);
                    json.put("title", title);
                    json.put("author", author);
                    json.put("desc", desc);
                    json.put("page_url", page_url);

                    list.put(json);
                }
            }

            return list;

        } catch (Exception ex){
            Log.e("News Loader", ex.getMessage());
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}

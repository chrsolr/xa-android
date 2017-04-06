package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

public class LatestScreenshotsLoader extends AsyncTaskLoader<JSONArray> {

    private final Context mContext;

    public LatestScreenshotsLoader(Context context) {
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
            String BASE_URL = "http://www.xboxachievements.com/";

            JSONArray list = new JSONArray();

            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Element root = document.getElementsByClass("bl_me_main").get(3);

            if (!root.toString().equals("")) {
                Elements elements = root.getElementsByTag("td");

                for(Element el : elements) {
                    String title = el.select("a b").first().text();
                    String image_url = el.select("a img").first().attr("abs:src");
                    String url = el.select("a").first().attr("abs:href");
                    String date = el.ownText().trim();

                    image_url = image_url.replace("thu", "med").replaceAll("\\s", "%20");

                    JSONObject json = new JSONObject();
                    json.put("title", title);
                    json.put("image_url", image_url);
                    json.put("url", url);
                    json.put("date", date);

                    list.put(json);
                }
            }

            return list;
        } catch (Exception ex) {
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }
}

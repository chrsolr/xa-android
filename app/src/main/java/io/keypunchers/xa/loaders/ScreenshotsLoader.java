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

import io.keypunchers.xa.models.Screenshot;

public class ScreenshotsLoader extends AsyncTaskLoader<ArrayList<Screenshot>> {

    private final Context mContext;
    private final String BASE_URL;
    private ArrayList<Screenshot> mData;

    public ScreenshotsLoader(Context context, String url, ArrayList<Screenshot> data) {
        super(context);
        mContext = context;
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
    public void deliverResult(ArrayList<Screenshot> data) {
        mData = data;
        if (isStarted() && mData != null) {
            super.deliverResult(mData);
        }
    }

    @Override
    public ArrayList<Screenshot> loadInBackground() {
        try {
            ArrayList<Screenshot> items = new ArrayList<>();

            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Element root = document.getElementsByClass("bl_me_main").get(3);

            if (!root.toString().equals("")) {
                Elements elements = root.getElementsByTag("td");

                for (Element el : elements) {
                    String title = el.select("a b").first().text();
                    String image_url = el.select("a img").first().attr("abs:src");
                    String url = el.select("a").first().attr("abs:href");
                    String date = el.ownText().trim();

                    image_url = image_url.replace("thu", "med").replaceAll("\\s", "%20");

                    Screenshot obj = new Screenshot();
                    obj.setTitle(title);
                    obj.setSubtitle(date);
                    obj.setImageUrl(image_url);
                    obj.setGameUrl(url);

                    items.add(obj);
                }
            }

            return items;
        } catch (Exception ex) {
            Log.e("Drawer Loader", ex.getMessage());
            return null;
        }

    }
}

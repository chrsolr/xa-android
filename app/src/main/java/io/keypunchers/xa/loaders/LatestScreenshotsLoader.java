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
import io.keypunchers.xa.misc.*;

public class LatestScreenshotsLoader extends AsyncTaskLoader<ArrayList<Screenshot>> {
    private ArrayList<Screenshot> mData;

    public LatestScreenshotsLoader(Context context, ArrayList<Screenshot> data) {
        super(context);
        mData = data;
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
    public void deliverResult(ArrayList<Screenshot> data) {
        mData = data;
        if (isStarted() && !mData.isEmpty()) {
            super.deliverResult(mData);
        }
    }

    @Override
    public ArrayList<Screenshot> loadInBackground() {
        try {
            Document document = Jsoup.parse(new URL(Common.BASE_URL).openStream(), "UTF-8", Common.BASE_URL);

            Element root = document.getElementsByClass("bl_me_main").get(3);

            if (!root.toString().equals("")) {
                Elements elements = root.getElementsByTag("td");

                for (Element el : elements) {
                    String title = el.select("a b:first-child").text().trim();
                    String image_url = el.select("a img:first-child").attr("abs:src");
                    String game_screenshots_url = el.select("a:first-child").attr("abs:href");
                    String date = el.ownText().trim();

                    image_url = Common.imageUrlThumbToMed(image_url);

                    Screenshot obj = new Screenshot();
                    obj.setTitle(title);
                    obj.setDateAdded(date);
                    obj.setImageUrl(image_url);
                    obj.setGamePermalink(game_screenshots_url);

                    mData.add(obj);
                }
            }

            return mData;
        } catch (Exception ex) {
            Log.e("LatestScreenshots Loader", ex.getMessage());
            return null;
        }

    }
}

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

import io.keypunchers.xa.models.DrawerBanner;
import io.keypunchers.xa.models.Screenshot;


public class DrawerBannerLoader extends AsyncTaskLoader<ArrayList<DrawerBanner>> {

    public DrawerBannerLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public ArrayList<DrawerBanner> loadInBackground() {
        try {
            String BASE_URL = "http://www.xboxachievements.com";

            ArrayList<DrawerBanner> items = new ArrayList<>();

            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Element root = document.getElementsByClass("bl_me_main").get(3);

            if (!root.toString().equals("")) {
                Elements elements = root.getElementsByTag("td");

                for (Element el : elements) {
                    String title = el.select("a b").first().text();
                    String image_url = el.select("a img").first().attr("abs:src");
                    String url = el.select("a").first().attr("abs:href");

                    image_url = image_url.replace("thu", "med").replaceAll("\\s", "%20");

                    DrawerBanner obj = new DrawerBanner();
                    obj.setTitle(title);
                    obj.setImage(image_url);
                    obj.setUrl(url);

                    items.add(obj);
                }
            }

            return items;
        } catch (Exception ex) {
            Log.e("Drawer Loader", ex.getMessage());
            return null;
        }
    }

    @Override
    public void deliverResult(ArrayList<DrawerBanner> data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }
}

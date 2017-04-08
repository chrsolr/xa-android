package io.keypunchers.xa.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.net.URL;

import io.keypunchers.xa.models.DrawerBanner;


public class DrawerBannerLoader extends AsyncTaskLoader<DrawerBanner> {
    private final Context mContext;
    private final String BASE_URL = "http://www.xboxachievements.com";

    public DrawerBannerLoader(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public DrawerBanner loadInBackground() {
        try {
            DrawerBanner banner = new DrawerBanner();

            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);
            Element root = document.select(".bl_me_main").get(3).select("tr td").first();

            if (!root.toString().equals("")) {
                String image_url = root.getElementsByTag("img").first().attr("abs:src");
                String game_screenshots_url = root.getElementsByTag("a").first().attr("abs:href");
                String game_title = root.getElementsByTag("b").first().text();

                image_url = image_url.replace("thu", "med").replaceAll("\\s", "%20");

                banner.setImage(image_url);
                banner.setUrl(game_screenshots_url);
                banner.setTitle(game_title);
            }

            return banner;
        } catch (Exception ex) {
            Log.e("Drawer Loader", ex.getMessage());
            Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void deliverResult(DrawerBanner data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }
}

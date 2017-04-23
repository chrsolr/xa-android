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

import io.keypunchers.xa.models.LatestScreenshot;
import io.keypunchers.xa.misc.*;

public class LatestScreenshotsLoader extends AsyncTaskLoader<ArrayList<LatestScreenshot>> {
    private ArrayList<LatestScreenshot> mData = new ArrayList<>();

    public LatestScreenshotsLoader(Context context) {
        super(context);
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
    public void deliverResult(ArrayList<LatestScreenshot> data) {
        if (isStarted() && !data.isEmpty()) {
            super.deliverResult(data);
        }
    }

    @Override
    public ArrayList<LatestScreenshot> loadInBackground() {
        try {
			Elements elements = Jsoup.connect(Common.BASE_URL)
				.get()
				.getElementsByClass("bl_me_main")
				.get(3)
				.getElementsByTag("td");

			for (Element el : elements) {
				String title = el.select("a b:first-child").text().trim();
				String image_url = el.select("a img:first-child").attr("abs:src");
				String game_screenshots_url = el.select("a:first-child").attr("abs:href");
				String date = el.ownText().trim();

				image_url = Common.imageUrlThumbToMed(image_url);

				LatestScreenshot obj = new LatestScreenshot();
				obj.setTitle(title);
				obj.setDateAdded(date);
				obj.setImageUrl(image_url);
				obj.setGamePermalink(game_screenshots_url);

				mData.add(obj);
			}
            
            return mData;
        } catch (Exception ex) {
            Log.e("Latest Screenshots Loader", ex.getMessage());
            return null;
        }
    }
}

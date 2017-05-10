package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.models.Screenshots;

public class ScreenshotsLoader extends AsyncTaskLoader<Screenshots> {
    private final String BASE_URL;
    private Screenshots mData = new Screenshots();

    public ScreenshotsLoader(Context context, String url) {
        super(context);
        BASE_URL = url;
        mData.setTitle("");
        mData.setImageUrls(new ArrayList<String>());
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (!mData.getImageUrls().isEmpty())
            deliverResult(mData);
        else
            forceLoad();
    }

    @Override
    public void deliverResult(Screenshots data) {
        if (isStarted() && !data.getImageUrls().isEmpty()) {
            super.deliverResult(data);
        }
    }

    @Override
    public Screenshots loadInBackground() {
        try {
            Document document = Jsoup.connect(BASE_URL).get();
            Elements elements = document.select(".bl_la_main .divtext table tbody tr td img");

            if (!document.select(".tt").isEmpty())
                mData.setTitle(document.select(".tt").first().text().trim());

            for (Element element : elements) {
                String image_url = element.attr("abs:src");

                mData.getImageUrls().add(Common.getHighResScreenshotImage(image_url, getContext()));
            }

            return mData;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

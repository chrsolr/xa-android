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
import java.util.ArrayList;
import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<JSONObject> {
    private final String BASE_URL;
    private final Context mContext;

    public ArticleLoader(Context context, String url) {
        super(context);
        mContext = context;
        BASE_URL = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public void deliverResult(JSONObject data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }

    @Override
    public JSONObject loadInBackground() {

        try {
            JSONObject json = new JSONObject();

            Document document = Jsoup.parse(new URL(BASE_URL).openStream(), "UTF-8", BASE_URL);

            Elements header_root = document.getElementsByClass("bl_la_main").first().getElementsByTag("td");
            Element body_root = document.getElementsByClass("bl_la_main").first().select("span[itemprop=articleBody]").first();

            String profile_image = header_root.first().select("img").first().attr("abs:src");
            String header_title = header_root.get(1).select(".newsTitle").first().text();
            String header_date = header_root.get(1).select(".newsNFO").text();
            String author_profile_url = header_root.get(1).select("a").first().attr("abs:href");

            JSONObject header = new JSONObject();
            header.put("profile_image", profile_image);
            header.put("header_title", header_title);
            header.put("header_date", header_date);
            header.put("author_profile_url", author_profile_url);

            JSONObject body = new JSONObject();

            Elements article_contents = body_root.children();
            Elements article_images = article_contents.select("img");
            Elements article_iframes = article_contents.select("iframe");

            article_contents.select("img").remove();
            article_contents.select("iframe").remove();

            List<String> images = new ArrayList<>();

            for (Element img : article_images) {
                String src = img.attr("abs:src");
                images.add(src);
            }

//            List<JSONObject> iframes = new ArrayList<>();
//
//            for (Element iframe : article_iframes) {
//                String src = iframe.attr("abs:src");
//                iframes.add(new JSONObject().put("url", src));
//            }

            body.put("body_text", article_contents.toString());
            body.put("body_images", images);
            //body.put("body_iframes", iframes.toArray());

            json.put("header", header);
            json.put("body", body);

            return json;
        } catch (Exception ex) {
            Log.e("Article Loader", ex.getMessage());
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}

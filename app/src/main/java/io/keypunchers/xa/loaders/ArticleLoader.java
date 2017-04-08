package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;

import io.keypunchers.xa.models.Article;

public class ArticleLoader extends AsyncTaskLoader<Article> {
    private final String BASE_URL;
    private Article mData;

    public ArticleLoader(Context context, String url, Article article) {
        super(context);
        BASE_URL = url;
        mData = article;
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
    public void deliverResult(Article data) {
        mData = data;
        if (isStarted() && mData != null) {
            super.deliverResult(mData);
        }
    }

    @Override
    public Article loadInBackground() {

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

            ArrayList<String> images = new ArrayList<>();

            for (Element img : article_images) {
                String src = img.attr("abs:src");
                images.add(src);
            }

            ArrayList<String> videos = new ArrayList<>();

            for (Element video : article_iframes) {
                String src = video.attr("abs:src");
                videos.add(src);
            }

            Article article = new Article();
            article.setAuthorProfileImageUrl(profile_image);
            article.setHeaderTitle(header_title);
            article.setHeaderDate(header_date);
            article.setAuthorProfileUrl(author_profile_url);
            article.setBodyText(article_contents.toString());

            article.setImageUrls(images);
            article.setVideoUrls(videos);

            return article;
        } catch (Exception ex) {
            Log.e("ArticleListItem Loader", ex.getMessage());
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}

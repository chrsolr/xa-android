package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.models.Article;
import io.keypunchers.xa.models.Comment;

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
            Document document = Jsoup.connect(BASE_URL).get();
            Document comments_doc = Jsoup.connect(Common.NEWS_COMMENTS_URL)
                    .data("nID", Common.getNewsCommenstId(BASE_URL))
                    .post();

//            String url = Common.BASE_URL + "/forum/login.php";
//            String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36";
//
//            Connection.Response response = Jsoup.connect(url).userAgent(userAgent)
//                    .method(Connection.Method.GET)
//                    .execute();
//
//            response = Jsoup.connect(url)
//                    .cookies(response.cookies())
//                    .data("vb_login_username", "username")
//                    .data("vb_login_password", "pass")
//                    .data("do", "login")
//                    .userAgent(userAgent)
//                    .method(Connection.Method.POST)
//                    .followRedirects(true)
//                    .execute();
//
//            Jsoup.connect("http://www.xboxachievements.com/postComment.php?type=360news")
//                    .data("newsID", Common.getNewsCommenstId(BASE_URL))
//                    .data("username", "CS15")
//                    .data("comment", "Gears where are you??? \n\n Via XA Android App")
//                    .data("submit", "Submit")
//                    .cookies(response.cookies())
//                    .post();

            Elements header_root = document.getElementsByClass("bl_la_main").first().getElementsByTag("td");
            Element body_root = document.getElementsByClass("bl_la_main").first().select("span[itemprop=articleBody]").first();
            Elements comments_rows = comments_doc.select("tr");

            String profile_image = header_root.first().select("img").first().attr("abs:src");
            String header_title = header_root.get(1).select(".newsTitle").first().text();
            String header_date = header_root.get(1).select(".newsNFO").text().split("By ")[0].replace("Written ", "").trim();
            String header_author_name = header_root.get(1).select(".newsNFO").text().split("By ")[1];
            String author_profile_url = header_root.get(1).select("a").first().attr("abs:href");
            String author_first_name = header_author_name.split(" ")[0];
            String author_last_name = header_author_name.split(" ")[1];

            Elements article_contents = body_root.children();
            Elements article_images = article_contents.select("img");
            Elements article_iframes = article_contents.select("iframe");

            article_contents.select("img").remove();
            article_contents.select("iframe").remove();

            ArrayList<String> images = new ArrayList<>();

            for (Element img : article_images) {
                String src = img.attr("abs:src");

                if (Common.isImageBlacklisted(src))
                    continue;

                images.add(Common.highResScreenshotImage(src, getContext()));
            }

            ArrayList<String> videos = new ArrayList<>();

            for (Element video : article_iframes) {
                String src = video.attr("abs:src");
                videos.add(src);
            }

            ArrayList<Comment> comments = new ArrayList<>();

            if (comments_rows.size() > 1) {
                for (int i = 0; i < comments_rows.size(); i++) {
                    String image_url = comments_rows.get(i).select("td img").attr("abs:src");
                    String title = comments_rows.get(i).select("td b").first().text().trim();
                    String date = comments_rows.get(i).select(".newsNFO").first().text().split(" @")[0].trim();
                    String text = "";

                    image_url = image_url.replaceAll("\\s", "%20");

                    Element text_root = comments_rows.get(i + 1).select("td").first();

                    if (text_root.textNodes().size() > 0) {
                        for (int j = 0; j < text_root.textNodes().size(); j++) {
                            text += text_root.textNodes().get(j).text();

                            if (j != (text_root.textNodes().size() - 1)) {
                                text += "\n";
                            }
                        }
                    }

                    i = i + 2;

                    Comment comment = new Comment();
                    comment.setImageUrl(image_url);
                    comment.setTitle(title);
                    comment.setDate(date);
                    comment.setText(text);

                    comments.add(comment);
                }
            }

            Article article = new Article();
            article.setAuthorProfileImageUrl(profile_image);
            article.setHeaderTitle(header_title);
            article.setHeaderDate(header_date);
            article.setAuthorProfileUrl(author_profile_url);
            article.setAuthorFirstName(author_first_name);
            article.setAuthorLastName(author_last_name);
            article.setBodyText(article_contents.toString());

            article.setImageUrls(images);
            article.setVideoUrls(videos);
            article.setComments(comments);

            return article;
        } catch (Exception ex) {
            Log.e("ArticleListItem Loader", ex.getMessage());
            return null;
        }
    }
}

package me.christiansoler.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import me.christiansoler.xa.misc.Common;
import me.christiansoler.xa.models.Article;
import me.christiansoler.xa.models.Comment;
import org.jsoup.nodes.TextNode;
import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<Article> {
    private final String BASE_URL;
    private Article mData;

    public ArticleLoader(Context context, String url) {
        super(context);
        BASE_URL = url;
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
                    .data("nID", Common.getNewsCommentstId(BASE_URL))
                    .post();

            Elements header_root = document.getElementsByClass("bl_la_main").first().getElementsByTag("td");
            Element body_root = document.getElementsByClass("bl_la_main").first().select("span[itemprop=articleBody]").first();
            Elements comments_rows = comments_doc.select("tr");
			Element authorElement = header_root.first().select("img").first();

            String profile_image = authorElement != null 
					? Common.getArticleAuthorImage(header_root.first().select("img").first().attr("abs:src"))
				: "http://www.xboxachievements.com/images/news/general/xba-news.png";
				
            String header_title = header_root.get(1).select(".newsTitle").first().text();
            String header_date = header_root.get(1).select(".newsNFO").text().split("By ")[0].replace("Written ", "").trim();
            String header_author_name = header_root.get(1).select(".newsNFO").text().split("By ")[1];
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

                images.add(Common.getScreenshotImage(src, true));
            }

            ArrayList<String> videos = new ArrayList<>();

            for (Element video : article_iframes) {
                String src = video.attr("abs:src");
                videos.add(src);
            }

            ArrayList<Comment> comments = new ArrayList<>();

            if (comments_rows.size() > 1) {
                for (int i = 0, size = comments_rows.size(); i < size; i++) {
                    String image_url = comments_rows.get(i).select("td img").attr("abs:src");
                    String title = comments_rows.get(i).select("td b").first().text().trim();
                    String date = comments_rows.get(i).select(".newsNFO").first().text().split(" @")[0].trim();
                    String content = "";

                    image_url = image_url.replaceAll("\\s", "%20");
					
					title = title.substring(title.indexOf("by ") + 3, title.length()).trim();
					
					List<TextNode> text_nodes = comments_rows.get(i + 1).select("td").first().textNodes();
					for (int j = 0, nodes_size = text_nodes.size(); j < nodes_size; j++){
						content += text_nodes.get(j).text().trim();
						
						if (j != nodes_size - 1) {
							content += System.getProperty("line.separator") + System.getProperty("line.separator");
						}
					}

                    i = i + 2;

                    Comment comment = new Comment();
                    comment.setImageUrl(image_url);
                    comment.setTitle(title);
                    comment.setDate(date);
                    comment.setText(content);

                    comments.add(comment);
                }
            }

            Article article = new Article();
            article.setAuthorProfileImageUrl(profile_image);
            article.setHeaderTitle(header_title);
            article.setHeaderDate(header_date);
            article.setAuthorFirstName(author_first_name);
            article.setAuthorLastName(author_last_name);
            article.setBodyText(article_contents.toString());
            article.setImageUrls(images);
            article.setVideoUrls(videos);
            article.setComments(comments);

            return article;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

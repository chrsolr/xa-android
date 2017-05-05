package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import io.keypunchers.xa.models.Comment;
import org.jsoup.nodes.TextNode;
import java.util.List;

public class AchievementCommentsLoader extends AsyncTaskLoader<ArrayList<Comment>> {
    private ArrayList<Comment> mData = new ArrayList<>();
    private String BASE_URL;

    public AchievementCommentsLoader(Context context, String url) {
        super(context);
        BASE_URL = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (!mData.isEmpty())
            deliverResult(mData);
        else
            forceLoad();
    }

    @Override
    public void deliverResult(ArrayList<Comment> data) {
        if (isStarted() && data != null) {
            super.deliverResult(data);
        }
    }

    @Override
    public ArrayList<Comment> loadInBackground() {
        try {
            Elements elements = Jsoup.connect(BASE_URL)
                    .get()
                    .select(".divtext")
                    .eq(1)
                    .select("table")
                    .first()
                    .select("tr");

            for (Element element : elements) {
                if (element.children().size() < 2) continue;

                String image_url = element.select("td:eq(0) img").attr("abs:src");
                String title = element.select("td:eq(1) b").first().text().trim();
                String date = element.select(".newsNFO").first().text().split(" @")[0].trim();
                String content = "";

                image_url = image_url.replaceAll("\\s", "%20");

				List<TextNode> text_nodes = element.select("td:eq(1)").first().textNodes();
				for (int j = 0, nodes_size = text_nodes.size(); j < nodes_size; j++){
					content += text_nodes.get(j).text().trim();

					if (j != nodes_size - 1) {
						content += System.getProperty("line.separator") + System.getProperty("line.separator");
					}
				}

                Comment comment = new Comment();
                comment.setImageUrl(image_url);
                comment.setTitle(title);
                comment.setDate(date);
                comment.setText(content);

                mData.add(comment);
            }

            return mData;
        } catch (Exception e) {
            Log.e(AchievementCommentsLoader.class.getSimpleName(), e.getMessage());
            return null;
        }
    }
}

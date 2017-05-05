package io.keypunchers.xa.loaders;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import io.keypunchers.xa.misc.Common;
import org.jsoup.Jsoup;
import org.jsoup.Connection;
import java.util.Locale;

public class SubmitArticleCommentLoader extends AsyncTaskLoader<Boolean> {
    private final String BASE_URL;
	private String mComment;

    public SubmitArticleCommentLoader(Context context, String url, String comment) {
        super(context);
        BASE_URL = url;
		mComment = comment;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

		if (mComment == null || mComment.equals("")) {
			super.deliverResult(false);
		} else {
            forceLoad();
		}
    }

    @Override
    public void deliverResult(Boolean data) {
        if (isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    public Boolean loadInBackground() {

        try {
            String url = Common.BASE_URL + "/forum/login.php";
            String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36";

            Connection.Response response = Jsoup.connect(url).userAgent(userAgent)
				.method(Connection.Method.GET)
				.execute();

            response = Jsoup.connect(url)
				.cookies(response.cookies())
				.data("vb_login_username", "cs15")
				.data("vb_login_password", "damian.l")
				.data("do", "login")
				.userAgent(userAgent)
				.method(Connection.Method.POST)
				.followRedirects(true)
				.execute();

            Jsoup.connect("http://www.xboxachievements.com/postComment.php?type=360news")
				.data("newsID", Common.getNewsCommenstId(BASE_URL))
				.data("username", "CS15")
				.data("comment", String.format(Locale.US, "%s%s%sVia XA Android App", mComment, System.getProperty("line.separator"), System.getProperty("line.separator")))
				.data("submit", "Submit")
				.cookies(response.cookies())
				.post();

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

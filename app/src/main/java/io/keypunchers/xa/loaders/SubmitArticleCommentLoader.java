package io.keypunchers.xa.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.util.Pair;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.util.Locale;

import io.keypunchers.xa.misc.Common;
import io.keypunchers.xa.misc.Enums;
import io.keypunchers.xa.misc.Singleton;
import io.keypunchers.xa.models.UserProfile;

public class SubmitArticleCommentLoader extends AsyncTaskLoader<Pair<Boolean, String>> {
    private String mThread = "https://forum.xda-developers.com/android/apps-games/app-xa-unofficial-xboxachievements-com-t3600450";
	private String mComment;
    private Enums.PostType mPostType;
    private String mID;
    private boolean isCached;

    public SubmitArticleCommentLoader(Context context, String id, String comment, Enums.PostType type) {
        super(context);
        mComment = comment;
        mPostType = type;
        mID = id;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();

        if (isCached) {
            super.deliverResult(Pair.create(false, "Cached"));
        } else {
            forceLoad();
        }
    }

    @Override
    public void deliverResult(Pair<Boolean, String> data) {
        if (isStarted()) {
            isCached = true;
            super.deliverResult(data);
        }
    }

    @Override
    public Pair<Boolean, String> loadInBackground() {

        try {
            UserProfile profile = Singleton.getInstance().getUserProfile();

            if (!profile.isLogged())
                return Pair.create(false, "Not logged in. Set credentials in settings.");

            String url = Common.BASE_URL + "/forum/login.php";
            String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.96 Safari/537.36";

            Connection.Response response = Jsoup.connect(url).userAgent(userAgent)
                    .method(Connection.Method.GET)
                    .execute();

            response = Jsoup.connect(url)
                    .cookies(response.cookies())
                    .data("vb_login_username", profile.getUsername())
                    .data("vb_login_password", profile.getPassword())
                    .data("do", "login")
                    .userAgent(userAgent)
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .execute();

            if (response.cookies().get("bbsessionhash") == null) {
                return Pair.create(false, "Please check your credentials.");
            }
			
			mComment = String.format(Locale.US, "%s%s%sVia XA Android App%s%s", mComment, System.getProperty("line.separator"), System.getProperty("line.separator"), System.getProperty("line.separator"), mThread);

            if (mPostType == Enums.PostType.ARTICLE) {
                Jsoup.connect("http://www.xboxachievements.com/postComment.php?type=360news")
                        .data("newsID", mID)
                        .data("username", profile.getUsername())
						.data("comment", mComment)
                        .data("submit", "Submit")
                        .cookies(response.cookies())
                        .post();
            } else if (mPostType == Enums.PostType.ACHIEVEMENTS) {
                Jsoup.connect("http://www.xboxachievements.com/postCommentAch.php?achID=" + mID)
                        .data("achID", mID)
                        .data("username", profile.getUsername())
						.data("comment", mComment)
                        .data("submit", "Submit")
                        .cookies(response.cookies())
                        .post();
            }

            return Pair.create(true, null);
        } catch (Exception ex) {
            return Pair.create(false, ex.getMessage());
        }
    }
}

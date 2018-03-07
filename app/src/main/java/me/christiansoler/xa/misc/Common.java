package me.christiansoler.xa.misc;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.Locale;

import me.christiansoler.xa.R;
import me.christiansoler.xa.misc.Enums.UrlType;
import android.view.View;

public class Common extends Application {
    public static final String BASE_URL = "https://www.xboxachievements.com";
    public static final String NEWS_COMMENTS_URL = "https://www.xboxachievements.com/news2-loadcomments.php";

    public static int convertDpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
	
	public static String getArticleAuthorImage(String url) {
        if (url != null)
            url = url.replace(".png", "-la.png").replace(".jpg", "-la.jpg");

        return url;
    }

    public static String getScreenshotImage(String url, boolean isHttps) {
        if (url != null)
            url = url.replace("thu_", "med_").replaceAll("\\s", "%20");
        
		if ((url != null && !url.contains("https")) && isHttps)
			url = url.replace("http", "https");
			
        return url;
    }

    public static String getCoverImage(String url) {
        if (url != null)
            url = url.replace("/game/", "/achievements/").replaceAll("\\s", "%20");
        
        return url;
    }

    public static String getAchievementImage(String url) {
        if (url != null)
            url = url.replace("lo.", "hi.").replaceAll("\\s", "%20");
        
        return url;
    }

    public static String getYouTubeImage(String url) {

        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }

        String video_id = url.replace("https://www.youtube.com/embed/", "");

        url = String.format(Locale.US, "https://i3.ytimg.com/vi/%s/0.jpg", video_id);

        return url;
    }

    public static String getYouTubeWatchLink(String url) {
        if (url.contains("?")) {
            url = url.substring(0, url.indexOf("?"));
        }

        final String video_id = url.replace("https://www.youtube.com/embed/", "");

        url = String.format(Locale.US, "https://www.youtube.com/watch?v=%s", video_id);

        return url;
    }

    public static String getGameScreenshotsUrlByPermalink(String permalink, int page) {
        return String.format("%s/game/%s/screenshots/%s/", BASE_URL, permalink, page);
    }

    public static String getGameAchievementsUrlByPermalink(String permalink) {
        return String.format("%s/game/%s/achievements/", BASE_URL, permalink);
    }

    public static String createPermalink(String url, UrlType type) {
        if (type == UrlType.BROWSE_GAMES || type == UrlType.LATEST_ACHIEVEMENTS) {
            return url.replace("/game/", "").replace("/achievements/", "");
        }

        if (type == UrlType.LATEST_SCREENSHOTS) {
            return url.replace("/game/", "").replace("/screenshots/", "");
        }
		
		return url;
    }

    public static String getNewsCommentstId(String url) {
        String nID = url.substring(url.indexOf("-") + 1, url.length() - 1);

        if (nID.contains("-"))
            nID = nID.substring(0, nID.indexOf("-"));

        return nID;
    }

    public static String getAchievementId(String url) {
        String nID = url.substring(url.indexOf("achievement/") + 12, url.length() - 1);

        if (nID.contains("-"))
            nID = nID.substring(0, nID.indexOf("-"));

        return nID;
    }

    public static boolean isImageBlacklisted(String url) {
        if (url.contains("interview-divider.png"))
            return true;

        return false;
    }

    public static String getSubmitCommentUrl(Enums.PostType postType) {
        String url = null;

        switch (postType) {
            case ARTICLE:
                url = Common.BASE_URL + "/postComment.php?type=360news";
                break;
            case ACHIEVEMENTS:
                url = Common.BASE_URL + "/postCommentAch.php?achID=";
                break;
        }

        return url;
    }

    @NonNull
    public static Snackbar makeSnackbar(@NonNull Context context, @NonNull CharSequence text, int duration) {
        Snackbar snackBarView = Snackbar.make(((AppCompatActivity) context).getWindow().getDecorView().findViewById(android.R.id.content), text, duration);
        snackBarView.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.color_primary_accent));

        TextView tv = (TextView) snackBarView.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        return snackBarView;
    }
	
	@NonNull
    public static Snackbar makeSnackbar(@NonNull Context context, @NonNull View view, CharSequence text, int duration) {
        Snackbar snackBarView = Snackbar.make(view, text, duration);
        snackBarView.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.color_primary_accent));

        TextView tv = (TextView) snackBarView.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);

        return snackBarView;
    }
}

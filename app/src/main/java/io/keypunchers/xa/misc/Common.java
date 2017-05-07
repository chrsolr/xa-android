package io.keypunchers.xa.misc;

import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.Locale;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.Enums.UrlType;
import android.view.View;

public class Common extends Application {
    public static final String BASE_URL = "http://www.xboxachievements.com";
    public static final String NEWS_COMMENTS_URL = "http://www.xboxachievements.com/news2-loadcomments.php";

    public static int convertDpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static String highResScreenshotImage(String url, Context context) {
        final String HIGH_RES_IMAGE_SETTING = context.getResources().getString(R.string.HIGH_RES_IMAGE_SETTING_TAG);
        boolean high_res = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(HIGH_RES_IMAGE_SETTING, true);

        if (url != null && high_res)
            url = url.replace("thu_", "med_").replaceAll("\\s", "%20");
        else if (url != null)
            url = url.replace("med_", "thu_").replaceAll("\\s", "%20");

        return url;
    }

    public static String highResCoverImage(String url, Context context) {
        final String HIGH_RES_IMAGE_SETTING = context.getResources().getString(R.string.HIGH_RES_IMAGE_SETTING_TAG);
        boolean high_res = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(HIGH_RES_IMAGE_SETTING, true);

        if (url != null && high_res)
            url = url.replace("/game/", "/achievements/").replaceAll("\\s", "%20");
        else if (url != null)
            url = url.replace("/achievements/", "/game/").replaceAll("\\s", "%20");

        return url;
    }

    public static String highResAchImage(String url, Context context) {
        final String HIGH_RES_IMAGE_SETTING = context.getResources().getString(R.string.HIGH_RES_IMAGE_SETTING_TAG);
        boolean high_res = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(HIGH_RES_IMAGE_SETTING, true);

        if (url != null && high_res)
            url = url.replace("lo.", "hi.").replaceAll("\\s", "%20");
        else if (url != null)
            url = url.replace("hi.", "lo.").replaceAll("\\s", "%20");

        return url;
    }

    public static String highResYouTubeImage(String url) {

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
        String permalink = null;

        if (type == UrlType.BROWSE_GAMES || type == UrlType.LATEST_ACHIEVEMENTS) {
            permalink = url.replace("/game/", "").replace("/achievements/", "");
        }

        if (type == UrlType.LATEST_SCREENSHOTS) {
            permalink = url.replace("/game/", "").replace("/screenshots/", "");
        }

        return permalink;
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

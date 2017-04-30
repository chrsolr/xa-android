package io.keypunchers.xa.misc;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import io.keypunchers.xa.misc.Enums.UrlType;

public class Common extends Application {
    public static final String BASE_URL = "http://www.xboxachievements.com";
    public static final String NEWS_COMMENTS_URL = "http://www.xboxachievements.com/news2-loadcomments.php";

    public static int convertDpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static String highResScreenshotImage(String url, Context context) {
        boolean high_res = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("HIGH_IMAGE_QUALITY", true);

        if (url != null && high_res)
            url = url.replace("thu", "med").replaceAll("\\s", "%20");
        else if (url != null)
            url = url.replace("med", "thu").replaceAll("\\s", "%20");

        return url;
    }

    public static String highResCoverImage(String url, Context context) {
        boolean high_res = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("HIGH_IMAGE_QUALITY", true);

        if (url != null && high_res)
            url = url.replace("/game/", "/achievements/").replaceAll("\\s", "%20");
        else if (url != null)
            url = url.replace("/achievements/", "/game/").replaceAll("\\s", "%20");

        return url;
    }

    public static String highResAchImage(String url, Context context) {
        boolean high_res = PreferenceManager.getDefaultSharedPreferences(context).getBoolean("HIGH_IMAGE_QUALITY", true);

        if (url != null && high_res)
            url = url.replace("lo.", "hi.").replaceAll("\\s", "%20");
        else if (url != null)
            url = url.replace("hi.", "lo.").replaceAll("\\s", "%20");

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

    public static String getNewsCommenstId(String url) {
        String nID = url.substring(url.indexOf("-") + 1, url.length() - 1);

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
    public static Snackbar makeSnackbar(@NonNull View layout, @NonNull CharSequence text, int duration, int backgroundColor, int textColor) {
        Snackbar snackBarView = Snackbar.make(layout, text, duration);
        snackBarView.getView().setBackgroundColor(backgroundColor);

        TextView tv = (TextView) snackBarView.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(textColor);

        return snackBarView;
    }
}

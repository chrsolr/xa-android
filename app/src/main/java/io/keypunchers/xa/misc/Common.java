package io.keypunchers.xa.misc;

import android.app.Application;
import android.content.Context;
import android.util.TypedValue;

public class Common extends Application {

    public static final String BASE_URL = "http://www.xboxachievements.com";

    public static int convertDpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static String imageUrlThumbToMed(String url) {
        return url.replace("thu", "med").replaceAll("\\s", "%20");
    }

    public static String getGameScreenshotsUrlByPermalink(String permalink) {
        return String.format("/game/%s/screenshots/", permalink);
    }
}

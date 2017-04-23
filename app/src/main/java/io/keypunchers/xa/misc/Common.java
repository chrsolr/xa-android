package io.keypunchers.xa.misc;

import android.app.Application;
import android.content.Context;
import android.util.TypedValue;

public class Common extends Application {

    public static final String BASE_URL = "http://www.xboxachievements.com";
	public static final String NEWS_COMMENTS_URL = "http://www.xboxachievements.com/news2-loadcomments.php";
	
    public static int convertDpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static String imageUrlThumbToMed(String url) {
        return url.replace("thu", "med").replaceAll("\\s", "%20");
    }

    public static String getGameScreenshotsUrlByPermalink(String permalink) {
        return String.format("/game/%s/screenshots/", permalink);
    }

	public static String getNewsCommentId(String url){
		String nID = url.substring(url.indexOf("-") + 1, url.length() - 1);

		if (nID.contains("-"))
			nID = nID.substring(0, nID.indexOf("-"));

		return nID;
	}
}

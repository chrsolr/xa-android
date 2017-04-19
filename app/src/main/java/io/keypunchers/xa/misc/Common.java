package io.keypunchers.xa.misc;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.TypedValue;

public class Common extends Application {

	public static final String BASE_URL = "http://www.xboxachievements.com";	
	
    public static int convertDpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,  context.getResources().getDisplayMetrics());
    }
	
	public static String imageUrlthumbToMed(String url){
		return url.replace("thu", "med").replaceAll("\\s", "%20");
	}
}

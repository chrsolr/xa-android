package io.keypunchers.xa.misc;

import android.app.Application;
import android.content.Context;
import android.util.TypedValue;

public class Common extends Application {

    public static int convertDpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,  context.getResources().getDisplayMetrics());
    }
}

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

    public static int convertDpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16,  context.getResources().getDisplayMetrics());
    }

    public static Bitmap getCircleImage(Bitmap bitmap, int resColor, int strokeWidth) {
        // create Bitmap to draw
        Bitmap mBitmap = Bitmap.createBitmap(bitmap.getWidth() + 8, bitmap.getHeight() + 8, Bitmap.Config.ARGB_8888);

        // create Rect to hold image
        final Rect mRec = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        // create Canvas
        Canvas mCanvas = new Canvas(mBitmap);
        mCanvas.drawARGB(0, 0, 0, 0);

        // create Paint
        final Paint mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);

        // get the half size of the image
        int mHalfWidth = bitmap.getWidth() / 2;
        int mHalfHeight = bitmap.getHeight() / 2;

        // draw circle
        mCanvas.drawCircle((mHalfWidth + 4), (mHalfHeight + 4), Math.min(mHalfWidth, mHalfHeight), mPaint);

        // unknown
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // draw the image
        mCanvas.drawBitmap(bitmap, mRec, mRec, mPaint);

        // set border mode
        mPaint.setXfermode(null);

        // set stroke
        mPaint.setStyle(Paint.Style.STROKE);

        // set stroke color
        mPaint.setColor(resColor);

        // set stroke width
        mPaint.setStrokeWidth(strokeWidth);

        // draw stroke
        mCanvas.drawCircle((mHalfWidth + 4), (mHalfHeight + 4), Math.min(mHalfWidth, mHalfHeight), mPaint);

        // return the circle image
        return mBitmap;
    }
}

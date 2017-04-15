package io.keypunchers.xa.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

import io.keypunchers.xa.R;
import io.keypunchers.xa.misc.Common;

public class CircularNetworkImageView extends NetworkImageView {
    Context mContext;

    public CircularNetworkImageView(Context context) {
        super(context);
        mContext = context;
    }

    public CircularNetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public CircularNetworkImageView(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }
    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            super.setImageBitmap(Common.getCircleImage(bitmap, ContextCompat.getColor(mContext, R.color.color_light_gray), 0));
        } else {
            super.setImageBitmap(bitmap);
        }
    }
}

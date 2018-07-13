package me.christiansoler.xa.misc;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private static BitmapLruCache mBitmapLruCache;

    private VolleySingleton() {}

    static void instantiate(Context context) {
        mBitmapLruCache = new BitmapLruCache();
        mRequestQueue = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueue, mBitmapLruCache);
    }

    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) 
			return mImageLoader;

        throw new IllegalStateException("ImageLoader not instantiated");
    }

    public static RequestQueue getRequestQueque() {
        if (mRequestQueue != null) 
			return mRequestQueue;

        throw new IllegalStateException("RequestQueue not instantiated");
    }

    public static BitmapLruCache getBitmapLruCache() {
        if (mBitmapLruCache != null) 
			return mBitmapLruCache;

        throw new IllegalStateException("BitmapLruCache not instantiated");
    }
}

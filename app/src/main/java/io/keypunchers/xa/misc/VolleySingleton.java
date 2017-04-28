package io.keypunchers.xa.misc;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static RequestQueue mRequestQueque;
    private static ImageLoader mImageLoader;
    private static BitmapLruCache mBitmapLruCache;

    private VolleySingleton() {
    }

    static void instantiate(Context context) {
        mBitmapLruCache = new BitmapLruCache();
        mRequestQueque = Volley.newRequestQueue(context);
        mImageLoader = new ImageLoader(mRequestQueque, mBitmapLruCache);
    }

    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) return mImageLoader;

        throw new IllegalStateException("ImageLoader not instantiated");
    }

    public static RequestQueue getRequestQueque() {
        if (mRequestQueque != null) return mRequestQueque;

        throw new IllegalStateException("RequestQueue not instantiated");
    }

    public static BitmapLruCache getBitmapLruCache() {
        if (mBitmapLruCache != null) return mBitmapLruCache;

        throw new IllegalStateException("BitmapLruCache not instantiated");
    }
}

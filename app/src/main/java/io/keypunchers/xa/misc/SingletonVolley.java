package io.keypunchers.xa.misc;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Sl
 */
public class SingletonVolley {
    // fields
    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    private static BitmapLruCache mBitmapLruCache;

    /**
     * Empty constructor
     */
    private SingletonVolley(){}

    /**
     * Instantiate objects
     *
     * @param mContext The application context
     */
    static void initantiate (Context mContext){
        mBitmapLruCache = new BitmapLruCache();
        mRequestQueue = Volley.newRequestQueue(mContext);
        mImageLoader = new ImageLoader(mRequestQueue, mBitmapLruCache);
    }

    /**
     * Get the RequestQueue instance
     *
     * @return Returns the RequestQueue instance
     */
    public static RequestQueue getRequestQueue(){
        if (mRequestQueue != null){
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    /**
     * Get the ImageLoader instance
     *
     * @return Returns the ImageLoader instance
     */
    public static ImageLoader getImageLoader(){
        if (mImageLoader != null){
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

    /**
     * Get the BitmapLruCache instance
     *
     * @return Returns the BitmapLruCache instance
     */
    public static BitmapLruCache getBitmapLruCache(){
        if (mBitmapLruCache != null){
            return mBitmapLruCache;
        } else {
            throw new IllegalStateException("BitmapLruCache not initialized");
        }
    }
}

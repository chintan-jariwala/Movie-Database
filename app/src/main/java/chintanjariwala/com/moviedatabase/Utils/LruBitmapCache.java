package chintanjariwala.com.moviedatabase.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by chint on 11/11/2016.
 */

public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache{

    public static int getDefaultLruCacheSize(){
        final int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int CACHESIZE = MAX_MEMORY / 8;
        return CACHESIZE;
    }
    public LruBitmapCache(){
        this(getDefaultLruCacheSize());
    }
    public LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight() / 1024;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url,bitmap);
    }
}

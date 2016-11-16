package chintanjariwala.com.moviedatabase.App;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import chintanjariwala.com.moviedatabase.R;
import chintanjariwala.com.moviedatabase.Utils.LruBitmapCache;

/**
 * Created by chint on 11/11/2016.
 */

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getAppContext(){
        return mInstance.getApplicationContext();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

}

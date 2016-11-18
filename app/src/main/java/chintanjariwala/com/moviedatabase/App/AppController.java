package chintanjariwala.com.moviedatabase.app;

import android.app.Application;
import android.content.Context;

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

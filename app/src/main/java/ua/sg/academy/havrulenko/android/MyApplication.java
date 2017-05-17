package ua.sg.academy.havrulenko.android;

import android.app.Application;
import android.content.Context;

import ua.sg.academy.havrulenko.android.sqlite.HelperFactory;

public class MyApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
        mContext = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }

    public static Context getContext() {
        return mContext;
    }

}

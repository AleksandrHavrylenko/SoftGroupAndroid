package ua.sg.academy.havrulenko.android;

import android.app.Application;

import ua.sg.academy.havrulenko.android.sqlite.HelperFactory;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }
    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}

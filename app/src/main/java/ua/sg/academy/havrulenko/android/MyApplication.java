package ua.sg.academy.havrulenko.android;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.File;

import ua.sg.academy.havrulenko.android.sqlite.HelperFactory;

public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private static String IMG_PATH_USERS;
    private static String IMG_PATH_PLACES;

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File usersDir = cw.getDir("users_images", Context.MODE_PRIVATE);
        IMG_PATH_USERS = usersDir.getPath();
        File placesDir = cw.getDir("places_images", Context.MODE_PRIVATE);
        IMG_PATH_PLACES = placesDir.getPath();
        Log.d(TAG, "onCreate: Users image path: " + IMG_PATH_USERS);
        Log.d(TAG, "onCreate: Places image path: " + IMG_PATH_PLACES);
    }

    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }

    public static String getImgPathUsers() {
        return IMG_PATH_USERS;
    }

    public static String getImgPathPlaces() {
        return IMG_PATH_PLACES;
    }
}

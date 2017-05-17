package ua.sg.academy.havrulenko.android.dao;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ua.sg.academy.havrulenko.android.HashUtils;
import ua.sg.academy.havrulenko.android.MyApplication;

public class SharedPreferencesStorage implements UsersDaoInterface {

    private static SharedPreferencesStorage instance;

    private SharedPreferencesStorage() {
    }

    public static SharedPreferencesStorage getInstance() {
        if (instance == null) {
            instance = new SharedPreferencesStorage();
        }
        return instance;
    }

    @Override
    public boolean contains(String email) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        String data = sharedPreferences.getString(email, "");
        return !data.isEmpty();
    }

    @Override
    public String getPasswordByEmail(String email) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        return sharedPreferences.getString(email, "");
    }

    @Override
    public void addUser(String email, String password) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(email, HashUtils.sha512(password));
        editor.apply();
    }

    @Override
    public String getAllRecordsLog() {
        return "Mode: SharedPreferences";
    }

}

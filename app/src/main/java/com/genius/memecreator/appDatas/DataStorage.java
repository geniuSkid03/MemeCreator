package com.genius.memecreator.appDatas;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

public class DataStorage {

    private SharedPreferences preferences;
    private final static String NAME = "meme_factory_prefs";
    private static final String IS_LIVE_SERVER = "IS_LIVE_SERVER";

    private static DataStorage mInstance = null;

    public static DataStorage getInstance(Context context) {
        if (mInstance == null) mInstance = new DataStorage(context);
        return mInstance;
    }

    public DataStorage(Context context) {
        preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public void saveData(String key, String data) {
        preferences.edit().putString(key, data).apply();
    }

    public void saveData(String key, Uri data) {
        preferences.edit().putString(key, String.valueOf(data)).apply();
    }

    public void saveData(String key, boolean data) {
        preferences.edit().putBoolean(key, data).apply();
    }

    public boolean getBoolean(String key, boolean def) {
        return preferences.getBoolean(key, def);
    }

    public int getInt(String key) {
        return preferences.getInt(key, 0);
    }

    public void saveInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public String getData(String key) {
        return preferences.getString(key, null);
    }

    public boolean hasData(String key) {
        return preferences.contains(key);
    }

    public void setLiveServer(boolean liveServer) {
        preferences.edit().putBoolean(IS_LIVE_SERVER, liveServer).apply();
    }

    public boolean isLiveServer() {
        if (!preferences.contains(IS_LIVE_SERVER)) {
            preferences.edit().putBoolean(IS_LIVE_SERVER, true).apply();
        }
        return preferences.getBoolean(IS_LIVE_SERVER, true);
    }

    public void clearAll() {
        preferences.edit().clear().apply();
    }

    public void removeData(String key) {
        preferences.edit().remove(key).apply();
    }
}

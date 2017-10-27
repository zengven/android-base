package com.github.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

/**
 * author: zengven
 * date: 2017/6/7
 * Desc: SharedPreferences缓存工具类
 */
public class PreferencesUtil {

    private static final String PREFERENCE_FILE_NAME = "config";
    private SharedPreferences mPreferences = null;
    private static PreferencesUtil sInstance = null;

    private PreferencesUtil(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PreferencesUtil.class) {
                if (sInstance == null) {
                    sInstance = new PreferencesUtil(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    public boolean putBoolean(String key, boolean value) {
        return mPreferences.edit().putBoolean(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mPreferences.getBoolean(key, defValue);
    }

    public boolean putString(String key, String value) {
        return mPreferences.edit().putString(key, value).commit();
    }

    public String getString(String key, String defValue) {
        return mPreferences.getString(key, defValue);
    }

    public boolean putInt(String key, int value) {
        return mPreferences.edit().putInt(key, value).commit();
    }

    public int getInt(String key, int defValue) {
        return mPreferences.getInt(key, defValue);
    }

    public boolean putFloat(String key, float value) {
        return mPreferences.edit().putFloat(key, value).commit();
    }

    public float getFloat(String key, float defValue) {
        return mPreferences.getFloat(key, defValue);
    }

    public boolean putLong(String key, long value) {
        return mPreferences.edit().putLong(key, value).commit();
    }

    public long getLong(String key, long defValue) {
        return mPreferences.getLong(key, defValue);
    }

    public boolean putStringSet(String key, Set<String> values) {
        return mPreferences.edit().putStringSet(key, values).commit();
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return mPreferences.getStringSet(key, defValues);
    }

    public boolean remove(String key) {
        return mPreferences.edit().remove(key).commit();
    }

    public void remove(String... keys) {
        if (keys.length != 0) {
            for (String key : keys) {
                remove(key);
            }
        }
    }

    public void clear() {
        mPreferences.edit().clear().apply();
    }

}

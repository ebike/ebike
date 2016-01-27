package com.jcsoft.emsystem.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.io.File;
import java.io.Serializable;

/**
 * sharePreferences 数据的存储
 * by dive 2015/08/11
 */
public class PreferencesUtil implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String sharedPreferencesInfo = "ebike";

    private static Context myContext;
    private static SharedPreferences saveInfo;
    private static Editor saveEditor;
    private static PreferencesUtil sharedPreferencesUtil = new PreferencesUtil();

    public static void init(Context context) {
        myContext = context;
    }

    public static PreferencesUtil getinstance(Context context) {
        myContext = context;
        if (saveInfo == null && myContext != null) {
            saveInfo = myContext.getSharedPreferences(sharedPreferencesInfo, Context.MODE_PRIVATE);
            saveEditor = saveInfo.edit();
        }
        return sharedPreferencesUtil;
    }

    public static PreferencesUtil getInstance() {
        if (saveInfo == null && myContext != null) {
            saveInfo = myContext.getSharedPreferences(sharedPreferencesInfo, Context.MODE_PRIVATE);
            saveEditor = saveInfo.edit();
        }
        return sharedPreferencesUtil;
    }

    public static String getPrefString(Context context, String key, final String defaultValue) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getString(key, defaultValue);
    }

    public static void setPrefString(Context context, final String key, final String value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putString(key, value).commit();
    }

    public static boolean getPrefBoolean(Context context, final String key, final boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getBoolean(key, defaultValue);
    }

    public static boolean hasKey(Context context, final String key) {
        return PreferenceManager.getDefaultSharedPreferences(context).contains(key);
    }

    public static void setPrefBoolean(Context context, final String key, final boolean value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putBoolean(key, value).commit();
    }

    public static void setPrefInt(Context context, final String key, final int value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putInt(key, value).commit();
    }

    public static int getPrefInt(Context context, final String key, final int defaultValue) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getInt(key, defaultValue);
    }

    public static void setPrefFloat(Context context, final String key, final float value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putFloat(key, value).commit();
    }

    public static float getPrefFloat(Context context, final String key, final float defaultValue) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getFloat(key, defaultValue);
    }

    public static void setSettingLong(Context context, final String key, final long value) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings.edit().putLong(key, value).commit();
    }

    public static long getPrefLong(Context context, final String key, final long defaultValue) {
        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        return settings.getLong(key, defaultValue);
    }

    public static void clearPreference(Context context, final SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }

    public static void deletePreference(Context context) {
        File file = new File("/data/data/" + context.getPackageName().toString() + "/shared_prefs", sharedPreferencesInfo + ".xml");
        if (file.exists()) {
            file.delete();
        }
    }

}

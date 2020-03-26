package pw.xiaohaozi.android_utils.utils;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashSet;
import pw.xiaohaozi.android_utils.Utils;

/**
 * SharePreference封装
 */
public class SPUtils extends Utils {

    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void setBoolean(String key, boolean value) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static void setString(String key, String value) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defValue) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static HashSet<String> getStringSet(String key, HashSet<String> defValue) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        return (HashSet<String>) sp.getStringSet(key, defValue);
    }

    public static void setStringSet(String key, HashSet<String> defValue) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        sp.edit().putStringSet(key, defValue).apply();
    }

    public static void setInt(String key, int value) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defValue) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }
    public static void setLong(String key, long value) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).apply();
    }

    public static long getLong(String key, long defValue) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }
    public static void setFloat(String key, float value) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        sp.edit().putFloat(key, value).apply();
    }

    public static float getFloat(String key, float defValue) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        return sp.getFloat(key, defValue);
    }
}

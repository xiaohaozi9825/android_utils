package pw.xiaohaozi.zkr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

import pw.xiaohaozi.zkr.Utils;

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
        sp.edit().putString(key, value).commit();
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
        sp.edit().putStringSet(key, defValue).commit();
    }

    public static void setInt(String key, int value) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        SharedPreferences sp = sContext.getSharedPreferences(sSPName, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }
}

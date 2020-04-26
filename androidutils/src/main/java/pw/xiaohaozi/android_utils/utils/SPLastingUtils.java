package pw.xiaohaozi.android_utils.utils;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.lang.reflect.Type;
import java.util.HashMap;

import androidx.annotation.RequiresApi;

/**
 * 本地偏好参数存储工具类
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SPLastingUtils {
    private static final String TAG = "LoadSPUtils";
    private static Gson sGson = new GsonBuilder().enableComplexMapKeySerialization().create();
    private static File sFile;

    public static void init(String fileName, String... filePaths) {
        sFile = FileUtils.getFile(FileUtils.getSDCardDir(), fileName, filePaths);
    }

    public static void set(String key, String value) {
        String json = toJson(key, value);
        FileUtils.writer(sFile, json);
    }

    public static void set(String key, boolean value) {
        set(key, String.valueOf(value));
    }

    public static <V extends Number> void set(String key, V value) {
        set(key, String.valueOf(value));
    }

    public static <V> void set(String key, V value) {
        set(key, new Gson().toJson(value));
    }


    public static String get(String key, String def) {
        String json = FileUtils.readString(sFile);
        HashMap<String, String> hashMap = fromMap(json);
        if (hashMap == null) return def;
        String s = hashMap.get(key);
        if (s == null) return def;
        return s;
    }

    public static String get(String key) {
        String json = FileUtils.readString(sFile);
        HashMap<String, String> hashMap = fromMap(json);
        if (hashMap == null) return null;
        String s = hashMap.get(key);
        if (s == null) return null;
        return s;
    }

    public static int get(String key, int def) {
        return Integer.parseInt(get(key, String.valueOf(def)));
    }

    public static long get(String key, long def) {
        return Long.parseLong(get(key, String.valueOf(def)));
    }

    public static byte get(String key, byte def) {
        return Byte.parseByte(get(key, String.valueOf(def)));
    }

    public static short get(String key, short def) {
        return Short.parseShort(get(key, String.valueOf(def)));
    }

    public static float get(String key, float def) {
        return Float.parseFloat(get(key, String.valueOf(def)));
    }

    public static double get(String key, double def) {
        return Double.parseDouble(get(key, String.valueOf(def)));
    }

    public static boolean get(String key, boolean def) {
        return Boolean.parseBoolean(get(key, String.valueOf(def)));
    }

    /**
     * 获取Object类型键值对
     * @param key
     * @param def
     * @param <V>
     * @return
     */
    public static <V> V get(String key, V def) {
        String json = FileUtils.readString(sFile);
        HashMap<String, String> hashMap = fromMap(json);
        if (hashMap == null) return def;
        String s = hashMap.get(key);
        if (s == null) return def;

        return (V) new Gson().fromJson(s, def.getClass());
    }

    /**
     * 获取任意类型的键值对，可以是数字，字符串，布尔，object
     * @param key
     * @param vClass
     * @param <V>
     * @return
     */
    public static <V> V get(String key, Class<V> vClass) {
        String json = FileUtils.readString(sFile);
        HashMap<String, String> hashMap = fromMap(json);
        if (hashMap == null) return null;
        String s = hashMap.get(key);
        if (s == null) return null;
        return new Gson().fromJson(s, vClass);
    }

    /**
     * 移除指定的键值对
     *
     * @param key
     * @return
     */
    public static boolean remove(String key) {
        String json = FileUtils.readString(sFile);
        HashMap<String, String> hashMap = fromMap(json);
        if (hashMap == null) return false;
        String s = hashMap.remove(key);
        if (s == null) return false;
        return FileUtils.writer(sFile, sGson.toJson(hashMap));
    }

    /**
     * 清空所有键值对
     *
     * @return
     */
    public static boolean clear() {
        return FileUtils.writer(sFile, "");
    }

    //辅助方法
    private static String toJson(String key, String value) {
        HashMap<String, String> map;
        String json = FileUtils.readString(sFile);
        if (StrUtils.isEmpty(json)) map = new HashMap<>();
        else map = fromMap(json);
        map.put(key, value);
        return sGson.toJson(map);
    }

    //辅助方法
    private static HashMap<String, String> fromMap(String json) {
        Type type = new TypeToten(HashMap.class, String.class, String.class);
        return sGson.fromJson(json, type);
    }
}

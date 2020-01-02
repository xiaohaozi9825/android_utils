package pw.xiaohaozi.android_utils;

import android.content.Context;

public class Utils {
    public static Context sContext;
    protected static String sSPName = "config";

    /**
     * 初始化，首次调用必须先初始化
     * @param context
     */
    public static void init(Context context) {
        sContext = context;
    }

    public static void setSPName(String sp_name) {
        sSPName = sp_name;
    }
}

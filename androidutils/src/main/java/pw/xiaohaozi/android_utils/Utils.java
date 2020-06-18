package pw.xiaohaozi.android_utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

public class Utils {
    public static Context sContext;
    /**
     * 应用是否是debug模式
     * 如果想强制转换成debug，可以在 application 标签下添加：
     * android:debuggable="true" tools:ignore="HardcodedDebugMode"
     */
    public static boolean IS_DEBUG = false;
    protected static String sSPName = "config";

    /**
     * 初始化，首次调用必须先初始化
     *
     * @param context
     */
    public static void init(Context context) {
        sContext = context;
        try {
            IS_DEBUG = (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            IS_DEBUG = false;
        }
    }

    public static void setSPName(String sp_name) {
        sSPName = sp_name;
    }

}

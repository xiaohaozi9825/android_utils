package pw.xiaohaozi.zkr;

import android.content.Context;

public class Utils {
    public static Context sContext;
    protected static String sSPName = "config";

    public static void init(Context context) {
        sContext = context;
    }

    public static void setSPName(String sp_name) {
        sSPName = sp_name;
    }
}

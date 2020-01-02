package pw.xiaohaozi.android_utils.utils;

import pw.xiaohaozi.android_utils.Utils;

/**
 * 单位转换工具
 */
public class DisplayUtils extends Utils {

    /**
     * 将px转换为与之相等的dp
     */
    public static int px2dp(float pxValue) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 将dp转换为与之相等的px
     */
    public static int dp2px(float dipValue) {
        final float scale = sContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 将px转换为sp
     */
    public static int px2sp(float pxValue) {
        final float fontScale = sContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 将sp转换为px
     */
    public static int sp2px(float spValue) {
        final float fontScale = sContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
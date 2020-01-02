package pw.xiaohaozi.android_utils.utils;

/**
 * 字符串工具类
 */
public class StrUtils {
    /**
     * 去除字符串首尾空字符串
     * 主要对字符串做了非空判断，防止空指针异常
     *
     * @param str
     * @return
     */
    public static String trim(String str) {
        return str == null ? "" : str.trim();
    }

    /**
     * 字符串是否为空
     * 比 TextUtils.isEmpty(str)更严格，空字符串也会返回true
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }
}
